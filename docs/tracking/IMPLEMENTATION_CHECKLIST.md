# Implementation Checklist - Tracking System

Checklist completo para la implementación del sistema de tracking offline-first.

## ✅ Fase 1: Documentación

- [x] ARCHITECTURE.md - Arquitectura general del sistema
- [x] DATABASE_SCHEMA.md - Esquema de Room database
- [x] MAL_XML_FORMAT.md - Formato de exportación MyAnimeList
- [x] ANILIST_SYNC_FUTURE.md - Roadmap para integración AniList
- [x] IMPLEMENTATION_CHECKLIST.md - Este checklist

## 📦 Fase 2: Database Layer (Room 3 KMP) ✅

### Entity
- [x] `MediaListEntryEntity.kt` - Entidad con todos los campos
  - [x] Campos de identificación (id, mediaId, mediaType)
  - [x] Campos de tracking (status, progress, score, etc.)
  - [x] Campos de sync (anilistEntryId, isSynced, needsSync)
  - [x] Campos de cache (mediaTitle, mediaCoverImage, etc.)
  - [x] Indices únicos y de búsqueda
  - [x] Anotaciones Room correctas

### DAO
- [x] `MediaListDao.kt` - Data Access Object
  - [x] **CREATE**: insertEntry, insertEntries
  - [x] **READ**: getEntryByMedia, getEntryById
  - [x] **READ (Flows)**: observeEntryByMedia, observeEntriesByType
  - [x] **READ (Filters)**: observeEntriesByTypeAndStatus
  - [x] **READ (Search)**: searchEntries
  - [x] **UPDATE**: updateEntry, updateProgress, updateStatus, updateScore
  - [x] **DELETE**: deleteEntry, deleteByMedia, deleteAll
  - [x] **STATS**: getCountByStatus, getStatsByType
  - [x] **SYNC**: getUnsyncedEntries, markAsSynced

### Database
- [x] `TrackingDatabase.kt` (commonMain)
  - [x] @Database annotation con entity
  - [x] Abstract function para mediaListDao()
  - [x] Companion con DATABASE_NAME constant
  - [x] Expect object TrackingDatabaseBuilder

- [x] `TrackingDatabase.android.kt` (androidMain)
  - [x] Actual TrackingDatabaseBuilder implementation
  - [x] Context setup
  - [x] Database path configuration

- [x] `TrackingDatabase.ios.kt` (iosMain)
  - [x] Actual TrackingDatabaseBuilder implementation
  - [x] NSHomeDirectory setup
  - [x] Database path configuration

- [x] `TrackingDatabase.jvm.kt` (jvmMain)
  - [x] Actual TrackingDatabaseBuilder implementation
  - [x] User home directory setup
  - [x] Database path configuration

## 🎯 Fase 3: Domain Layer ✅

### Models
- [x] `MediaListEntry.kt` - Domain model principal
  - [x] Data class con todos los campos
  - [x] Propiedad mediaInfo con CachedMediaInfo

- [x] `MediaListStatus.kt` - Enum de estados
  - [x] 6 estados: CURRENT, COMPLETED, PLANNING, DROPPED, PAUSED, REPEATING
  - [x] Function toMALStatus(mediaType)
  - [x] Companion function fromMALStatus(malStatus)

- [x] `MediaListPriority.kt` - Enum de prioridades
  - [x] LOW (0), MEDIUM (1), HIGH (2)
  - [x] Companion function fromValue(value)

- [x] `MediaListStats.kt` - Data class de estadísticas
  - [x] Contadores por estado
  - [x] totalProgress
  - [x] averageScore

- [x] `CachedMediaInfo.kt` - Info cacheada del media
  - [x] title, coverImage
  - [x] totalEpisodes, totalChapters, totalVolumes

- [x] `MediaListSortOption.kt` - Enum de opciones de ordenamiento
  - [x] TITLE, SCORE, PROGRESS, UPDATED_AT

### Repository Interface
- [x] `MediaListRepository.kt` - Interface del repository
  - [x] addToList()
  - [x] updateEntry()
  - [x] removeFromList()
  - [x] getEntry()
  - [x] observeEntry()
  - [x] observeList()
  - [x] observeFilteredList()
  - [x] searchList()
  - [x] observeStats()
  - [x] importFromMAL()
  - [x] exportToMAL()
  - [x] syncWithAniList() (stub para futuro)
  - [x] getUnsyncedEntries()

### Use Cases
- [x] `AddToListUseCase.kt`
  - [x] operator fun invoke()
  - [x] Validación de parámetros
  - [x] Call repository.addToList()

- [x] `UpdateListEntryUseCase.kt`
  - [x] operator fun invoke() con parámetros opcionales
  - [x] Call repository.updateEntry()

- [x] `RemoveFromListUseCase.kt`
  - [x] operator fun invoke()
  - [x] Call repository.removeFromList()

- [x] `GetMediaListUseCase.kt`
  - [x] operator fun invoke() retornando Flow
  - [x] Parámetros: mediaType, status opcional

- [x] `GetListStatsUseCase.kt`
  - [x] operator fun invoke() retornando Flow<MediaListStats>
  - [x] Call repository.observeStats()

- [x] `ExportToMALUseCase.kt`
  - [x] operator fun invoke() retornando Result<String>
  - [x] Call repository.exportToMAL()

- [x] `ImportFromMALUseCase.kt`
  - [x] operator fun invoke() con xmlContent
  - [x] Call repository.importFromMAL()

- [x] `CheckInListUseCase.kt`
  - [x] operator fun invoke() suspend retornando Boolean
  - [x] operator fun observe() retornando Flow<Boolean>

## 💾 Fase 4: Data Layer ✅

### Mappers
- [x] `MediaListMapper.kt`
  - [x] Extension fun MediaListEntryEntity.toDomain()
  - [x] Extension fun MediaListEntry.toEntity()
  - [x] Conversiones de enums
  - [x] Conversiones de nullable fields

### Repository Implementation
- [x] `MediaListRepositoryImpl.kt`
  - [x] Constructor con mediaListDao, malXmlMapper
  - [x] Implementar addToList()
    - [x] Crear entity con timestamp actual
    - [x] Set needsSync = true
    - [x] Insert en DAO
    - [x] Retornar Result
  - [x] Implementar updateEntry()
    - [x] Get existing entry
    - [x] Copy con nuevos valores
    - [x] Set needsSync = true
    - [x] Update timestamp
    - [x] Update en DAO
  - [x] Implementar removeFromList()
  - [x] Implementar getEntry()
  - [x] Implementar observeEntry() con Flow
  - [x] Implementar observeList()
  - [x] Implementar observeFilteredList()
  - [x] Implementar searchList()
  - [x] Implementar observeStats()
    - [x] Observar lista completa
    - [x] Map a MediaListStats con contadores
  - [x] Implementar importFromMAL()
    - [x] Parse XML con malXmlMapper
    - [x] Insert batch en DAO
  - [x] Implementar exportToMAL()
    - [x] Get all entries por tipo
    - [x] Generate XML con malXmlMapper
  - [x] Stub syncWithAniList() con NotImplementedError
  - [x] Implementar getUnsyncedEntries()

### XML Export/Import
- [x] `MALXmlMapper.kt`
  - [x] Function parseMALXml()
    - [x] Parse XML string (TODO placeholder para futuro)
    - [x] Extract myinfo section
    - [x] Extract entries (anime o manga)
    - [x] Map a MediaListEntryEntity
    - [x] Retornar List<MediaListEntryEntity>
  - [x] Function generateMALXml()
    - [x] Build XML string con buildString {}
    - [x] Add XML declaration
    - [x] Add myinfo section con estadísticas
    - [x] Loop entries y generar XML
    - [x] Use CDATA para strings
    - [x] Convert status con toMALStatus()
    - [x] Convert timestamps (millis → seconds)
    - [x] Retornar String
  - [x] Private function calculateStats()

## 🎨 Fase 5: Presentation Layer

### ViewModels

#### AnimeListViewModel
- [ ] `AnimeListViewModel.kt`
  - [ ] Dependencies: GetMediaListUseCase, GetListStatsUseCase, UpdateListEntryUseCase, ExportToMALUseCase
  - [ ] _uiState MutableStateFlow
  - [ ] uiState StateFlow expuesto
  - [ ] init block: loadAnimeList(), loadStats()
  - [ ] Function loadAnimeList()
  - [ ] Function loadStats()
  - [ ] Function selectStatus(status)
  - [ ] Function exportToMAL()
  - [ ] Function searchList(query)
  - [ ] Function sortBy(option, ascending)
  - [ ] AnimeListUiState data class
    - [ ] entries: List<MediaListEntry>
    - [ ] selectedStatus: MediaListStatus?
    - [ ] stats: MediaListStats?
    - [ ] isLoading: Boolean
    - [ ] isExporting: Boolean
    - [ ] exportSuccess: Boolean
    - [ ] error: String?

#### MangaListViewModel
- [ ] `MangaListViewModel.kt` (similar a AnimeListViewModel)
  - [ ] Misma estructura pero con MediaType.MANGA
  - [ ] MangaListUiState data class

#### DetailViewModel Updates
- [ ] Actualizar `DetailViewModel.kt`
  - [ ] Add dependencies: AddToListUseCase, UpdateListEntryUseCase, CheckInListUseCase, RemoveFromListUseCase
  - [ ] Add observeListStatus() function
  - [ ] Add addToList(status) function
  - [ ] Add updateListEntry() function
  - [ ] Add removeFromList() function
  - [ ] Update UiState con isInList, listEntry
  - [ ] Observe entry changes

### UI Screens

#### AnimeListScreen
- [ ] `AnimeListScreen.kt`
  - [ ] Screen composable con ViewModel
  - [ ] Collect uiState
  - [ ] AppBar con title, search icon, export action
  - [ ] Status filter chips (CURRENT, COMPLETED, etc.)
  - [ ] LazyColumn con entries
  - [ ] AnimeListItem composable
    - [ ] Cover image
    - [ ] Title
    - [ ] Progress indicator
    - [ ] Score
    - [ ] Status badge
  - [ ] Loading indicator
  - [ ] Empty state
  - [ ] Error state
  - [ ] Pull-to-refresh
  - [ ] Click para navegar a detail

#### MangaListScreen
- [ ] `MangaListScreen.kt`
  - [ ] Similar a AnimeListScreen
  - [ ] MangaListItem composable
    - [ ] Progress: chapters + volumes

#### AddToListBottomSheet Updates
- [ ] Actualizar `AddToListBottomSheet.kt`
  - [ ] Replace ListStatus enum con MediaListStatus
  - [ ] Wire up a AddToListUseCase
  - [ ] Add progress slider
  - [ ] Add score input
  - [ ] Add date pickers (start/finish)
  - [ ] Add notes text field
  - [ ] Add priority selector
  - [ ] onConfirm callback con todos los campos
  - [ ] Loading state mientras guarda

#### EditListEntryDialog
- [ ] `EditListEntryDialog.kt` (nuevo)
  - [ ] Full edit dialog para entry existente
  - [ ] All fields editable
  - [ ] Save button
  - [ ] Delete button
  - [ ] Cancel button

### Profile Screen Updates
- [ ] Actualizar `ProfileScreen.kt`
  - [ ] Add statistics section
    - [ ] Anime stats (total, by status)
    - [ ] Manga stats (total, by status)
  - [ ] Add export section
    - [ ] Export Anime to MAL XML
    - [ ] Export Manga to MAL XML
  - [ ] Add import section (futuro)
  - [ ] Add sync section (futuro)
    - [ ] Login with AniList button (disabled con "Coming soon")

## 🔧 Fase 6: Dependency Injection (Koin) ✅

- [x] `TrackingModule.kt`
  - [x] Database providers
    - [x] single { TrackingDatabaseBuilder.create().build() }
    - [x] single { get<TrackingDatabase>().mediaListDao() }
  - [x] Data layer
    - [x] singleOf(::MALXmlMapper)
    - [x] singleOf(::MediaListRepositoryImpl) bind MediaListRepository::class
  - [x] Domain layer - Use Cases
    - [x] factoryOf(::AddToListUseCase)
    - [x] factoryOf(::UpdateListEntryUseCase)
    - [x] factoryOf(::RemoveFromListUseCase)
    - [x] factoryOf(::GetMediaListUseCase)
    - [x] factoryOf(::GetListStatsUseCase)
    - [x] factoryOf(::ExportToMALUseCase)
    - [x] factoryOf(::ImportFromMALUseCase)
    - [x] factoryOf(::CheckInListUseCase)
  - [ ] Presentation layer - ViewModels (Pendiente)
    - [ ] viewModelOf(::AnimeListViewModel)
    - [ ] viewModelOf(::MangaListViewModel)

- [x] Actualizar `KoinInitializer.kt`
  - [x] Add trackingModule to modules list

- [x] Actualizar `MainActivity.kt` (Android)
  - [x] Initialize TrackingDatabaseBuilder with context

## ✅ Fase 7: Integration & Testing

### Manual Testing
- [ ] Database operations
  - [ ] Add entry to list
  - [ ] Update entry fields
  - [ ] Remove entry from list
  - [ ] Query entries by status
  - [ ] Search entries
  - [ ] Get statistics

- [ ] UI flows
  - [ ] Navigate to AnimeListScreen
  - [ ] Filter by status
  - [ ] Search entries
  - [ ] Navigate to detail
  - [ ] Add from detail screen
  - [ ] Edit entry
  - [ ] Remove entry
  - [ ] Export to XML
  - [ ] Verify XML format

- [ ] Multiplatform
  - [ ] Test on Android
  - [ ] Test on iOS (if available)
  - [ ] Test on Desktop

### Unit Tests (Optional pero recomendado)
- [ ] MediaListRepositoryImplTest
  - [ ] Test addToList
  - [ ] Test updateEntry
  - [ ] Test removeFromList
  - [ ] Test queries

- [ ] MALXmlMapperTest
  - [ ] Test generateMALXml
  - [ ] Test parseMALXml
  - [ ] Test status mapping

- [ ] Use Cases Tests
  - [ ] Test cada use case básico

## 🚀 Fase 8: Polish & Documentation

### Error Handling
- [ ] Repository error handling
- [ ] ViewModel error states
- [ ] UI error messages
- [ ] Validation errors

### Performance
- [ ] Database query optimization
- [ ] Flow collection optimization
- [ ] LazyColumn performance
- [ ] Image loading optimization

### UX Improvements
- [ ] Loading states
- [ ] Empty states
- [ ] Success feedback
- [ ] Animations
- [ ] Haptic feedback

### Code Quality
- [ ] Code review
- [ ] Remove TODOs
- [ ] Add KDoc comments
- [ ] Format code
- [ ] Remove debug logs

### Documentation Updates
- [ ] Update README.md con tracking features
- [ ] Add screenshots
- [ ] Update changelog

## 📋 Pre-Release Checklist

- [ ] All features working on Android
- [ ] All features working on iOS (if applicable)
- [ ] All features working on Desktop (if applicable)
- [ ] No crashes or critical bugs
- [ ] Database migrations tested
- [ ] XML export/import tested
- [ ] Performance acceptable (lists con 1000+ entries)
- [ ] Code committed to git
- [ ] Documentation complete

## 🔮 Future Enhancements (Post-MVP)

- [ ] AniList OAuth integration
- [ ] AniList sync functionality
- [ ] Import from MAL XML
- [ ] Backup/Restore functionality
- [ ] Advanced filters (genre, year, score range)
- [ ] Sorting options
- [ ] Batch operations
- [ ] Statistics visualizations (charts, graphs)
- [ ] Custom lists/tags
- [ ] Activity history log

---

## Notas de Implementación

### Orden Recomendado
1. ✅ Documentación (Fase 1)
2. → Database Layer (Fase 2) - **SIGUIENTE**
3. → Domain Layer (Fase 3)
4. → Data Layer (Fase 4)
5. → Presentation Layer (Fase 5)
6. → DI (Fase 6)
7. → Testing (Fase 7)
8. → Polish (Fase 8)

### Estimación de Tiempo
- Fase 2 (Database): 2-3 horas
- Fase 3 (Domain): 2-3 horas
- Fase 4 (Data): 3-4 horas
- Fase 5 (Presentation): 5-6 horas
- Fase 6 (DI): 30 minutos
- Fase 7 (Testing): 2-3 horas
- Fase 8 (Polish): 2-3 horas

**Total estimado: 17-23 horas**

### Tips
- Compilar frecuentemente para detectar errores temprano
- Testear cada capa antes de pasar a la siguiente
- Usar git commits pequeños y frecuentes
- Mantener la documentación actualizada
- No optimizar prematuramente
