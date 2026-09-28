# Crypto Repository Modernization Plan

Repository: `kelly001/crypto`  
Current baseline: Java 8 bytecode (class-file version 52), largely Java 7-era coding style  
Target: Java 25 LTS

## Goals

Modernize the project so it:

- builds reproducibly with Java 25 LTS;
- uses a standard dependency/build system instead of checked-in JARs;
- replaces obsolete or insecure cryptographic practices;
- has automated tests around certificate, key, database, and PKCS#12 behavior;
- uses modern Java features where they improve clarity and safety;
- preserves the original application concept and Swing UI initially;
- is easy to run locally and suitable as a portfolio/refresher project.

The first goal is **not** to rewrite the application or replace Swing. The migration should separate build/runtime modernization, security fixes, and code-style refactoring so regressions are easier to identify.

---

# Phase 0 — Establish the Baseline

## Tasks

- Clone the repository into a clean workspace.
- Document the current structure:
  - `src/`
  - `lib/`
  - `out/`
  - `META-INF/`
  - `files/`
  - `crypto_db.sql`
- Record the current third-party dependencies:
  - Bouncy Castle `bcprov-jdk15on-151`
  - Bouncy Castle `bcpkix-jdk15on-151`
  - MySQL Connector/J `5.1.34`
  - `teacode-common.jar`
- Identify the main entry point:
  - `com.zpayment.MainFrame`
- Document the application flows:
  - login;
  - company/user management;
  - certificate creation;
  - key generation;
  - certificate revocation;
  - PKCS#12 generation;
  - persistence to MySQL.

## Verification

Before refactoring, write down which parts of the original project still run and which already fail because of old dependencies or environment assumptions.

---

# Phase 1 — Introduce a Modern Build

## Recommended approach

Use **Maven**.

The project has a relatively small dependency graph and Maven is a good fit for a traditional Java application being modernized from an IDE-managed structure.

## Tasks

Create:

```text
pom.xml

src/
  main/
    java/
    resources/
  test/
    java/
```

Move production Java sources from:

```text
src/
```

to:

```text
src/main/java/
```

Move configuration/resources to:

```text
src/main/resources/
```

Configure Java 25:

```xml
<properties>
    <maven.compiler.release>25</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

Remove build artifacts from version control:

```text
out/
*.class
*.jar
```

Add them to `.gitignore`.

Do not delete the original checked-in libraries until their Maven replacements are verified.

## Verification

The project should compile through:

```bash
mvn clean compile
```

with all dependencies resolved by Maven.

---

# Phase 2 — Replace Checked-In Dependencies

## Bouncy Castle

Current:

```text
bcprov-jdk15on-151.jar
bcpkix-jdk15on-151.jar
```

Replace them with current Bouncy Castle Maven artifacts compatible with Java 25.

Expected artifacts will be from the modern `jdk18on` family.

### Likely code changes

Old APIs in the project include:

```java
PEMWriter
X509Extension
AuthorityKeyIdentifierStructure
```

Some of these are obsolete or replaced by newer Bouncy Castle APIs.

For example, replace old PEM writing code with modern APIs such as:

```java
JcaPEMWriter
```

Review all imports in:

```text
src/com/zpayment/Security.java
```

against the current Bouncy Castle API.

## MySQL

Current:

```text
mysql-connector-java-5.1.34
```

Replace with the current MySQL Connector/J.

Remove:

```java
Class.forName("com.mysql.jdbc.Driver");
```

Modern JDBC drivers support automatic driver registration.

If explicit loading is temporarily needed during migration, the modern class is:

```java
com.mysql.cj.jdbc.Driver
```

## `teacode-common.jar`

Determine whether this dependency is:

- still required;
- replaceable by a public dependency;
- dead code;
- or a bundled/custom helper library.

Search for every referenced class from this JAR before deciding how to handle it.

## Verification

- No application dependency should be loaded manually from `lib/`.
- `mvn dependency:tree` should show all required dependencies.
- Application should compile on Java 25.

---

# Phase 3 — Create a Test Safety Net

Do this before large refactoring.

## Testing stack

Use:

- JUnit 5;
- AssertJ optionally;
- Testcontainers for MySQL if database integration tests are needed.

## Priority tests

### Certificate generation

Test:

- root CA certificate generation;
- user certificate generation;
- certificate subject fields;
- issuer fields;
- expiration dates;
- basic constraints;
- key usage;
- signature verification.

### Key handling

Test:

- public key serialization;
- private key serialization;
- reloading keys from disk;
- invalid key input;
- missing files.

### PKCS#12

Test:

- `.p12` generation;
- correct password behavior;
- certificate/key presence;
- invalid password handling.

### Database

Test:

- user creation;
- company creation;
- certificate persistence;
- certificate lookup;
- certificate revocation;
- loading by user;
- invalid IDs;
- missing database connection.

### Existing inconsistencies to cover

There appears to be a naming inconsistency between queries using:

```sql
certificate
```

and:

```sql
certificates
```

Add a test before correcting this so the expected database schema is explicit.

## Verification

A meaningful subset of core behavior should be covered before changing crypto algorithms or model structure.

Run:

```bash
mvn test
```

---

# Phase 4 — Fix Resource Management

The current JDBC and file code manually closes many resources.

Replace patterns like:

```java
Connection con = Database.getConnection();
PreparedStatement preparedStatement = null;

try {
    ...
} finally {
    if (preparedStatement != null) {
        preparedStatement.close();
    }
}
```

with try-with-resources:

```java
try (
    var connection = Database.getConnection();
    var statement = connection.prepareStatement(query)
) {
    ...
}
```

Apply this to:

- `Connection`;
- `Statement`;
- `PreparedStatement`;
- `ResultSet`;
- `InputStream`;
- `OutputStream`;
- `Reader`;
- `Writer`.

## Benefits

- prevents resource leaks;
- reduces boilerplate;
- simplifies exception handling;
- makes Java 7+ resource management explicit and reliable.

## Verification

Run all database and file-related tests after each group of changes.

---

# Phase 5 — Modernize Configuration

Remove hardcoded database configuration:

```java
username = "crypto";
password = "crypto";
server = "localhost";
port = "3306";
database = "crypto";
```

Replace with environment variables or a configuration file.

Example:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Add sensible local-development defaults only where safe.

Do not commit real credentials.

## Verification

The application should run using environment-specific configuration without code changes.

---

# Phase 6 — Security and Cryptography Modernization

This is the highest-risk phase and should be done only after tests exist.

## 6.1 Replace SHA-1 / DSA usage

Current patterns include:

```java
SHA1withDSA
```

and:

```java
KeyPairGenerator.getInstance("DSA", "BC");
keyGen.initialize(1024, random);
```

Do not preserve this as the default modern implementation.

Choose a modern certificate-signing approach, for example:

```text
RSA 3072 + SHA-256
```

or:

```text
ECDSA using a modern named curve + SHA-256
```

For a portfolio/refresher project, RSA may be simpler to reason about and demonstrate.

## 6.2 Replace RC2-based PKCS#12 protection

Current code uses:

```java
pbeWithSHAAnd128BitRC2_CBC
```

Replace it with a current secure algorithm supported by the modern Bouncy Castle PKCS#12 APIs.

## 6.3 Replace MD5 password hashing

Current:

```java
MessageDigest.getInstance("MD5");
```

with a static salt:

```java
"securesalt123!!!!!!"
```

Replace this entirely.

For stored passwords, use a real password hashing algorithm such as:

- Argon2id;
- bcrypt;
- PBKDF2 if avoiding another dependency.

Each password must use a unique random salt.

## 6.4 Fix key specification handling

Review code that uses:

```java
X509EncodedKeySpec
```

for private keys.

Public keys normally use:

```java
X509EncodedKeySpec
```

Private keys normally use:

```java
PKCS8EncodedKeySpec
```

Add round-trip tests before modifying this code.

## 6.5 Secure randomness

Review explicit provider-specific calls such as:

```java
SecureRandom.getInstance("SHA1PRNG", "SUN")
```

Prefer:

```java
new SecureRandom()
```

unless there is a concrete interoperability requirement.

## Verification

Add security-oriented tests for:

- generated key sizes;
- signature algorithms;
- certificate validity;
- key round trips;
- password hashing;
- PKCS#12 loading.

Do not treat "it compiles" as sufficient verification for this phase.

---

# Phase 7 — Replace Legacy Date/Time Code

Current code uses:

```java
Date
Calendar
Timestamp
```

For business/domain logic, migrate to `java.time`:

```java
Instant
LocalDate
LocalDateTime
Duration
```

Example:

```java
var issuedAt = Instant.now();
var expiresAt = issuedAt.plus(365, ChronoUnit.DAYS);
```

Convert back to legacy `Date` only at API boundaries where a library requires it.

For certificate validity, ensure the semantics are intentional:

```java
plusYears(1)
```

may be preferable to simply adding 365 days.

## Verification

Add tests around:

- issue date;
- expiration date;
- leap years;
- conversion between `Instant` and certificate APIs.

---

# Phase 8 — Introduce Stronger Domain Types

The project passes certificate input through structures such as:

```java
Map<String, String>
HashMap<String, String>
```

with keys like:

```text
username
organization
country
email
filename
```

This is fragile and loses compile-time safety.

Replace these maps with typed request/domain objects.

Example:

```java
public record CertificateRequest(
    String username,
    String organization,
    String department,
    String locality,
    String state,
    String country,
    String email,
    String filename
) {}
```

Benefits:

- compiler catches missing/renamed fields;
- easier refactoring;
- easier testing;
- clear API contracts;
- fewer string-key mistakes.

Do not automatically convert mutable database entities into records. Use records first for immutable DTOs, requests, responses, and configuration objects.

---

# Phase 9 — Modern Java Syntax and Style

Apply modern features selectively.

## Lambdas

Replace suitable anonymous listener classes.

Old:

```java
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        saveUser();
    }
});
```

Modern:

```java
button.addActionListener(e -> saveUser());
```

This is especially relevant in the Swing UI.

## Type inference

Replace unnecessarily verbose local declarations:

```java
ArrayList<Certificate> certificates =
    new ArrayList<Certificate>();
```

with:

```java
var certificates = new ArrayList<Certificate>();
```

Use `var` where the type remains obvious.

## Collections

Prefer interfaces:

```java
List<Certificate>
```

over:

```java
ArrayList<Certificate>
```

in public APIs and variable declarations where the concrete implementation is not important.

## Streams

Use streams where they make transformations clearer.

Do **not** convert loops mechanically.

A JDBC loop like:

```java
while (rs.next()) {
    ...
}
```

is perfectly acceptable.

## Pattern matching

Where relevant, modern `instanceof` pattern matching can replace cast-heavy code.

## Switch expressions

Use modern switch expressions if the code contains branching that benefits from them.

## Text blocks

Use text blocks for longer SQL or structured strings where they materially improve readability.

---

# Phase 10 — Improve Error Handling and Logging

The current code frequently uses:

```java
System.out.println(...)
e.printStackTrace()
```

and sometimes catches broad:

```java
Exception
```

Replace this gradually with structured logging.

Recommended option:

- SLF4J API;
- Logback implementation.

Avoid swallowing exceptions.

For example, code like:

```java
catch (Exception e) {
    e.getLocalizedMessage();
}
```

should be corrected because it effectively ignores the failure.

Define clear application-level exceptions where useful:

```text
CertificateGenerationException
KeyStorageException
DatabaseException
```

Do not over-engineer the exception hierarchy.

---

# Phase 11 — Database Layer Cleanup

The current model classes contain SQL directly.

Initially, keep the design simple but separate responsibilities.

Possible structure:

```text
database/
  Database.java
  UserRepository.java
  CompanyRepository.java
  CertificateRepository.java

model/
  User.java
  Company.java
  Certificate.java
```

Move SQL statements out of domain objects.

This makes:

- tests easier;
- resource handling clearer;
- future database replacement easier;
- UI/domain/database dependencies less tangled.

Do not introduce Hibernate/JPA unless there is a specific reason. Plain JDBC is sufficient for this project and keeps the modernization focused on Java fundamentals.

---

# Phase 12 — Clean Repository Structure

Remove from Git:

```text
out/
*.class
*.jar
Crypto.rar
crypto-130215.rar
```

unless there is a historical reason to preserve archives.

Review committed files such as:

```text
*.p12
private keys
RootKey
certificate/key test files
```

Private keys and generated credentials generally should not be stored in a public repository.

Replace them with test fixtures generated during test execution where possible.

Update `.gitignore` for:

```text
target/
*.class
*.jar
*.p12
*.key
.env
.idea/
```

Be careful: removing sensitive data from the latest commit does not remove it from Git history. If the repository has ever contained real secrets, treat them as compromised and consider history cleanup separately.

---

# Phase 13 — Documentation

Replace the currently empty `README.md`.

Include:

- project purpose;
- historical background;
- Java version;
- architecture overview;
- requirements;
- how to start MySQL;
- how to initialize schema;
- how to configure environment variables;
- how to build;
- how to run;
- how to run tests;
- certificate-generation overview;
- security limitations;
- modernization notes.

Add a short section such as:

```text
Originally developed in 2014–2015 and later modernized from
an IDE-managed Java 8 codebase to Java 25 LTS.
```

That makes the migration history clear without pretending the original code was written using modern Java.

---

# Phase 14 — CI

Add GitHub Actions.

Pipeline:

```text
checkout
→ install Java 25
→ mvn clean verify
→ dependency/security checks
```

Recommended additions:

- Maven dependency caching;
- test report publishing;
- dependency vulnerability scanning;
- optional static analysis.

Possible tools:

- OWASP Dependency-Check;
- SpotBugs;
- Checkstyle only if desired;
- Dependabot for Maven dependencies.

Do not make style tooling the focus of the project.

---

# Suggested Execution Order

## Milestone 1 — Reproducible Java 25 build

1. Create Maven structure.
2. Move source files.
3. Declare dependencies.
4. Upgrade MySQL driver.
5. Upgrade Bouncy Castle enough to compile.
6. Remove dependency on checked-in JARs.
7. Get `mvn clean compile` passing.

Deliverable:

```text
Project builds on Java 25.
```

## Milestone 2 — Behavior protected by tests

1. Add JUnit 5.
2. Add certificate generation tests.
3. Add key round-trip tests.
4. Add database integration tests.
5. Add PKCS#12 tests.

Deliverable:

```text
Core legacy behavior is reproducible and tested.
```

## Milestone 3 — Security modernization

1. Replace SHA1withDSA.
2. Replace 1024-bit DSA.
3. Replace RC2 PKCS#12 encryption.
4. Replace MD5 password hashing.
5. Fix private/public key encoding.
6. Review committed key material.

Deliverable:

```text
No intentionally obsolete cryptographic primitives remain
in the default application flow.
```

## Milestone 4 — Modern Java refactoring

1. Introduce typed certificate request records.
2. Replace legacy date/time logic.
3. Add try-with-resources.
4. Introduce lambdas in Swing listeners.
5. Simplify collection code.
6. Use modern language features selectively.

Deliverable:

```text
Code looks and behaves like a modern Java application rather
than a Java 7-era project merely compiled with Java 25.
```

## Milestone 5 — Architecture and developer experience

1. Extract repositories from model classes.
2. Add configuration handling.
3. Add structured logging.
4. Clean repository.
5. Improve README.
6. Add GitHub Actions.

Deliverable:

```text
The repository is maintainable, reproducible, documented,
and suitable as a portfolio/refresher project.
```

---

# Things Not to Do Initially

Avoid these until the Java 25 migration is stable:

- rewriting Swing as React;
- converting the application to Spring Boot;
- introducing Hibernate/JPA;
- splitting it into microservices;
- replacing every loop with streams;
- introducing modules with `module-info.java` just because Java supports them;
- rewriting all models as records;
- doing large architecture changes before tests exist.

These changes would obscure the real goal: learning and demonstrating modern Java by evolving a real Java 8 codebase.

---

# Main Risks

## Bouncy Castle API changes

This is the most likely source of compile-time migration work.

Mitigation:

- migrate incrementally;
- keep certificate tests around every change;
- compare generated certificate properties before and after migration.

## Crypto behavior changes

Modern algorithms may change output formats, key sizes, or interoperability.

Mitigation:

- explicitly define expected behavior rather than expecting identical binary output;
- test parsing and verification rather than byte-for-byte equality.

## Database assumptions

The SQL schema and code contain old conventions and possible table-name inconsistencies.

Mitigation:

- establish integration tests using a fresh database created from `crypto_db.sql`.

## Unknown custom library

`teacode-common.jar` may contain functionality that is not available through Maven Central.

Mitigation:

- identify every reference first;
- remove if unused;
- otherwise isolate or replace it.

---

# Definition of Done

The modernization is complete when:

- Java 25 LTS is the documented and enforced build version;
- `mvn clean verify` passes;
- no application dependencies are committed as JARs;
- core certificate and database behavior has automated tests;
- obsolete crypto primitives are removed from default application behavior;
- database credentials are externalized;
- JDBC/file resources use safe lifecycle management;
- core domain inputs use typed objects rather than arbitrary string maps;
- the repository contains no generated build output or private key material;
- GitHub Actions runs the build and tests;
- README documents setup, architecture, security choices, and migration history.

---

# Recommended First Pull Requests

Keep the work split into reviewable changes.

### PR 1 — Maven and Java 25 project structure

No functional refactoring.

### PR 2 — Dependency modernization

Upgrade MySQL and Bouncy Castle and fix compilation.

### PR 3 — Test foundation

JUnit 5 plus certificate/key/database tests.

### PR 4 — Resource and configuration cleanup

Try-with-resources, database configuration, logging.

### PR 5 — Cryptography modernization

Modern signature algorithms, key generation, PKCS#12 protection, password hashing.

### PR 6 — Modern Java language refactoring

Records, lambdas, `java.time`, collection cleanup, selected modern syntax.

### PR 7 — Repository and CI cleanup

README, GitHub Actions, ignored/generated files, dependency scanning.

This order keeps functional changes separated from infrastructure changes and makes regressions much easier to diagnose.
