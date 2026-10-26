---
name: review-branch
description: Review Crypto Java modernization branch changes for security, correctness, maintainability, and test quality with specialist agents.
---

# Review branch

Perform a read-only review using [the project rubric](references/review-rubric.md).
Do not edit source, switch branches, install dependencies, or publish findings
externally unless separately instructed.

## Establish scope

Read root `AGENTS.md`, the README, relevant modernization phases, current build
configuration, and the baseline report. Resolve target (default HEAD) and base
(default local main, otherwise master). Prefer local refs, then matching origin
refs; disclose the selected refs and possible staleness. Honor explicit ranges.

Record status, target commit, merge base, and `git diff base...target` name/status
and patch. Review committed changes by default; include working-tree or staged
changes when requested and state that scope. Never claim an empty committed diff
covers uncommitted edits. Read callers and surrounding code beyond changed lines.

## Specialist analysis

This workflow authorizes bounded review delegation. Assign security, correctness,
quality, and test verification to specialist agents, running in batches when
concurrency is limited. Follow root model guidance; security-sensitive migration
analysis requires a suitably capable model. Give each agent the exact comparison,
changed paths, relevant requirements, rubric, and known environment limitations.
Only the test specialist runs shared verification commands to avoid duplicate runs.
If delegation is unavailable, disclose it and cover these aspects directly.

Require exact locations, triggering cases, evidence, impact, proposed severity,
and a practical correction for candidate findings. Separate pre-existing baseline
defects from regressions. A specialist with no findings should say so.

## Final review

Recheck candidates against code and requirements, merge duplicate causes, and
discard speculative or preference-only complaints. Report confirmed findings in
descending severity with precise file/line links. State the reviewed comparison,
commands/results, and unverified behavior. If none remain, say "No reportable
findings." A passing compile or historical smoke run does not prove crypto,
database, or interactive Swing correctness.
