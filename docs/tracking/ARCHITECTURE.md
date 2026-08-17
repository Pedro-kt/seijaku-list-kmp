# Sistema de Tracking - Arquitectura

## Visión General

Sistema de tracking offline-first para anime y manga con capacidad de exportación a MyAnimeList XML y preparado para sincronización futura con AniList.

## Características Principales

- **Offline-first**: Funcionamiento completo sin conexión a internet
- **Local storage**: Room 3 KMP como base de datos multiplataforma
- **Estados AniList**: 6 estados compatibles con AniList (CURRENT, COMPLETED, PLANNING, DROPPED, PAUSED, REPEATING)
- **Tracking completo**: Progreso, score, fechas, notas personales
- **Export MAL**: Exportación a formato XML de MyAnimeList
- **Escalable**: Arquitectura preparada para sincronización AniList en el futuro

## Arquitectura (Clean Architecture)

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ AnimeList    │  │ MangaList    │  │ Detail       │      │
│  │ ViewModel    │  │ ViewModel    │  │ ViewModel    │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                            │
│  ┌──────────────────────────────────────────────────┐       │
│  │ Use Cases:                                        │       │
│  │ - AddToListUseCase                               │       │
│  │ - UpdateListEntryUseCase                         │       │
│  │ - GetMediaListUseCase                            │       │
│  │ - GetListStatsUseCase                            │       │
│  │ - ExportToMALUseCase                             │       │
│  │ - ImportFromMALUseCase                           │       │
│  │ - CheckInListUseCase                             │       │
│  └──────────────────────────────────────────────────┘       │
│                           ▲                                  │
│  ┌──────────────────────────────────────────────────┐       │
│  │ Repository Interface: MediaListRepository        │       │
│  └──────────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────────┘
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                             │
│  ┌──────────────────────────────────────────────────┐       │
│  │ MediaListRepositoryImpl                          │       │
│  └──────────────────────────────────────────────────┘       │
│           ▼                            ▼                     │
│  ┌──────────────┐            ┌──────────────┐              │
│  │ MediaListDao │            │ MALXmlMapper │              │
│  │ (Room 3 KMP) │            │              │              │
│  └──────────────┘            └──────────────┘              │
│           ▼                                                  │
│  ┌──────────────────────────────────────────────────┐       │
│  │ TrackingDatabase (SQLite)                        │       │
│  │ - MediaListEntryEntity                           │       │
│  └──────────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────────┘
```

## Modelos de Datos

### MediaListEntry (Domain)
```kotlin
data class MediaListEntry(
    val id: Long,
    val mediaId: Int,              // AniList ID
    val mediaType: MediaType,      // ANIME | MANGA
    val status: MediaListStatus,   // CURRENT, COMPLETED, etc.
    val progress: Int,             // episodios/capítulos vistos
    val progressVolumes: Int?,     // volúmenes leídos (manga)
    val repeatCount: Int,          // veces repetidas
    val score: Float?,             // 0.0 - 10.0
    val startDate: String?,        // YYYY-MM-DD
    val finishDate: String?,       // YYYY-MM-DD
    val notes: String?,            // notas personales
    val priority: MediaListPriority, // LOW, MEDIUM, HIGH
    val createdAt: Long,
    val updatedAt: Long,
    val mediaInfo: CachedMediaInfo? // info cacheada del media
)
```

### Estados (MediaListStatus)
- **CURRENT**: Watching/Reading
- **COMPLETED**: Terminado
- **PLANNING**: Plan to Watch/Read
- **DROPPED**: Abandonado
- **PAUSED**: En pausa (On-Hold)
- **REPEATING**: Rewatching/Rereading

## Flujos Principales

### 1. Agregar a Lista
```
User taps "Add to List" →
DetailViewModel.addToList() →
AddToListUseCase →
Repository.addToList() →
DAO.insertEntry() →
Database updated →
Flow emits change →
UI updates
```

### 2. Actualizar Progreso
```
User updates progress →
ViewModel.updateProgress() →
UpdateListEntryUseCase →
Repository.updateEntry() →
DAO.updateEntry() →
needsSync flag set →
Flow emits change →
UI updates
```

### 3. Ver Lista
```
AnimeListScreen loads →
ViewModel observes →
GetMediaListUseCase →
Repository.observeList() →
DAO.observeEntriesByType() →
Flow<List<MediaListEntry>> →
UI displays list
```

### 4. Exportar a MAL
```
User taps "Export" →
ViewModel.exportToMAL() →
ExportToMALUseCase →
Repository.exportToMAL() →
MALXmlMapper.generateMALXml() →
XML string generated →
File saved/shared
```

## Tecnologías

- **Database**: Room 3.0.1 KMP con Bundled SQLite 2.7.0
- **DI**: Koin 4.0.1
- **Coroutines**: kotlinx-coroutines 1.11.0
- **Serialization**: kotlinx-serialization 1.8.0
- **KSP**: 2.4.10-1.0.20 (para Room)

## Compatibilidad Multiplataforma

- ✅ **Android**: Room con SQLite nativo
- ✅ **iOS**: Room con Bundled SQLite
- ✅ **Desktop (JVM)**: Room con SQLite embebido

## Preparación para Sincronización AniList (Futuro)

### Campos de Sync en Database
- `anilistEntryId`: ID de la entrada en AniList
- `isSynced`: Flag de sincronización
- `needsSync`: Flag de cambios pendientes

### Estrategia de Sync
1. **Autenticación**: OAuth 2.0 con AniList
2. **Conflictos**: Last-write-wins basado en `updatedAt`
3. **Dirección**: Bidireccional (local ↔ AniList)
4. **Prioridad**: Server authority en conflictos

## Estructura de Archivos

```
features/tracking/
├── data/
│   ├── local/
│   │   ├── entity/MediaListEntryEntity.kt
│   │   ├── dao/MediaListDao.kt
│   │   └── TrackingDatabase.kt
│   ├── export/MALXmlMapper.kt
│   ├── mapper/MediaListMapper.kt
│   └── repository/MediaListRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   ├── MediaListEntry.kt
│   │   ├── MediaListStatus.kt
│   │   ├── MediaListPriority.kt
│   │   └── MediaListStats.kt
│   ├── repository/MediaListRepository.kt
│   └── usecase/
│       ├── AddToListUseCase.kt
│       ├── UpdateListEntryUseCase.kt
│       ├── GetMediaListUseCase.kt
│       ├── GetListStatsUseCase.kt
│       ├── ExportToMALUseCase.kt
│       ├── ImportFromMALUseCase.kt
│       └── CheckInListUseCase.kt
└── presentation/
    └── (ViewModels en features anime/manga)
```

## Referencias

- [DATABASE_SCHEMA.md](./DATABASE_SCHEMA.md) - Detalles del esquema de base de datos
- [MAL_XML_FORMAT.md](./MAL_XML_FORMAT.md) - Formato de exportación MyAnimeList
- [ANILIST_SYNC_FUTURE.md](./ANILIST_SYNC_FUTURE.md) - Roadmap de sincronización AniList
- [IMPLEMENTATION_CHECKLIST.md](./IMPLEMENTATION_CHECKLIST.md) - Checklist de implementación
