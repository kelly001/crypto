# Crypto review rubric

Apply this rubric to changed behavior and its dependencies. The modernization
plan is intent; the baseline documents existing defects. Report issues introduced
or materially worsened by the reviewed change, not every known legacy weakness.

## Security

- Trace login/password hashing, salts, credentials, SQL parameters, file paths,
  key storage, logs, and export passwords through changed code.
- Check signing keys against claimed issuers, certificate chain verification,
  CA constraints/key usage, signature algorithms, key sizes, and validity.
- Check public X.509 versus private PKCS#8 encoding, PKCS#12 protection and password
  failures, provider initialization, and compatibility/migration of stored data.
- Look for leaked secrets, accidental committed test keys, unsafe output paths,
  and destructive database initialization. Ground findings in a concrete path.

## Logic and correctness

- Trace Swing event handlers through persistence and crypto, including cancel,
  empty/invalid input, null results, and error reporting. Check event-dispatch
  thread behavior and blocking work when affected.
- Verify company versus employee session/certificate paths, issuer ownership,
  table names against the schema, lookup IDs, CRUD, and revocation effects.
- Check resource ownership, transaction consistency, file/DB partial failures,
  date/time conversions and leap-year/expiry boundaries.
- Check packaging entry points, resources/classpaths, source filename casing,
  and required custom Swing helpers during build/dependency migration.

## Quality

- Keep infrastructure, behavior, security, and style changes distinguishable.
  Preserve Swing/plain JDBC scope unless the task explicitly changes it.
- Prefer clear resource lifecycles and propagated/contextual errors; flag swallowed
  failures and misleading success reports. Avoid speculative abstractions.
- Review UTF-8/English conventions for new text, accurate setup documentation,
  and ignored build/config files. Incremental translation of existing Russian
  text is welcome; check placeholders and behavior while moving toward a fully
  English application.

## Tests and verification

- Inspect the actual build before choosing commands. Run supported targeted
  tests/build checks; record commands, results, and the revision tested. If the
  checkout differs from the review target, disclose the mismatch.
- Test certificate properties and signatures, key round trips, PKCS#12 loading,
  wrong passwords, malformed/missing files, and meaningful database failure paths.
- Use fresh disposable databases and generated temporary keys. Never execute
  destructive schema initialization against an unverified endpoint.
- When migrations are introduced or changed, verify fresh initialization, repeat
  execution, upgrade/data preservation, and migration history validation. Tests
  must exercise the real migration runner rather than a separate schema copy.
- Distinguish source tests from historical binaries, headless diagnostics from UI
  tests, and mocks from real database integration. Measure coverage only if useful;
  do not invent numbers or treat no exception as sufficient assertion.

## Evidence and severity

Every finding needs a precise location, realistic trigger, concrete impact, and
practical correction. Confirm surrounding mitigations. Use official documentation
matching dependency versions for version-sensitive claims.

- Critical: exploitable severe security failure or major data loss.
- Important: realistic broken behavior or substantial security/reliability risk.
- Mid: bounded defect or meaningful maintainability/testability cost.
- Low: local clarity or coverage improvement without demonstrated immediate harm.

Missing tests alone default to Low unless evidence justifies greater severity.
Report existing out-of-scope risks separately only when they materially affect
the review; do not present them as new findings.
