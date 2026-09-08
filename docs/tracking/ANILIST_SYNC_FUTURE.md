# AniList Synchronization - Future Implementation

Roadmap y especificaciones para la futura integración de sincronización con AniList.

## Estado Actual

✅ **Implementado (Offline-first)**:
- Tracking local completo
- Estados compatibles con AniList
- Campos de sync en database preparados

❌ **Pendiente**:
- Autenticación OAuth con AniList
- Sincronización bidireccional
- Resolución de conflictos
- UI de gestión de cuenta

## Arquitectura de Sync

```
┌────────────────────────────────────────────────────────────┐
│                         User                                │
└────────────────────────────────────────────────────────────┘
                           ▼
┌────────────────────────────────────────────────────────────┐
│                    Sync UI Layer                            │
│  - Login Screen                                             │
│  - Sync Status Indicator                                    │
│  - Conflict Resolution Dialog                               │
└────────────────────────────────────────────────────────────┘
                           ▼
┌────────────────────────────────────────────────────────────┐
│                  Sync Manager                               │
│  - Trigger automatic/manual sync                            │
│  - Handle conflicts                                         │
│  - Queue failed operations                                  │
└────────────────────────────────────────────────────────────┘
                           ▼
┌─────────────────┬──────────────────┬───────────────────────┐
│  Local DB       │   Sync Service   │   AniList API         │
│  (Room)         │                  │   (GraphQL)           │
│  ↕              │        ↕         │          ↕            │
│  Needs Sync     │   Compare &      │    OAuth Token        │
│  Flags          │   Merge Data     │    MediaList Query    │
│                 │                  │    Mutations          │
└─────────────────┴──────────────────┴───────────────────────┘
```

## Fase 1: Autenticación OAuth 2.0

### 1.1 Flow de Autenticación

```
User taps "Login with AniList"
         ↓
Open browser with OAuth URL:
https://anilist.co/api/v2/oauth/authorize
  ?client_id={CLIENT_ID}
  &response_type=token
  &redirect_uri={REDIRECT_URI}
         ↓
User authorizes app
         ↓
Redirect to app with access_token
         ↓
Store token securely
         ↓
Fetch user info
         ↓
Enable sync features
```

### 1.2 Implementación

```kotlin
// Auth Service
class AniListAuthService(
    private val settings: Settings
) {
    companion object {
        private const val CLIENT_ID = "YOUR_CLIENT_ID"
        private const val REDIRECT_URI = "seijakulist://oauth/callback"
        private const val AUTH_URL = "https://anilist.co/api/v2/oauth/authorize"
        private const val TOKEN_KEY = "anilist_access_token"
        private const val USER_ID_KEY = "anilist_user_id"
    }

    fun getAuthUrl(): String {
        return "$AUTH_URL?client_id=$CLIENT_ID&response_type=token&redirect_uri=$REDIRECT_URI"
    }

    fun saveToken(token: String) {
        settings.putString(TOKEN_KEY, token)
    }

    fun getToken(): String? {
    return settings.getStringOrNull(TOKEN_KEY)
    }

    fun isAuthenticated(): Boolean {
        return getToken() != null
    }

    fun logout() {
        settings.remove(TOKEN_KEY)
        settings.remove(USER_ID_KEY)
    }

    suspend fun fetchUserInfo(): Result<AniListUser> {
        // GraphQL query to get current user
        return runCatching {
            // Implementation
        }
    }
}
```

### 1.3 Deep Link Configuration

**Android (AndroidManifest.xml)**:
```xml
<activity android:name=".OAuthCallbackActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="seijakulist"
            android:host="oauth"
            android:pathPrefix="/callback" />
    </intent-filter>
</activity>
```

**iOS (Info.plist)**:
```xml
<key>CFBundleURLTypes</key>
<array>
    <dict>
        <key>CFBundleURLSchemes</key>
        <array>
            <string>seijakulist</string>
        </array>
    </dict>
</array>
```

## Fase 2: GraphQL Queries & Mutations

### 2.1 Obtener Lista del Usuario

```graphql
query GetUserMediaList($userId: Int, $type: MediaType, $chunk: Int) {
  MediaListCollection(userId: $userId, type: $type, chunk: $chunk, perChunk: 500) {
    hasNextChunk
    lists {
      name
      status
      entries {
        id
        mediaId
        status
        progress
        progressVolumes
        score(format: POINT_10_DECIMAL)
        repeat
        private
        notes
        startedAt {
          year
          month
          day
        }
        completedAt {
          year
          month
          day
        }
        updatedAt
        createdAt
        media {
          id
          title {
            romaji
            english
            native
          }
          coverImage {
            large
            medium
          }
          episodes
          chapters
          volumes
          format
          status
        }
      }
    }
  }
}
```

### 2.2 Actualizar/Crear Entrada

```graphql
mutation SaveMediaListEntry(
  $mediaId: Int
  $status: MediaListStatus
  $score: Float
  $progress: Int
  $progressVolumes: Int
  $repeat: Int
  $notes: String
  $startedAt: FuzzyDateInput
  $completedAt: FuzzyDateInput
) {
  SaveMediaListEntry(
    mediaId: $mediaId
    status: $status
    scoreRaw: $score
    progress: $progress
    progressVolumes: $progressVolumes
    repeat: $repeat
    notes: $notes
    startedAt: $startedAt
    completedAt: $completedAt
  ) {
    id
    mediaId
    status
    score(format: POINT_10_DECIMAL)
    progress
    progressVolumes
    repeat
    notes
    startedAt {
      year
      month
      day
    }
    completedAt {
      year
      month
      day
    }
    updatedAt
  }
}
```

### 2.3 Eliminar Entrada

```graphql
mutation DeleteMediaListEntry($id: Int) {
  DeleteMediaListEntry(id: $id) {
    deleted
  }
}
```

## Fase 3: Sync Service

### 3.1 Sync Strategy

```kotlin
class AniListSyncService(
    private val repository: MediaListRepository,
    private val aniListApi: AniListApi,
    private val authService: AniListAuthService
) {
    sealed class SyncResult {
        data class Success(val syncedCount: Int, val conflictsResolved: Int) : SyncResult()
        data class Failure(val error: String) : SyncResult()
        data class Conflicts(val conflicts: List<SyncConflict>) : SyncResult()
    }

    suspend fun performFullSync(): SyncResult {
        if (!authService.isAuthenticated()) {
            return SyncResult.Failure("Not authenticated")
        }

        return try {
            // 1. Fetch remote data
            val remoteEntries = fetchAllRemoteEntries()

            // 2. Get local data
            val localEntries = repository.getAllEntries()

            // 3. Detect conflicts
            val conflicts = detectConflicts(localEntries, remoteEntries)

            if (conflicts.isNotEmpty()) {
                return SyncResult.Conflicts(conflicts)
            }

            // 4. Push local changes
            val pushResult = pushLocalChanges()

            // 5. Pull remote changes
            val pullResult = pullRemoteChanges(remoteEntries)

            // 6. Mark as synced
            repository.markAllAsSynced()

            SyncResult.Success(
                syncedCount = pushResult + pullResult,
                conflictsResolved = 0
            )
        } catch (e: Exception) {
            SyncResult.Failure(e.message ?: "Unknown error")
        }
    }

    private suspend fun fetchAllRemoteEntries(): List<AniListEntry> {
        val allEntries = mutableListOf<AniListEntry>()
        var chunk = 0
        var hasNextChunk = true

        while (hasNextChunk) {
            val response = aniListApi.getUserMediaList(
                userId = authService.getUserId(),
                chunk = chunk
            )

            response.lists.forEach { list ->
                allEntries.addAll(list.entries)
            }

            hasNextChunk = response.hasNextChunk
            chunk++
        }

        return allEntries
    }

    private suspend fun pushLocalChanges(): Int {
        val unsyncedEntries = repository.getUnsyncedEntries()
        var count = 0

        unsyncedEntries.forEach { entry ->
            val result = aniListApi.saveMediaListEntry(entry.toAniListMutation())

            if (result.isSuccess) {
                repository.updateSyncStatus(
                    entryId = entry.id,
                    anilistEntryId = result.getOrNull()?.id,
                    synced = true
                )
                count++
            }
        }

        return count
    }

    private suspend fun pullRemoteChanges(remoteEntries: List<AniListEntry>): Int {
        var count = 0

        remoteEntries.forEach { remote ->
            val local = repository.getEntryByAniListId(remote.id)

            if (local == null) {
                // New entry from remote
                repository.addFromRemote(remote)
                count++
            } else if (remote.updatedAt > local.updatedAt) {
                // Remote is newer
                repository.updateFromRemote(local.id, remote)
                count++
            }
        }

        return count
    }

    private fun detectConflicts(
        local: List<MediaListEntry>,
        remote: List<AniListEntry>
    ): List<SyncConflict> {
        val conflicts = mutableListOf<SyncConflict>()

        local.forEach { localEntry ->
            if (!localEntry.isSynced && localEntry.anilistEntryId != null) {
                val remoteEntry = remote.find { it.id == localEntry.anilistEntryId }

                if (remoteEntry != null &&
                    remoteEntry.updatedAt > localEntry.updatedAt &&
                    localEntry.needsSync
                ) {
                    // Both modified since last sync
                    conflicts.add(
                        SyncConflict(
                            localEntry = localEntry,
                            remoteEntry = remoteEntry,
                            type = ConflictType.BOTH_MODIFIED
                        )
                    )
                }
            }
        }

        return conflicts
    }
}
```

### 3.2 Conflict Resolution

```kotlin
data class SyncConflict(
    val localEntry: MediaListEntry,
    val remoteEntry: AniListEntry,
    val type: ConflictType
)

enum class ConflictType {
    BOTH_MODIFIED,
    REMOTE_DELETED,
    LOCAL_DELETED
}

enum class ConflictResolution {
    KEEP_LOCAL,      // Sobrescribir remoto con local
    KEEP_REMOTE,     // Sobrescribir local con remoto
    MERGE,           // Combinar datos (ej: mayor progreso)
    SKIP             // No sincronizar esta entrada
}

class ConflictResolver {
    fun resolveAutomatically(conflict: SyncConflict): ConflictResolution {
        // Estrategia automática: Last-write-wins
        return if (conflict.localEntry.updatedAt > conflict.remoteEntry.updatedAt) {
            ConflictResolution.KEEP_LOCAL
        } else {
            ConflictResolution.KEEP_REMOTE
        }
    }

    fun mergeProgress(conflict: SyncConflict): MediaListEntry {
        // Tomar el mayor progreso
        val maxProgress = maxOf(
            conflict.localEntry.progress,
            conflict.remoteEntry.progress
        )

        // Tomar el score local si existe, sino remoto
        val score = conflict.localEntry.score ?: conflict.remoteEntry.score

        // Combinar notas
        val notes = listOfNotNull(
            conflict.localEntry.notes,
            conflict.remoteEntry.notes
        ).joinToString("\n---\n")

        return conflict.localEntry.copy(
            progress = maxProgress,
            score = score,
            notes = notes.ifEmpty { null },
            updatedAt = System.currentTimeMillis()
        )
    }
}
```

## Fase 4: UI de Sync

### 4.1 Login Screen

```kotlin
@Composable
fun AniListLoginScreen(
    viewModel: AniListLoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Connect with AniList")

        Spacer(Modifier.height(16.dp))

        Button(onClick = { viewModel.loginWithAniList() }) {
            Text("Login with AniList")
        }

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.error?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
}
```

### 4.2 Sync Status Indicator

```kotlin
@Composable
fun SyncStatusBanner(
    syncState: SyncState
) {
    when (syncState) {
        is SyncState.Syncing -> {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        is SyncState.Error -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.Error, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Sync failed: ${syncState.message}")
            }
        }
        is SyncState.Success -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Synced ${syncState.itemCount} items")
            }
        }
        else -> { /* No banner */ }
    }
}
```

### 4.3 Conflict Resolution Dialog

```kotlin
@Composable
fun ConflictResolutionDialog(
    conflict: SyncConflict,
    onResolve: (ConflictResolution) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sync Conflict") },
        text = {
            Column {
                Text("This entry was modified both locally and on AniList.")

                Spacer(Modifier.height(16.dp))

                Text("Local: ${conflict.localEntry.status}, Progress: ${conflict.localEntry.progress}")
                Text("Remote: ${conflict.remoteEntry.status}, Progress: ${conflict.remoteEntry.progress}")
            }
        },
        confirmButton = {
            TextButton(onClick = { onResolve(ConflictResolution.KEEP_LOCAL) }) {
                Text("Keep Local")
            }
        },
        dismissButton = {
            TextButton(onClick = { onResolve(ConflictResolution.KEEP_REMOTE) }) {
                Text("Keep Remote")
            }
        }
    )
}
```

## Fase 5: Automatic Sync

### 5.1 Sync Triggers

```kotlin
class SyncManager(
    private val syncService: AniListSyncService,
    private val settings: Settings
) {
    companion object {
        private const val AUTO_SYNC_KEY = "auto_sync_enabled"
        private const val SYNC_INTERVAL_KEY = "sync_interval_minutes"
        private const val LAST_SYNC_KEY = "last_sync_timestamp"
    }

    fun isAutoSyncEnabled(): Boolean {
        return settings.getBoolean(AUTO_SYNC_KEY, defaultValue = true)
    }

    fun getSyncInterval(): Long {
        return settings.getLong(SYNC_INTERVAL_KEY, defaultValue = 30)
    }

    suspend fun syncIfNeeded() {
        if (!isAutoSyncEnabled()) return

        val lastSync = settings.getLong(LAST_SYNC_KEY, defaultValue = 0)
        val now = System.currentTimeMillis()
        val interval = getSyncInterval() * 60 * 1000

        if (now - lastSync >= interval) {
            performSync()
        }
    }

    private suspend fun performSync() {
        val result = syncService.performFullSync()

        if (result is AniListSyncService.SyncResult.Success) {
            settings.putLong(LAST_SYNC_KEY, System.currentTimeMillis())
        }
    }

    // Call on app startup
    fun schedulePeriodicSync() {
        // Platform-specific background work implementation
    }

    // Call when user makes changes
    fun markNeedsSync() {
        // Flag to sync on next opportunity
    }
}
```

### 5.2 Background Sync (Platform-specific)

**Android**: WorkManager
**iOS**: Background App Refresh
**Desktop**: Periodic coroutine

## Checklist de Implementación

### Autenticación
- [ ] Configurar OAuth en AniList Developer Settings
- [ ] Implementar AniListAuthService
- [ ] Configurar deep links (Android/iOS/Desktop)
- [ ] UI de login
- [ ] Secure token storage
- [ ] Logout functionality

### GraphQL Integration
- [ ] Definir queries en .graphql files
- [ ] Query: GetUserMediaList
- [ ] Mutation: SaveMediaListEntry
- [ ] Mutation: DeleteMediaListEntry
- [ ] Implementar AniListApi con Apollo Client
- [ ] Mappers: AniList ↔ Domain

### Sync Logic
- [ ] SyncService implementation
- [ ] Conflict detection
- [ ] Conflict resolution (automatic)
- [ ] Conflict resolution (manual)
- [ ] Push local changes
- [ ] Pull remote changes
- [ ] Update sync flags

### UI
- [ ] Login screen
- [ ] Sync settings
- [ ] Sync status indicators
- [ ] Conflict resolution dialogs
- [ ] Sync history/logs
- [ ] Account management

### Testing
- [ ] Unit tests para SyncService
- [ ] Integration tests
- [ ] Edge cases (offline, conflicts, etc.)
- [ ] Performance testing (large lists)

### Polish
- [ ] Error handling
- [ ] Retry logic
- [ ] Offline queue
- [ ] User notifications
- [ ] Documentation

## Referencias

- [AniList API Documentation](https://anilist.gitbook.io/anilist-apiv2-docs/)
- [AniList OAuth Documentation](https://anilist.gitbook.io/anilist-apiv2-docs/overview/oauth)
- [Apollo Android OAuth Example](https://www.apollographql.com/docs/kotlin/advanced/authentication)
