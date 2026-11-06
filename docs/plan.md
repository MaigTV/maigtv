# Project Plan: MaigTV

## Next

- [ ] Query installed Leanback & Android applications via `PackageManager`.
- [ ] Implement D-pad navigable TV grid layout with high-contrast focus rings.
- [ ] Add app launching execution with security fallback guards.
- [ ] Integrate Jetpack DataStore Preferences for pinned favorite applications.

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
