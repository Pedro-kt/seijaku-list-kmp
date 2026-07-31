# Seijaku List KMP

Aplicación multiplataforma para descubrir, seguir y gestionar tu colección de anime y manga usando la API de AniList.

## Plataformas

- Android (API 26+)
- iOS (iPhone & iPad)
- Desktop (Windows, macOS, Linux)

## Stack Tecnológico

- **Kotlin Multiplatform** - Lógica compartida
- **Compose Multiplatform** - UI multiplataforma
- **MVVM + Clean Architecture** - Patrón de arquitectura 
- **Apollo GraphQL** - Integración con AniList API
- **Room 3** - Base de datos local
- **Koin** - Inyección de dependencias
- **Voyager** - Navegación multiplataforma

## Ejecutar el Proyecto

### Android
```bash
./gradlew :androidApp:assembleDebug
```

### Desktop
```bash
# Hot reload
./gradlew :desktopApp:hotRun --auto

# Ejecución estándar
./gradlew :desktopApp:run
```

### iOS
Abre el directorio `/iosApp` en Xcode y ejecuta desde ahí.

## Tests

```bash
# Android
./gradlew :shared:testAndroidHostTest

# Desktop
./gradlew :shared:jvmTest

# iOS (requiere macOS)
./gradlew :shared:iosSimulatorArm64Test
```

## Compilar Shared Module

```bash
./gradlew :shared:build
```

## Documentación

- [Wiki del Proyecto](../../wiki) - Guías detalladas y documentación

## Licencia

Este proyecto está bajo la licencia Apache 2.0 - ver el archivo [LICENSE](LICENSE) para más detalles.