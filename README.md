<div align="center">

# MAIG
**Media & AI Guide for Android TV and Fire OS**

[![Platform](https://img.shields.io/badge/Platform-Android%20TV%20%7C%20Fire%20OS-3DDC84?style=flat-square&logo=android)](https://developer.android.com/tv)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1+-7F52FF?style=flat-square&logo=kotlin)](https://kotlinlang.org)
[![UI](https://img.shields.io/badge/Compose%20for%20TV-1.1.0-4285F4?style=flat-square&logo=jetpackcompose)](https://developer.android.com/jetpack/androidx/releases/tv)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)

*A fast, ad-free, native Kotlin launcher that replaces bloated stock TV home screens.*

</div>

---

## Why MAIG?

Stock TV launchers—especially on Fire OS—have turned into slow, ad-heavy billboards. Meanwhile, existing third-party launchers are strictly static icon grids that help you open apps, but don't help you figure out *what to watch*.

**MAIG** bridges the gap without sacrificing performance on low-RAM streaming sticks like the Fire TV Stick 4K Max:
- **Zero Cross-Platform Bloat:** Written in 100% native Kotlin using `androidx.tv` Compose and compiled with aggressive R8 shrinking.
- **Zero Sponsored Clutter:** Only your installed apps, your curated backdrops, and AI-powered recommendations when you ask for them.

---

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Serialization & Networking:** `kotlinx.serialization` (reflection-free compile-time codegen) + OkHttp
- **Image Pipeline:** Coil 3 (`coil-compose`) with 1080p hardware bitmap downsampling and disk caching
- **External APIs:**
  - [Fanart.tv API v3.2](https://fanart.tv/api-docs/) (Textless 1080p/4K backdrops & clearlogos)
  - [TMDb API](https://developer.themoviedb.org/) (ID resolution & streaming provider lookup)
  - [Open-Meteo API](https://open-meteo.com/) (Keyless local weather for ambient mode)
  - Pluggable OpenAI-compatible / Gemini / local `llama.cpp` LLM endpoint
