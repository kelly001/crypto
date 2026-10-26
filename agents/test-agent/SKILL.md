---
name: test-agent
description: Add or update focused Java tests for Crypto certificate, key, PKCS#12, JDBC, and Swing behavior, accounting for the project's current build phase.
---

# Test agent

Use for a bounded testing request. Read root `AGENTS.md`, the README, baseline,
relevant modernization phase, implemented code, and current test/build setup.
Do not treat planned APIs or behavior as already implemented.

## Delegate and scope

The coordinator establishes the feature branch and assigns one testing subagent
the behavior, acceptance criteria, permitted files, and verification commands.
Follow root model-selection guidance; use a suitably capable model for crypto
semantics. Reuse an available worker where practical. No further delegation
without assignment. If unavailable, disclose it and perform the task directly.

Testing workers leave changes unstaged and do not change branches, commit, push,
or create PRs. The coordinator may commit and push verified development-branch
changes under root instructions.
Keep production changes limited to assigned fixes or necessary testability seams;
report unrelated defects rather than silently changing expected behavior.

## Choose the appropriate foundation

The initial repository has no automated suite. Standalone `Test` classes and
`docs/baseline/LegacySmoke.java` are experiments/diagnostics. If Maven/JUnit are
still absent, distinguish a baseline probe from a regression test; introduce
test infrastructure only as part of the assigned scope or report the prerequisite.

After setup, prefer JUnit 5, optionally AssertJ, and Testcontainers for MySQL
integration as described in the plan. Discover actual paths/versions/commands.
Use deterministic clocks/dates, temporary directories, generated disposable keys,
and meaningful assertions. Do not compare randomized crypto output byte-for-byte
or mock away the crypto operation being tested.

## Behavioral priorities

- Certificates: subjects/issuers, root versus user signing, signatures and chain
  relationships, validity, basic constraints, and key usage.
- Keys: public/private serialization and reload, malformed input, missing files,
  and provider initialization. Distinguish X.509 public and PKCS#8 private formats.
- PKCS#12: loading exported containers, key/certificate presence and association,
  correct/incorrect passwords, and password-dialog cancellation where testable.
- Authentication: success/failure and salt/hash handling for the implemented
  scheme; clearly separate legacy characterization from secure target behavior.
- JDBC: disposable-schema CRUD, lookup, revocation, invalid IDs, connection
  failures, and `certificate`/`certificates` naming. Once migrations are available,
  initialize test databases with the real migration runner and test fresh setup,
  repeat execution, and upgrades preserving representative existing data. Until
  then, verify the disposable endpoint before running destructive legacy SQL;
  never assume localhost is expendable.
- Swing: meaningful event/state behavior on the event-dispatch thread. State when
  a graphical environment is required; headless startup failure is not a UI test.

Keep tests small and focused on observable behavior. Add regression coverage
before fixing known defects, and label legacy characterization so insecure
behavior is not mistaken for the desired permanent contract.

## Verify and report

Run targeted tests first, then broader checks when shared setup or integration
requires them. Use `mvn test` / `mvn clean verify` only once supported by the
repository. Coordinate build outputs with other workers. Return changed files,
commands/results, covered behavior, and blocked checks. Do not hide failures by
weakening assertions or claim tests passed when prerequisites were unavailable.
