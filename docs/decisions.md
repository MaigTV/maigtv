# Project Decisions: MaigTV

## Request
The user requested a fast, ad-free, native launcher that replaces bloated stock TV home screens, running across Android TV, Google TV, and Amazon Fire OS devices. The goal is an instantaneous, clean, distraction-free 10-foot experience focused purely on launching user applications with seamless D-pad remote navigation.

## Scope
Scaffold. A complete, production-grade foundation featuring the official project baseline, feature-first structure, dependency injection, typed navigation, unit/UI test harnesses, accessibility compliance for TV D-pad navigation, formatting, linting, documentation, and agent guidelines.

## Agreed Choices

- **Stack**: Android Native with Kotlin and Jetpack Compose for TV (`androidx.tv:tv-material`, `androidx.tv:tv-foundation`).
- **UI Strategy**: TV-first 10-foot UI designed specifically for remote D-pad navigation, focus highlights, high-contrast visibility, and proper TV overscan safe margins.
- **Build System**: Gradle with Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`) and Version Catalog (`gradle/libs.versions.toml`).
- **App Name**: MaigTV
- **Application ID / Namespace**: `tv.maig.launcher`
- **Organization**: Maig TV (`tv.maig`)
- **Target Platforms**: Android TV, Google TV, and Amazon Fire OS (Fire TV Sticks, Fire TV Cubes, smart TVs).
- **Minimum OS Version**: Android 9.0 Pie (API 28) — provides 100% compatibility with Fire OS 7+, Fire OS 8, Android TV 9+, and Google TV.
- **Target / Compile SDK**: Android 14/15 (API 34/35).
- **State Management**: `ViewModel` + `StateFlow` with unidirectional data flow (immutable UI state, intents down via methods, lifecycle-aware collection via `collectAsStateWithLifecycle`).
- **Navigation**: Type-safe Navigation Compose for TV / explicit routes.
- **Dependency Injection**: Constructor injection at the edge; composition root in `MainActivity` with interfaces and fakes for testing.
- **Persistence**: Jetpack DataStore Preferences for user favorites, custom sorting, and layout settings.
- **Networking**: None (completely offline, zero ads, zero tracking, maximum privacy and speed).
- **Lint Level**: Standard (Android Lint + `ktlint` with repository-level `.editorconfig`).
- **License**: MIT License.
- **Agent Instructions**: Yes, `AGENTS.md` at root pointing to conventions and project documentation.

## README Outline
1. **Overview**: Purpose, ad-free philosophy, and performance goals.
2. **Platform Support**: Android TV, Google TV, Fire OS compatibility matrix.
3. **Architecture & Project Layout**: Feature-first module layout and data flow.
4. **Development & Toolchain**: JDK requirements, Android SDK setup, and Gradle wrapper.
5. **Building & Running**: Commands for assembling APK, running on device/emulator, setting as default TV launcher.
6. **Testing & Quality Checks**: Unit tests, UI tests, linting, and formatting commands.
7. **License**: MIT terms.

## Architecture Sketch
MaigTV follows a single-activity architecture where `MainActivity` is declared in `AndroidManifest.xml` with intent filters for `android.intent.category.HOME`, `android.intent.category.DEFAULT`, and `android.intent.category.LEANBACK_LAUNCHER`. The composition root constructs platform dependencies (`PackageManager`-backed `AppRepository`, `DataStore`-backed `PreferencesRepository`) and injects them into `HomeViewModel`. 

The UI layer is built with Jetpack Compose for TV, organizing installed apps into an accessible, D-pad navigable grid with smooth focus transitions, app banner/icon rendering, and instant launch actions. Layer boundaries strictly enforce that UI composables observe immutable `UiState`, domain interfaces remain free of Android UI framework dependencies, and repository implementations wrap system calls and package queries with failure-tolerant error handling.

For complete architectural details, see [docs/architecture.md](architecture.md).
