<img src="images/featured_graphics.png" width="100%" alt="Seijaku List Featured Graphics">

# Seijaku List

A multiplatform application for discovering, tracking, and managing your anime and manga collection using the AniList API.

## Platforms

- Android (API 26+)
- iOS (iPhone & iPad)
- Desktop (Windows, macOS, Linux)

## Tech Stack

- **Kotlin Multiplatform** - Shared business logic
- **Compose Multiplatform** - Multiplatform UI
- **MVVM + Clean Architecture** - Architecture pattern
- **Apollo GraphQL** - AniList API integration
- **Room 3** - Local database
- **Koin** - Dependency injection
- **Voyager** - Multiplatform navigation

## Running the Project

### Android

```bash
./gradlew :androidApp:assembleDebug
```

### Desktop

```bash
# Hot reload
./gradlew :desktopApp:hotRun --auto

# Standard execution
./gradlew :desktopApp:run
```

### iOS

Open the `/iosApp` directory in Xcode and run the project from there.

## Tests

```bash
# Android
./gradlew :shared:testAndroidHostTest

# Desktop
./gradlew :shared:jvmTest

# iOS (requires macOS)
./gradlew :shared:iosSimulatorArm64Test
```

## Build Shared Module

```bash
./gradlew :shared:build
```

## Documentation

- [Project Wiki](../../wiki) - Detailed guides and documentation

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for more details.
