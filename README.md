# Crypto

Crypto is a Java desktop application for managing companies, employees, X.509
certificates, keys, and PKCS#12 containers. Originally developed in 2014–2015,
it retains its Swing UI and direct JDBC persistence while being modernized.
Existing application text is mostly Russian; a fully English application is the
target, with incremental translation as code is updated.

**Status: Phases 1 and 2 implemented.** The project builds from source on Java 25
with Maven, resolves dependencies from Maven Central, and includes focused build
and dependency compatibility tests. Full behavioral tests, database migrations,
security modernization, and CI remain future work. See the
[phase report](docs/phase-1-2-build.md) and [modernization plan](docs/crypto_java25_modernization_plan.md).

## Requirements and build

- JDK **25** (set `JAVA_HOME` to the JDK directory).
- Network access to Maven Central on the first build.
- A graphical desktop to run the Swing application.
- A disposable MySQL instance for database flows. The upgraded driver targets
  modern MySQL; compatibility with the original MySQL 5.5 dump is not established.

The included Apache Maven wrapper pins Maven **3.10.0**, verifies its distribution
SHA-256, and downloads it if needed. A separate Maven installation is unnecessary.

From PowerShell at the repository root:

```powershell
$env:JAVA_HOME = 'C:\path\to\jdk-25'
.\mvnw.cmd clean compile
.\mvnw.cmd clean verify
.\mvnw.cmd dependency:tree
```

On Linux/macOS, use `sh ./mvnw` in place of `.\mvnw.cmd`. The build requires Java
25 and Maven 3.9.12 or later within the Maven 3 series. IDE users should import
`pom.xml` and select JDK 25; no manual JAR classpath configuration is needed.

## Run

```powershell
.\mvnw.cmd package
& "$env:JAVA_HOME\bin\java.exe" -jar target\crypto-1.0-SNAPSHOT.jar
```

The JAR manifest selects `com.zpayment.MainFrame` and loads runtime dependencies
from `target/dependency/`. Keep that directory beside the application JAR when
copying the build. Bouncy Castle's signed JARs are kept intact rather than merged
into an executable JAR. Icons are loaded from packaged classpath resources.

Run in an isolated working directory with a writable `files/` directory for
generated certificates/containers. Legacy code also writes some key files in the
working directory. Never use committed demonstration keys for real credentials.

The application still connects to `jdbc:mysql://localhost:3306/crypto`, with
username/password `crypto`, hardcoded in `src/main/java/database/Database.java`.
Environment variables are **not supported yet**. No database is needed to build
or run the compatibility tests; they do not open a database connection.

`crypto_db.sql` is historical schema evidence, not a modern migration system. It
starts with `DROP DATABASE crypto`, contains old grant syntax, and has no seeded
application users. Do not import it into a database you need. Executable,
versioned migrations and verified fresh setup/data-preserving upgrades are
planned alongside the database test foundation. Login and CRUD are not yet
verified with the new driver against a running database.

## Tests

```powershell
.\mvnw.cmd test
# Additional dialog checks on a machine with a graphical desktop:
.\mvnw.cmd '-Djava.awt.headless=false' '-Dtest=DialogCompatibilityTest' test
```

The default run is headless: five checks run and three dialog tests are skipped.
Tests cover Java 25 bytecode, packaged resource availability, JDBC service
discovery, BC provider registration, legacy certificate generation/PEM exports,
and the replacement form layout. Desktop tests cover modal dialog OK, cancel,
Escape, and close behavior without displaying application windows.

Generated test keys live under `target/test-work/` and are deleted by the tests.
These checks do not constitute the Phase 3 suite or prove secure algorithms,
valid company-issued chains, PKCS#12 interoperability, or database correctness.
The unused standalone `Test` experiments have been removed; runnable diagnostics
remain under `docs/baseline/` and `docs/verification/`.

## Structure and dependencies

| Path | Purpose |
| --- | --- |
| `src/main/java/com/zpayment/` | Swing application, login, and crypto operations |
| `src/main/java/com/zpayment/ui/` | Project-owned Swing form/dialog helpers |
| `src/main/java/database/` | Models and direct JDBC persistence |
| `src/main/java/external/` | Browser-link UI helpers |
| `src/main/resources/` | Packaged icons |
| `src/test/java/` | Focused JUnit 5 compatibility tests |
| `pom.xml`, `.mvn/`, `mvnw*` | Pinned build and wrapper |
| `target/` | Ignored build output and resolved runtime JARs |
| `files/`, `RootKey`, `key.p12` | Historical demonstration key/container material |
| `docs/` | Modernization plan, baseline, and phase reports |

Production dependencies are Bouncy Castle `bcprov-jdk18on` / `bcpkix-jdk18on`
**1.86** and MySQL Connector/J **26.10.0**. Maven resolves BC utility and Protobuf
transitives. Tests use JUnit Jupiter **5.14.4**. Versions and build plugins are
pinned in `pom.xml`. The old `teacode-common.jar` was replaced with standard Swing
helpers; no application dependency is loaded from a checked-in JAR or `lib/`.

## Known limitations

MD5 password hashing, SHA-1/1024-bit DSA signing, RC2-based PKCS#12 protection,
inconsistent salting, and historical committed private keys remain. This is not
ready for production security use. Phase 2 updates library APIs and registers BC
programmatically; it does not modernize those algorithms.

The private-key decoding bug, user
certificate issuer/signing mismatch, password-dialog cancellation bug, manual
resource management, and swallowed exceptions remain for tested follow-up work.
Revocation changes database state and attempts local file deletion; it is not a
CRL/OCSP service. Help menu links still point to removed HTML files.

The Java cleanup removed the unused plural-table certificate lookup, duplicate
query execution, abandoned UI alternatives, and unused model/helper APIs. This
does not establish correctness of the remaining live database flows.

The [Phase 0 report](docs/phase-0-baseline.md) is a historical record. Its old source
paths and binary diagnostics describe the pre-Maven revision; use that revision
to reproduce them. Historical build output, bundled JARs, and conflicting
manifests have now been removed from the working source tree.
