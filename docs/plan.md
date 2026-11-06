# Project Plan: MaigTV

## Next

- [ ] Implement D-pad navigable TV grid layout with high-contrast focus rings.
- [ ] Integrate Jetpack DataStore Preferences for pinned favorite applications.

## Active

### Query and Launch Installed Applications via AppRepository

- **Status:** done
- **Release:** 0.2.0
- **Goal:** Provide a resilient, testable repository seam that queries installed Leanback and launcher applications via `PackageManager`, monitors package changes via `LauncherApps.Callback`, and securely launches selected apps.
- **Requirements:**
  - Query native TV applications matching `CATEGORY_LEANBACK_LAUNCHER`.
  - Query sideloaded applications matching `CATEGORY_LAUNCHER` without duplicates.
  - Exclude MaigTV launcher itself from the returned list.
  - Observe real-time install, uninstall, and update events via `LauncherApps.Callback` in a reactive `callbackFlow`.
  - Provide safe app launching prioritizing Leanback TV entry point before standard launcher intent.
  - Retain full testability with `FakeAppRepository` running on JVM without framework mocks.
- **Approach:** Introduce `TvAppInfo` model and `AppRepository` interface with `DefaultAppRepository` (production) and `FakeAppRepository` (tests/previews). Migrate `MainActivity`, `Navigation`, `MainScreenViewModel`, and `MainScreen` to consume `AppRepository`.
- **Alternatives:**
  - *Only `LauncherApps.getActivityList()`*: Rejected because it omits pure Android TV applications declaring only `CATEGORY_LEANBACK_LAUNCHER`.
  - *Only `PackageManager` with BroadcastReceivers*: Rejected because broadcast receivers for package changes are clunky, not profile-aware, and subject to background execution limits.
- **Sub-steps:**
  - [x] Step 1: Declare package visibility `<queries>` in `AndroidManifest.xml`.
  - [x] Step 2: Implement `TvAppInfo`, `AppRepository`, `DefaultAppRepository`, and `FakeAppRepository` in `tv.maig.launcher.data`.
  - [x] Step 3: Update `MainScreenViewModel`, `MainScreenUiState`, and `MainScreen` to consume `AppRepository`.
  - [x] Step 4: Wire `DefaultAppRepository` at composition root (`MainActivity` & `Navigation`).
  - [x] Step 5: Update unit tests (`MainScreenViewModelTest`) and verify JVM test harness.
  - [x] Step 6: Synchronize `docs/architecture.md` and `README.md`.
  - [x] Step 7: Full gate verification (`./gradlew testDebugUnitTest assembleDebug`).
- **Acceptance:**
  - `AppRepository` interface has production `DefaultAppRepository` and testing `FakeAppRepository`.
  - Unit tests run and pass without framework errors on JVM.
  - Full gate `./gradlew testDebugUnitTest assembleDebug` succeeds.
- **Docs:** `docs/architecture.md`, `README.md`, `docs/decisions.md`.
- **Links:** `docs/decisions.md#decision-hybrid-appdiscovery-packagemanager-query-launcherappscallback`, `docs/architecture.md#seams-dependency-direction`.

## Done

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
