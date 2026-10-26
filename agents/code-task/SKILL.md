---
name: code-task
description: Implement a bounded task in the Crypto Java Swing/JDBC modernization project using a scoped coding subagent and appropriate verification.
---

# Code task

Use for a delegated implementation request, such as establishing the Maven build
or fixing a tested persistence defect. This workflow permits a coding subagent
for that task, not a separate user-visible chat or unrelated work.

## Coordinate

1. Read root `AGENTS.md`, `README.md`, the baseline report, and relevant modernization
   phases. Inspect current source/build files and Git status rather than assuming
   a phase has been completed.
2. Establish the feature branch under the root rules before dispatch. Preserve
   user edits and the index; leave work unstaged with no commits or pushes.
3. Give one coding agent the concrete outcome, acceptance criteria, permitted
   files, relevant docs, branch constraint, and available verification commands.
   Follow root model-selection guidance. Split work only when ownership is independent;
   workers do not change branches or spawn additional agents without assignment.
4. If delegation is unavailable, disclose it and execute the same scoped workflow.

## Implement

- Read the relevant code and instructions yourself. Stay within assigned scope;
  report ownership conflicts or necessary scope expansion to the coordinator.
- Trace UI actions in `src/com/zpayment/`, persistence in `src/database/`, and
  crypto in `Security.java`. Discover new locations if the Maven migration has
  moved them. Preserve existing workflows unless the task changes them.
- Keep build migration separate from functional/security refactoring. Retain
  required bundled libraries until replacement is verified; avoid unrelated
  dependency upgrades. Document any dependency introduced by the assigned task.
- Add meaningful regression tests for changed behavior where the test foundation
  exists. If building that foundation is required, make it explicit in scope.
  Do not claim historical-binary smoke results verify edited source.
- Use disposable keys/files/databases. Do not run the destructive SQL dump on a
  user's existing database. Coordinate shared build output with other workers.
- For database setup modernization, implement versioned migrations with a real
  runner and reproducible commands, not another standalone SQL dump. Include
  migration-backed integration tests and document existing-database adoption.
- Translate existing Russian application text where practical as related code is
  updated; the target is a fully English application. Preserve placeholders and
  behavior, and keep large translation changes independently reviewable.

## Integrate

Return files changed, behavior, commands and actual results, and unresolved risks.
The coordinator inspects the complete diff, resolves relevant issues, and performs
necessary integration checks. Report unsupported/environment-blocked checks
honestly. No automatic staging, commits, pushes, or PR creation.
