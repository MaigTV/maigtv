# Agent Instructions: MaigTV

Welcome! When working on the MaigTV codebase, follow these rules:

1. **Read the project documentation before modifying code**:
   - [`docs/decisions.md`](docs/decisions.md) — The rationale behind project choices and rejected alternatives.
   - [`docs/architecture.md`](docs/architecture.md) — Component seams, layer boundaries, and dependency directions.
   - [`docs/plan.md`](docs/plan.md) — Current release status and active feature checklists.
   - [`README.md`](README.md) — Public facing setup, build commands, and platform targets.

2. **Follow Mobile Conventions**:
   - Invoke `mobile-conventions` before planning, implementing, or reviewing code.
   - Adhere to the Android Kotlin + Compose conventions.
   - Keep the seam between UI and Data clean: UI observes `StateFlow`; repositories sit behind interfaces; dependencies are injected at the composition root (`MainActivity`).

3. **Plan First**:
   - Start every feature, bug fix, or refactor with an explicit entry in [`docs/plan.md`](docs/plan.md).
   - Get user alignment before making broad structural changes.

4. **Synchronize Documents**:
   - Update [`docs/architecture.md`](docs/architecture.md) and [`README.md`](README.md) in the same commit whenever dependencies, commands, or architectural shapes change.
   - Treat any discrepancy between code and documentation as an incomplete change.

5. **Verify Before Committing**:
   - **Inner loop**: `./gradlew :app:compileDebugKotlin` and `./gradlew :app:testDebugUnitTest`.
   - **Full gate**: `./gradlew testDebugUnitTest assembleDebug`.
   - Commit often in small, atomic commits (under ±200 lines where practical).
