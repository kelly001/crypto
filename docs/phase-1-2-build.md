# Phase 1 and 2 — Java 25 build and dependencies

## Implementation plan

1. Adopt Maven's `src/main/java`, `src/main/resources`, and `src/test/java`
   layout. Consolidate the identical case-colliding certificate listener sources
   and remove the unused JDK-internal import that blocks Java 25 compilation.
2. Pin a Maven wrapper, Java 25 compiler/enforcer configuration, build plugins,
   and dependencies. Package the application with its runtime dependencies and
   `com.zpayment.MainFrame` as the entry point; load the icon from the classpath.
3. Replace Bouncy Castle 1.51 with the current `jdk18on` provider/PKIX artifacts
   and Connector/J 5.1.34 with `com.mysql:mysql-connector-j`. Adapt obsolete API
   references and rely on JDBC service discovery.
4. Replace the three used custom Swing helpers (`FieldPanel`, `CloseButtonDialog`,
   `OkCancelDialog`) with small project-owned Swing implementations. Preserve
   layout modes, modal dialogs, button callbacks, and Escape/OK behavior.
5. Verify source compilation, dependency resolution, packaging, and focused
   compatibility tests before deleting old JARs and compiled output. Update setup
   documentation and record actual results here.

## Scope

Preserve existing algorithms and business behavior in this migration. The full
Phase 3 regression suite, versioned database migrations, database configuration,
and Phase 6 cryptographic fixes remain subsequent work. Tests introduced here
cover the dependency/build seams changed by these two phases. Historical keys
are not used by new checks. Do not connect to or initialize an existing database.

## Dependency decisions

Versions are pinned after checking released artifacts in Maven Central:

| Component | Version |
| --- | --- |
| Java build/runtime | 25 |
| Maven / wrapper | 3.10.0 / 3.3.4 (only-script, SHA-256 verified) |
| Bouncy Castle provider / PKIX / utility | 1.86 (`jdk18on`) |
| MySQL Connector/J | 26.10.0 |
| JUnit Jupiter | 5.14.4 |

Release references: [Maven downloads](https://maven.apache.org/download.cgi),
[BC published versions](https://repo.maven.apache.org/maven2/org/bouncycastle/bcprov-jdk18on/maven-metadata.xml),
and [Connector/J release notes](https://dev.mysql.com/doc/relnotes/connector-j/en/news-26-10-0.html).
Build-plugin versions are pinned in `pom.xml`; prerelease Maven 4 plugins are not used.

The
custom helper JAR is required by the old source but has no established provenance
or supported Maven coordinate in this repository. Replacing its narrow used API
with standard Swing avoids `systemPath`, a private binary repository, or manual
local JAR installation. New helper text is English.

The two tracked listener paths have identical Git contents; only the correctly
cased `RemoveCertificateActionListener.java` is retained in the new source tree.

## Verification

Completed on 2026-10-27 on Windows with Temurin JDK `25.0.4.1+1-LTS`.

| Check | Outcome |
| --- | --- |
| `mvnw.cmd -B -ntp clean compile` | Passed; 30 production source files, release 25 |
| `mvnw.cmd -B -ntp clean verify dependency:tree` | Passed; five headless checks passed, three desktop-only checks skipped |
| `mvnw.cmd -B -ntp -o -Djava.awt.headless=false -Dtest=DialogCompatibilityTest test` | Three desktop dialog tests passed |
| Fresh wrapper cache and empty Maven repository, `clean verify dependency:tree` with `-Djava.awt.headless=false` | Passed after removing `lib/` and `out/`; eight tests passed, no skips |
| Packaged JAR diagnostic below | Passed using only the application JAR and its manifest-resolved dependencies |
| Source inventory | All 28 old Java paths accounted for; identical case collision consolidated into one source |
| `git diff --check` and ignore checks | Passed; POM/wrapper properties/resources visible, build output ignored |

To repeat the clean-cache verification from PowerShell, with JDK 25 selected:

```powershell
$env:MAVEN_USER_HOME = Join-Path $env:TEMP ('crypto-wrapper-' + [guid]::NewGuid())
$repository = Join-Path $env:TEMP ('crypto-repository-' + [guid]::NewGuid())
.\mvnw.cmd -B -ntp "-Dmaven.repo.local=$repository" '-Djava.awt.headless=false' clean verify dependency:tree
```

Omit the headless override when no graphical desktop is available. The wrapper
cache and artifact repository can be removed after verification. Maven's wrapper
handles the Maven distribution independently of the dependency repository.

To verify the actual packaged dependency classpath without a database or UI:

```powershell
& "$env:JAVA_HOME\bin\java.exe" --class-path target\crypto-1.0-SNAPSHOT.jar docs\verification\PackageSmoke.java
```

The diagnostic checks the icon, application and helper classes, BC initialization,
and JDBC discovery. It confirms their code sources are JARs under `target/`.

## Result and limitations

Production code now uses `JcaPEMWriter` and `Extension` constants; unused obsolete
BC imports were removed. BC registers on first use of the application's `Security`
class, so no global JDK security-file editing is needed. JDBC uses service discovery.
The new Swing helpers retain stacked/side-by-side layouts, labels/tooltips, modal
dialog contracts, and OK/cancel/Escape behavior. Their default button text is English.

Historical `out/`, all checked-in dependency JARs, the stray source-tree `.class`,
and conflicting manifests were removed after replacement verification. Maven
generates the sole manifest with the intended entry point and relative runtime
classpath. Icons are packaged resources. Runtime dependency JARs remain separate
to preserve BC signatures. Build/test output is confined to ignored `target/`.

Deprecation warnings in untouched legacy code remain. This phase does not establish
live MySQL compatibility, successful login/CRUD, a valid company-issued certificate
chain, interactive PKCS#12 export, or full visual equivalence of every application
screen. No database connection or schema import was performed. The focused tests
preserve legacy algorithms and expose only the compatibility boundary changed
here; obsolete cryptography and previously recorded behavior defects remain.

Next: Phase 3's broader safety net, including actual versioned schema migrations
and migration-backed database tests, before security or domain refactoring.

## Java cleanup follow-up — 2026-10-28

Reviewed application, test, and diagnostic Java sources. Removed commented-out
implementations, generated author/date notes, redundant imports, unused fields
and locals, uncalled helper/model methods, the two standalone `Test` experiments,
the unused alternate company/user panels, and the unused browser utility/key model.
Swing callbacks, both existing application launchers, test entry points, and the
historical baseline diagnostic's private-key reader remain.

Unused result holders were removed without dropping certificate generation or
dialog construction. Removed the discarded duplicate query in `User.loadUsers`
and the unused controls-map read after company creation. The unused
`Certificate.loadById` plural-table query was removed, not repaired as a live API.

The application now compiles 24 production sources. All eight compatibility tests
and the packaged-JAR diagnostic pass. Eclipse compiler checks for unused imports,
locals, private members, arguments, and dead code are clean for application and
test sources. The historical diagnostic keeps its connection resource variable
to guarantee closing the connection; that compiler warning is intentional.
Analysis tooling remains local under ignored `.build-tools/` and adds no project
dependency. No live database behavior was tested during this cleanup.
