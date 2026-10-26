# Project agent instructions

## Context and sources

Read `README.md`, `docs/phase-0-baseline.md`, and the relevant phase of
`docs/crypto_java25_modernization_plan.md` before changing the project. Inspect
current code and build configuration as well: the plan describes intended work,
and the baseline records historical observations, not current verification.

Crypto is a legacy Swing/JDBC application for companies, employees, certificates,
keys, and PKCS#12 containers. Java 25 LTS is the modernization target. At the time
these instructions were added, there was no Maven/Gradle build, automated test
suite, or CI. Do not claim planned infrastructure or configuration already works.

## Working conventions

- Work on a task-specific feature branch. Before edits, inspect status, branch,
  and staged/unstaged changes. From `main` or `master`, create
  `codex/<short-task-name>`; reuse a suitable existing feature branch. Honor an
  explicitly requested branch. Only the coordinating agent changes branches,
  before workers start.
- Preserve user edits and staging decisions. Do not reset, clean, stash, overwrite
  unrelated work, or rewrite history to obtain a clean checkout. Leave changes
  unstaged unless asked otherwise; commits, pushes, merges, and PR creation require
  the user's instruction.
- Use English by default for new comments, documentation, messages, and UI text.
  Translate existing Russian UI and application text into English where practical,
  including when touching related code. The final goal is a fully English UI and
  application. Preserve behavior, formatting placeholders, and stored data while
  translating; keep larger translation changes reviewable. Save text as UTF-8.
- Keep changes focused and reviewable. Separate build/dependency migration,
  behavioral fixes, security changes, and style/architecture refactoring.
- Update relevant documentation when setup, behavior, or verification changes.
  Report changed behavior, checks actually run, and remaining limitations.

## Delegation and models

When delegation is authorized and useful, assign bounded tasks with explicit file
ownership, acceptance criteria, and verification. Prefer lower-cost capable models:
`gpt-5.6-luna` for straightforward edits/tests and `gpt-5.6-terra` for moderate
implementation. If unavailable, use a suitable available model and report the
fallback. Use a more capable model for cryptography, authentication design,
cross-module migration, or unresolved correctness risks. Do not delegate merely
to create activity; the coordinating agent owns integration and final checks.
Workers must not change branches or edit overlapping files concurrently.

Project agent workflows, adapted from the backup examples, are maintained as
skill-format instructions:

- [Code task](agents/code-task/SKILL.md): scoped implementation and verification.
- [Review branch](agents/review-branch/SKILL.md): evidence-based specialist review.
- [Test agent](agents/test-agent/SKILL.md): focused Java and integration tests.

Read the relevant workflow when asked to use it. These are repository-local
instructions; their presence alone does not register custom runtime agents.
Keep reusable instructions in version control; temporary agent reports and scratch
output belong outside tracked source and must not contain secrets.

## Modernization boundaries

- Preserve Swing initially and keep plain JDBC. Do not introduce React, Spring
  Boot, JPA, microservices, or Java modules without a task-specific reason.
- Establish a reproducible build and meaningful behavior tests before large
  refactoring or changing cryptographic defaults. Follow the plan's sequencing.
- Do not delete bundled JARs before replacements are verified.
  `teacode-common.jar` is actively used by the UI; inspect its consumers first.
- Prefer try-with-resources for JDBC and files, explicit error handling, and
  `java.time` at domain boundaries. Use records for immutable inputs/DTOs rather
  than mechanically converting mutable entities. Use modern syntax for clarity.
- Check `.gitignore` when introducing build/config files: its legacy `*.xml` and
  `*.properties` rules can hide required files. Historical `out/` and `.class`
  files are not a reproducible source build; do not overwrite them during checks.
- Account for the tracked `RemoveCertificateActionListener.java` /
  `removeCertificateActionListener.java` case collision on Windows. Handle any
  correction deliberately rather than silently losing one version.

## Security and data

- Treat committed keys and containers as public demonstration material. Never
  reuse them for real credentials or commit new private keys, passwords, or secrets.
  Generate disposable test material and avoid logging sensitive values.
- Do not extend MD5, SHA-1/DSA-1024, or RC2 as modern defaults. Separate legacy
  characterization tests from intended secure behavior and test migrations.
- For crypto changes, verify certificate issuer/signature relationships, validity,
  constraints, key usage, key serialization round trips, and PKCS#12 loading.
  Parsing/verification matters more than byte-for-byte equality of random output.
- Use only disposable databases for experiments. `crypto_db.sql` drops `crypto`
  and contains old grants; never import it into an existing valuable database.
  Legacy connection settings are hardcoded; environment variables are planned.
- Replace dump-based setup with executable, versioned database migrations as part
  of modernization. Use a maintained migration tool such as Flyway or Liquibase,
  integrated with the Maven build and documented setup. Treat `crypto_db.sql` as
  historical schema evidence, not the future installation procedure. Verify fresh
  initialization, repeat execution, and upgrades preserving existing data; never
  edit an already-applied migration. Keep database/user provisioning and secrets
  separate from application schema migrations.
- Revocation currently changes database state and attempts local file deletion;
  do not describe it as a CRL/OCSP service.

## Verification

Choose checks from the actual revision and changed behavior. Before Maven exists,
use the baseline report's source-compilation diagnostics in an isolated output
directory. Known baseline blockers include the case collision and a JDK-internal
import. `docs/baseline/LegacySmoke.java` probes historical binaries, not rebuilt
source; standalone `Test` classes are not a regression suite.

Once Maven and tests exist, use relevant targeted tests and `mvn test`; run
`mvn clean verify` for integrated build changes when supported. The planned test
stack is JUnit 5, optionally AssertJ, and Testcontainers for MySQL integration.
For documentation-only changes, check links, consistency, and whitespace rather
than running unrelated application diagnostics. Distinguish source builds,
historical-binary probes, headless checks, and interactive UI verification.
