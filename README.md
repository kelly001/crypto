# Crypto

Crypto is a legacy Java desktop application for managing companies, employees,
X.509 certificates, keys, and PKCS#12 containers. It was developed in 2014–2015,
uses a Russian-language Swing interface, and stores account and certificate
metadata in MySQL.

**Status: modernization in progress.** The application is still an IDE-managed
codebase with bundled dependencies and historical build output. Java 25 LTS is
the target, not an established build requirement or verified runtime today.
There is no Maven/Gradle build, automated test suite, or CI configuration yet.

The [modernization plan](docs/crypto_java25_modernization_plan.md) describes future
work; its completion criteria are not claims about the current implementation.
The initial work documents the legacy baseline before changing application code.

## Current structure

| Path | Purpose |
| --- | --- |
| `src/com/zpayment/` | Swing windows, login logic, certificate/key operations in `Security.java` |
| `src/database/` | Models and direct JDBC persistence (`User`, `Company`, `Employer`, `Certificate`, `Key`) |
| `src/external/` | Browser-link UI helpers |
| `lib/` | Checked-in Bouncy Castle 1.51 provider/PKIX JARs, MySQL Connector/J 5.1.34, and `teacode-common.jar` Swing helpers |
| `out/` | Historical compiled classes and packaged application/dependency JARs; not a reproducible build |
| `META-INF/`, `src/META-INF/` | Conflicting historical packaging manifests |
| `files/` | Historical certificate, key, and PKCS#12 files |
| `crypto_db.sql` | Legacy MySQL schema and account grants |
| `docs/` | Original application help pages and modernization documentation |

UI actions call models containing SQL directly. `Security` uses Bouncy Castle for
certificate creation, key serialization, and PKCS#12 export. Company certificates
and employee certificates take different paths through the certificate dialog.
Revocation changes database status and attempts to delete a local certificate;
it is not a documented CRL/OCSP service.

## Legacy setup and execution

Use an isolated development environment with disposable data. The historical
baseline is Java 8 bytecode with older Java coding conventions. A JDK, a graphical
desktop, the four JARs in `lib/`, and MySQL are needed for a full legacy run.
The SQL dump identifies its original server as MySQL 5.5.25; compatibility with a
modern server has not been established.

The application connects to `jdbc:mysql://localhost:3306/crypto` with username
`crypto` and password `crypto`, hardcoded in `src/database/Database.java`.
Environment variables are **not supported yet**. Start a disposable MySQL server
on that endpoint before exercising database flows.

Review `crypto_db.sql` before importing it: it starts with `DROP DATABASE crypto`,
recreates the database, and includes old account-grant syntax. It contains no
seeded application users. Do not import it into an existing database you need.
Schema import and initial account creation still require baseline verification.

For legacy IDE setup, mark `src/` as the source root, add all four `lib/*.jar`
files to the classpath, and use the repository root as the working directory so
`icon.jpg`, `docs/`, and `files/` resolve. Use `com.zpayment.MainFrame` as the
application entry point. The root manifest instead points to `LoginFrame` and
should not be treated as authoritative.

For diagnostic runs of the **historical binaries**, from PowerShell at the
repository root with Java on PATH:

```powershell
java -cp 'out/production/crypto;lib/*' com.zpayment.MainFrame
```

This does not rebuild source or prove that the checked-in binaries match it.
A repeatable source build and Java 25 run instructions will be added during the
build modernization phase. There is currently no supported `mvn test` command;
classes named `Test` are standalone experiments, not an automated test suite.

## Known limitations

The legacy code uses MD5 password hashing, SHA-1/1024-bit DSA signing, and RC2 in
PKCS#12 export. Password salting is inconsistent. Historical private-key and
container files are committed; treat them as public demonstration material and
do not reuse them. This application is not ready for production security use.

Source inspection also shows a `certificate`/`certificates` table-name mismatch,
JDK-internal imports, manual resource management, and exceptions that can hide
failures. The bundled `teacode-common.jar` is actively referenced by the UI.
These issues remain for later phases; documenting them does not fix them.

The modernization preserves Swing initially and separates build changes,
behavioral tests, security fixes, and code refactoring. See the plan for scope
and sequencing.

Phase 0 findings and reproducible diagnostic commands are recorded in the
[baseline report](docs/phase-0-baseline.md). Java 25 source compilation currently
fails; see the report for confirmed blockers and unverified flows.
