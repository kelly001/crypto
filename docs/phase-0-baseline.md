# Phase 0 — Establish the Baseline

Recorded on 2026-09-28 on Windows, before application refactoring.

## Snapshot and scope

Original application revision: `1d7750c` (`dialogs fxs`). Documentation revision
`437cc43e0c6c08f4e0c769a4b07801996f08a09a` was committed and pushed first.
A clean local clone of that revision was created using:

```powershell
git clone --no-hardlinks D:\CodingProjects\crypto "$env:TEMP\crypto-baseline-<unique-id>"
```

The actual clone was `C:\Users\Kelly\AppData\Local\Temp\crypto-baseline-4982eee267e14dabbfff5ba644caaecf`.
`git status --porcelain` was empty immediately after cloning. Git warned that
`RemoveCertificateActionListener.java` and `removeCertificateActionListener.java`
collide on the case-insensitive filesystem. Both paths are tracked; only one can
be materialized normally on Windows. This is part of the baseline, not fixed here.

All compilation output and runtime experiments were confined to the disposable
clone. No application sources, dependencies, schema, or committed keys were
changed. No database schema was imported and no account/certificate SQL was run.

## Repository inventory

| Location | Current role |
| --- | --- |
| `src/com/zpayment/` | Swing UI, login helper, certificate and key implementation |
| `src/database/` | JDBC connection and SQL embedded in model classes |
| `src/external/` | Link button and URL helper |
| `src/Test.java`, `src/com/zpayment/Test.java` | Manual experiments, not a test suite |
| `lib/` | Four application dependency JARs listed below |
| `out/production/crypto/` | Historical compiled classes; sampled `MainFrame.class` is major version 52 (Java 8) |
| `out/artifacts/crypto/`, `out/artifacts/crypto_jar3/` | Historical application JAR plus copies of dependency JARs |
| `META-INF/MANIFEST.MF` | Points to `LoginFrame`, with a Java-source path as Class-Path |
| `src/META-INF/MANIFEST.MF` | Points to `MainFrame`, with bundled dependency filenames |
| `files/` | Historical certificates, private/public keys and PKCS#12 containers |
| `crypto_db.sql` | MySQL 5.5.25-era dump: `user` and singular `certificate` tables; no seed INSERTs |
| `docs/` | Existing Russian help HTML/images; now also plan and baseline records |
| Root miscellaneous files | `RootKey`, `key.p12`, `data`, icons, and two RAR archives |

No Maven/Gradle build or CI is present. `.gitignore` ignores `.out/` and `.files/`
instead of the actual `out/` and `files/`, and broadly ignores `*.xml` and
`*.properties`. This will need attention when introducing Maven/configuration.

## Dependencies

Versions below come from the checked-in filenames (no upgrade or dependency
resolution was performed). Hashes identify the exact baseline inputs.

| File | Role | SHA-256 |
| --- | --- | --- |
| `lib/bcprov-jdk15on-151.jar` | Bouncy Castle 1.51 provider | `8748F0EC73895F7F18C1A9C13CF754FDDDDF0451CF472463EF02F93C3E7A7DE7` |
| `lib/bcpkix-jdk15on-151.jar` | Bouncy Castle 1.51 PKIX/PEM/PKCS#12 | `3D87C38E4885FACE4E4846E10865291218156C3E7C2B81D2425E6A95614D04FA` |
| `lib/mysql-connector-java-5.1.34-bin.jar` | JDBC driver | `AF1E5F28BE112C85EC52A82D94E7A8DC02EDE57A182DC2F1545F7CEC5E808142` |
| `lib/teacode-common.jar` | Custom Swing helpers; version/provenance unestablished | `61B352397C16A6C5D2E587167845BDEA60D374F5C2F531B687335BA1871760C8` |

`teacode-common.jar` is required by the existing UI. `jar tf` confirmed
`FieldPanel`, `CloseButtonDialog`, and `OkCancelDialog`. References occur in
`CertificateDialog`, `CertificatePanel`, `CompanyDialog`, `CompanyDialog2`,
`CompanyPanel`, `LoginDialog`, `LoginFrame`, `MainFrame`, `MainPanel`,
`SaveCompanyAction`, `UserPanel`, `UserPanel2`, and `UsersViewDialog`.
It cannot simply be dropped as an unused dependency.

## Entry point and application flows

The intended entry point is `com.zpayment.MainFrame.main`. It constructs a frame
and displays `LoginDialog` first; command-line arguments do not bypass that
constructor. The alternative root manifest is inconsistent with this entry point.

| Flow | Source path and behavior | Baseline status |
| --- | --- | --- |
| Login | `LoginDialog.AuthActionListener` → `Login.authenticate` → `User.loadByEmail`; compares salted MD5 | Driver loads, but DB connection fails here; successful login unverified |
| Company/user management | `MainFrame` menus → company/user dialogs and panels → `Company`, `Employer`, `User` SQL | Inspected only; CRUD requires working database and interactive UI |
| Certificate creation | `CertificateDialog` saves metadata via `CertificatePanel`; companies call `generateRootCertificate`, employees call map-based `generateUserCertificate` | Low-level certificate generation works with explicit provider setup; full dialog/file/DB flow unverified |
| Key generation | `Security.GenKeys`: BC DSA 1024 with SUN SHA1PRNG | Returns null without provider registration; works after explicit BC registration |
| Revocation | `RemoveCertificateActionListener.actionPerformed`/`CertificateDialog.removeCertificate` → `Certificate.cancel`, then local file deletion | Inspected only; main menu constructs listener without invoking its action, so the menu path appears ineffective |
| PKCS#12 export | `Security.transformToPKS12`: Swing password prompt, RC2 certificate bag encryption, key bag, output under `files/` | Not exercised interactively; no successful container round trip claimed |
| MySQL persistence | `Database.getConnection`, `User`, `Company`, `Employer`, `Certificate` | Connection attempt to configured endpoint fails with `CommunicationsException`; schema/CRUD unverified |

## Checks and outcomes

Runtime: Temurin OpenJDK `25.0.4.1+1-LTS`, obtained through
`JAVA_HOME=C:\Users\Kelly\AppData\Local\Programs\Java\jdk-25.0.4.1+1`.
`java`, `javac`, `mvn`, `mysql`, and `docker` were not found on PATH. No listening
MySQL endpoint was found on local port 3306. Java 8 was not used or verified.

### Unmodified source compilation: failed

Run from the clone, with output outside the historical `out/` tree:

```powershell
New-Item -ItemType Directory -Force baseline-build
$sources = @(Get-ChildItem src -Filter *.java -Recurse | ForEach-Object FullName)
& "$env:JAVA_HOME\bin\javac.exe" -encoding UTF-8 -cp 'lib/*' -d baseline-build $sources
```

Two compiler errors were observed:

- `removeCertificateActionListener.java`: public class must be declared in
  `RemoveCertificateActionListener.java` (case-colliding tracked paths).
- `MainFrame.java:3`: `com.sun.org.apache.xpath.internal` is not exported by
  module `java.xml` to the unnamed module.

Deprecated-API notes were also emitted. No fixes, export overrides, or dependency
upgrades were applied to obtain this result.

### Historical binary startup: limited check

```powershell
& "$env:JAVA_HOME\bin\javap.exe" -verbose out/production/crypto/com/zpayment/MainFrame.class
& "$env:JAVA_HOME\bin\java.exe" '-Djava.awt.headless=true' -cp 'out/production/crypto;lib/*' com.zpayment.MainFrame
```

`javap` reported major version 52. Startup reached the JFrame constructor and
raised `HeadlessException`, as expected with headless mode. This confirms only
entry-point loading; it does not prove that the login dialog or full UI works.
Interactive UI behavior remains unverified.

### Historical binary diagnostics: mixed results

The accompanying [LegacySmoke.java](baseline/LegacySmoke.java) invokes historical
compiled classes rather than compiling application source. Copy it into the
clean clone, then run:

```powershell
& "$env:JAVA_HOME\bin\javac.exe" -cp 'out/production/crypto;lib/*' -d baseline-build LegacySmoke.java
& "$env:JAVA_HOME\bin\java.exe" '-Djava.awt.headless=true' -cp 'baseline-build;out/production/crypto;lib/*' com.zpayment.LegacySmoke
```

Observed results:

1. BC initially unregistered; legacy key generator logs `NoSuchProviderException`
   and returns null. Source contains no `addProvider` call; a comment assumes
   provider registration through Java security configuration.
2. Explicit diagnostic-only BC registration allows DSA key generation.
3. Low-level root certificate generation and signature verification succeed.
4. Map-based user certificate generation and verification with the user's own
   key succeed. This is not proof of a valid company-issued certificate chain.
5. Reloading a freshly generated PKCS#8 private key through `readPrivateKey`
   logs `key spec not recognised` and returns null. The temporary key is deleted.
6. `com.mysql.jdbc.Driver` loads, but a connection with two-second connect/socket
   timeouts returns `CommunicationsException`. No SQL is executed.

These checks are baseline probes, not the Phase 3 regression suite. Historical
binaries may differ from source, and success here does not imply source builds.

## Findings to preserve for later phases

- `Certificate.loadById` queries plural `certificates`; supplied schema creates
  singular `certificate`.
- `MainFrame.setUser` calls `Company.loadByEmail` again; employee session behavior
  needs an integration/UI test.
- The map-based user certificate method signs with the user's private key while
  naming a company issuer. CA-chain semantics need tests before crypto changes.
- `readPrivateKey` uses `X509EncodedKeySpec` instead of `PKCS8EncodedKeySpec`.
- Cancelling the PKCS#12 password prompt displays a default-password message but
  leaves `password` null before `toCharArray`; exceptions are caught and logged.
- MD5, fixed/incorrectly serialized salts, SHA-1/DSA-1024, RC2, hardcoded database
  credentials, swallowed exceptions, and committed key material remain unchanged.
- The SQL dump drops the database without `IF EXISTS`, has old grant syntax, and
  contains no initial application account; do not describe import/login as verified.

Phase 0 inventory and available-environment verification are complete. Successful
login, company/user CRUD, persisted certificates, revocation, interactive PKCS#12
export, schema import, and a Java 8 source build remain explicitly unverified.
The next implementation work is Phase 1; no later modernization phase was done.
