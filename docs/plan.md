# Project Plan: MaigTV

## Next (Living-Room OS Experience)

- [ ] Architectural Spike: Horizontal Workspace Coordinator & Pager with remote D-pad focus handover.
- [ ] Workspace 1 (Dashboard Hub): Living-room clock, quick-launch favorite bar, and media/content continuation cards.
- [ ] Workspace 2 (Glanceable Intelligence): Environmental and utility space (exploratory: multi-day weather forecast, AQI, ambient status).
- [ ] Workspace 3 (App Library): D-pad navigable TV card grid with high-contrast zoom/elevation focus rings (backed by `AppRepository`).
- [ ] Persistence: Jetpack DataStore Preferences for workspace order, pinned favorite apps, and hidden packages.

## Active

### OS-Esque Workspace Architecture Foundation

- **Status:** proposed
- **Release:** 0.3.0
- **Goal:** Transform the single-screen launcher UI into a modular, multi-workspace living-room experience inspired by window managers and virtual desktops, tailored for 10-foot remote D-pad ergonomics.
- **Requirements:**
  - Support distinct virtual spaces (Dashboard Hub, Glanceable Intelligence, App Library).
  - Provide instantaneous, zero-jank horizontal transitions across workspaces via remote left/right navigation.
  - Maintain focus restoration when paging between workspaces (returning to previous item).
  - Retain full testability and Compose previews for individual workspaces and the coordinator.
- **Approach:** Introduce a `WorkspaceCoordinator` / Pager in the UI layer with a typed `Workspace` enum/sealed hierarchy (`Dashboard`, `Intelligence`, `AppLibrary`). StateFlow tracks active workspace index and aggregated domain state.
- **Alternatives:**
  - *Single monolithic grid launcher:* Rejected as commoditized and lacking living-room utility.
  - *Vertical scrolling sponsored feeds:* Rejected to protect privacy, zero-ad purity, and snappy TV performance.
- **Sub-steps:**
  - [ ] Step 1: Record living-room OS workspace decision in `docs/decisions.md`.
  - [ ] Step 2: Define `Workspace` domain model and navigation state in `ui/main`.
  - [ ] Step 3: Implement `WorkspaceCoordinator` composable with TV focus management.
  - [ ] Step 4: Scaffold Workspace 1 (Dashboard Hub) with living-room clock & quick-launch row.
  - [ ] Step 5: Scaffold Workspace 2 (Glanceable Intelligence) placeholder for weather/AQI cards.
  - [ ] Step 6: Scaffold Workspace 3 (App Library) presenting `AppRepository` grid.
  - [ ] Step 7: Update unit tests and UI previews.
  - [ ] Step 8: Full gate verification (`./gradlew testDebugUnitTest assembleDebug`).
- **Docs:** `docs/architecture.md`, `README.md`, `docs/decisions.md`.
- **Links:** `docs/decisions.md#decision-evolution-from-basic-launcher-to-living-room-os-experience-workspaces-paradigm`, `docs/architecture.md#workspace-model`.

## Done

### Query and Launch Installed Applications via AppRepository

- **Status:** done
- **Release:** 0.2.0
- **Goal:** Provide a resilient, testable repository seam that queries installed Leanback and launcher applications via `PackageManager`, monitors package changes via `LauncherApps.Callback`, and securely launches selected apps.
- **Completed Steps:**
  - [x] Step 1: Declare package visibility `<queries>` in `AndroidManifest.xml`.
  - [x] Step 2: Implement `TvAppInfo`, `AppRepository`, `DefaultAppRepository`, and `FakeAppRepository` in `tv.maig.launcher.data`.
  - [x] Step 3: Update `MainScreenViewModel`, `MainScreenUiState`, and `MainScreen` to consume `AppRepository`.
  - [x] Step 4: Wire `DefaultAppRepository` at composition root (`MainActivity` & `Navigation`).
  - [x] Step 5: Update unit tests (`MainScreenViewModelTest`) and verify JVM test harness.
  - [x] Step 6: Synchronize `docs/architecture.md` and `README.md`.
  - [x] Step 7: Full gate verification (`./gradlew testDebugUnitTest assembleDebug`).

### Scaffold MaigTV Application Foundation

- **Status:** done
- **Release:** 0.1.0
- **Goal:** Build the complete, production-grade foundation for MaigTV: a fast, ad-free, native launcher for Android TV, Google TV, and Amazon Fire OS.
- **Completed Steps:**
  - [x] Step 1: Requirements intake and decision recording in `docs/decisions.md`.
  - [x] Step 2: Project template generated from official `android create empty-activity` CLI and committed intact (`commit 471ed33`).
  - [x] Step 3: Feature-first folder structure with clear package boundaries.
  - [x] Step 4: Constructor dependency injection at the composition root with `DataRepository`, `DefaultDataRepository`, and `FakeDataRepository`.
  - [x] Step 5: Repository-level `.editorconfig` formatting and GitHub Actions CI workflow (`.github/workflows/ci.yml`).
  - [x] Step 6: Type-safe Navigation 3 skeleton with `Main` and `Details` routes, backstack navigation, and deep link resolver (`NavigationRouteResolver`).
  - [x] Step 7: Test harness with unit tests (`MainScreenViewModelTest`, `NavigationRouteTest`) and UI test setup.
  - [x] Step 8: Accessibility baseline for TV D-pad navigation (explicit content descriptions, 48dp minimum touch/focus targets).
  - [x] Step 9: Comprehensive `README.md` and MIT `LICENSE`.
  - [x] Step 10: Living documentation synchronized across `docs/decisions.md`, `docs/architecture.md`, `docs/plan.md`, and `README.md`.
  - [x] Step 11: Agent workflow guidelines in `AGENTS.md`.
