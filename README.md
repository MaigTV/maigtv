# MaigTV

A fast, ad-free, native launcher that replaces bloated stock TV home screens across Android TV, Google TV, and Amazon Fire OS devices.

MaigTV is designed from the ground up for instantaneous 10-foot TV experiences: zero sponsored ads, zero background telemetry bloat, and pure responsive D-pad remote navigation.

---

## Supported Platforms

| Platform | Supported Versions | Notes |
| :--- | :--- | :--- |
| **Android TV** | Android 9.0 (API 28) - Android 15 | Native Leanback launcher support |
| **Google TV** | Android 10+ (API 29+) | Fast home screen replacement |
| **Amazon Fire OS** | Fire OS 7 (API 28), Fire OS 8 (API 30+) | Fire TV Stick 4K / Max / Lite / Cube |

---

## Architecture & Layout

The project follows clean, feature-first mobile conventions with constructor injection at the composition root:

```
app/
├── src/main/
│   ├── AndroidManifest.xml          # TV launcher, LEANBACK_LAUNCHER, HOME intent filters, <queries>
│   └── kotlin/tv/maig/launcher/
│       ├── MainActivity.kt          # Composition root & deep-link route resolution
│       ├── Navigation.kt            # Navigation 3 NavDisplay & TV screen routing
│       ├── NavigationKeys.kt        # Typed navigation keys (Main, Settings)
│       ├── data/
│       │   └── AppRepository.kt     # Seam: TvAppInfo, AppRepository, DefaultAppRepository, FakeAppRepository
│       └── ui/
│           ├── main/
│           │   ├── MainScreen.kt          # Stateless TV launcher composable with focus semantics
│           │   └── MainScreenViewModel.kt # Unidirectional MVI StateFlow pipeline
│           └── theme/
│               ├── Color.kt         # TV color tokens
│               ├── Theme.kt         # Living-room dark color scheme & typography
│               └── Type.kt          # 10-foot scale typography
```

For full architectural contracts and module seams, see [docs/architecture.md](docs/architecture.md).

---

## Prerequisites

- **Java Development Kit (JDK)**: JDK 17 or higher
- **Android SDK**: `compileSdk = 37`, `minSdk = 28`
- **Build System**: Gradle 9.1 with Android Gradle Plugin 9.0.1

---

## Building & Verification

### Inner Loop
```bash
# Compile Kotlin sources
./gradlew :app:compileDebugKotlin

# Run unit tests
./gradlew :app:testDebugUnitTest
```

### Full Verification Gate
```bash
# Run unit tests and assemble debug APK
./gradlew testDebugUnitTest assembleDebug
```

---

## Running on TV Devices

1. Connect your Android TV, Google TV, or Fire TV via ADB:
   ```bash
   adb connect <TV_IP_ADDRESS>:5555
   ```
2. Install and launch the debug build:
   ```bash
   ./gradlew installDebug
   adb shell monkey -p tv.maig.launcher -c android.intent.category.LAUNCHER 1
   ```
3. (Optional) Set MaigTV as the default home launcher on Fire OS or Android TV:
   ```bash
   adb shell cmd package set-home-activity "tv.maig.launcher/.MainActivity"
   ```

---

## License

This project is licensed under the [MIT License](LICENSE).
