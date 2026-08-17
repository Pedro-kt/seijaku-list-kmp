# Database Schema - Room 3 KMP

## Tabla: media_list_entries

Almacena las entradas de tracking de anime y manga del usuario.

### Schema SQL

```sql
CREATE TABLE media_list_entries (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,

    -- Media Reference
    media_id INTEGER NOT NULL,
    media_type TEXT NOT NULL,

    -- Tracking Status
    status TEXT NOT NULL,

    -- Progress
    progress INTEGER NOT NULL DEFAULT 0,
    progress_volumes INTEGER,
    repeat_count INTEGER NOT NULL DEFAULT 0,

    -- User Rating
    score REAL,

    -- Dates (ISO 8601: YYYY-MM-DD)
    start_date TEXT,
    finish_date TEXT,

    -- Notes
    notes TEXT,

    -- Priority
    priority INTEGER NOT NULL DEFAULT 1,

    -- Metadata
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL,

    -- Sync Fields (for future AniList sync)
    anilist_entry_id INTEGER,
    is_synced INTEGER NOT NULL DEFAULT 0,
    needs_sync INTEGER NOT NULL DEFAULT 0,

    -- Cached Media Info (denormalized for performance)
    media_title TEXT,
    media_cover_image TEXT,
    media_total_episodes INTEGER,
    media_total_chapters INTEGER,
    media_total_volumes INTEGER
);

-- Indexes
CREATE UNIQUE INDEX idx_media_list_entries_media
    ON media_list_entries(media_id, media_type);

CREATE INDEX idx_media_list_entries_status
    ON media_list_entries(status);

CREATE INDEX idx_media_list_entries_media_type
    ON media_list_entries(media_type);

CREATE INDEX idx_media_list_entries_updated_at
    ON media_list_entries(updated_at);
```

## Campos

### Identificación
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Primary key autoincremental |
| `media_id` | Int | ID de AniList del anime/manga |
| `media_type` | String | "ANIME" o "MANGA" |

### Estado de Tracking
| Campo | Tipo | Descripción | Valores |
|-------|------|-------------|---------|
| `status` | String | Estado actual | CURRENT, COMPLETED, PLANNING, DROPPED, PAUSED, REPEATING |

### Progreso
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `progress` | Int | Episodios vistos o capítulos leídos |
| `progress_volumes` | Int? | Volúmenes leídos (solo manga) |
| `repeat_count` | Int | Número de veces repetido (rewatch/reread) |

### Calificación
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `score` | Float? | Calificación personal 0.0-10.0, null = no calificado |

### Fechas
| Campo | Tipo | Descripción | Formato |
|-------|------|-------------|---------|
| `start_date` | String? | Fecha de inicio | YYYY-MM-DD |
| `finish_date` | String? | Fecha de finalización | YYYY-MM-DD |

### Notas
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `notes` | String? | Notas personales del usuario |

### Prioridad
| Campo | Tipo | Descripción | Valores |
|-------|------|-------------|---------|
| `priority` | Int | Prioridad (para PLANNING) | 0=LOW, 1=MEDIUM, 2=HIGH |

### Metadata
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `created_at` | Long | Timestamp de creación (milisegundos) |
| `updated_at` | Long | Timestamp de última actualización |

### Sync (Futuro)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `anilist_entry_id` | Int? | ID de MediaList entry en AniList |
| `is_synced` | Boolean | Flag: está sincronizado con AniList |
| `needs_sync` | Boolean | Flag: tiene cambios locales pendientes de sync |

### Cached Media Info
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `media_title` | String? | Título del media (cacheado) |
| `media_cover_image` | String? | URL de cover image |
| `media_total_episodes` | Int? | Total de episodios (anime) |
| `media_total_chapters` | Int? | Total de capítulos (manga) |
| `media_total_volumes` | Int? | Total de volúmenes (manga) |

## Índices

### idx_media_list_entries_media (UNIQUE)
- **Campos**: `media_id`, `media_type`
- **Propósito**: Asegurar que cada anime/manga aparezca solo una vez
- **Performance**: Lookup rápido por ID de media

### idx_media_list_entries_status
- **Campo**: `status`
- **Propósito**: Filtrado rápido por estado (CURRENT, COMPLETED, etc.)

### idx_media_list_entries_media_type
- **Campo**: `media_type`
- **Propósito**: Separación rápida de listas de anime y manga

### idx_media_list_entries_updated_at
- **Campo**: `updated_at`
- **Propósito**: Ordenamiento por fecha de actualización

## Queries Comunes

### Obtener entrada específica
```sql
SELECT * FROM media_list_entries
WHERE media_id = ? AND media_type = ?
LIMIT 1;
```

### Lista por tipo y estado
```sql
SELECT * FROM media_list_entries
WHERE media_type = ? AND status = ?
ORDER BY updated_at DESC;
```

### Estadísticas por tipo
```sql
SELECT status, COUNT(*) as count
FROM media_list_entries
WHERE media_type = ?
GROUP BY status;
```

### Búsqueda por título
```sql
SELECT * FROM media_list_entries
WHERE media_title LIKE '%' || ? || '%';
```

### Entradas pendientes de sync
```sql
SELECT * FROM media_list_entries
WHERE needs_sync = 1;
```

## Entity Kotlin

```kotlin
@Entity(
    tableName = "media_list_entries",
    indices = [
        Index(value = ["media_id", "media_type"], unique = true),
        Index(value = ["status"]),
        Index(value = ["media_type"]),
        Index(value = ["updated_at"])
    ]
)
data class MediaListEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "media_id")
    val mediaId: Int,

    @ColumnInfo(name = "media_type")
    val mediaType: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "progress")
    val progress: Int = 0,

    @ColumnInfo(name = "progress_volumes")
    val progressVolumes: Int? = null,

    @ColumnInfo(name = "repeat_count")
    val repeatCount: Int = 0,

    @ColumnInfo(name = "score")
    val score: Float? = null,

    @ColumnInfo(name = "start_date")
    val startDate: String? = null,

    @ColumnInfo(name = "finish_date")
    val finishDate: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "priority")
    val priority: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "anilist_entry_id")
    val anilistEntryId: Int? = null,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "needs_sync")
    val needsSync: Boolean = false,

    @ColumnInfo(name = "media_title")
    val mediaTitle: String? = null,

    @ColumnInfo(name = "media_cover_image")
    val mediaCoverImage: String? = null,

    @ColumnInfo(name = "media_total_episodes")
    val mediaTotalEpisodes: Int? = null,

    @ColumnInfo(name = "media_total_chapters")
    val mediaTotalChapters: Int? = null,

    @ColumnInfo(name = "media_total_volumes")
    val mediaTotalVolumes: Int? = null
)
```

## Migraciones

### Version 1 (Inicial)
- Creación de tabla `media_list_entries`
- Todos los índices

### Futuras Versiones
- v2: Agregar campos adicionales según necesidad
- v3+: Usar AutoMigration de Room cuando sea posible

## Optimizaciones

### Denormalización
- **Cached media info**: Evita joins y network calls
- **Trade-off**: Espacio en disco vs performance
- **Actualización**: Solo al agregar/actualizar entrada

### Índices
- **Selectivos**: Solo en campos frecuentemente filtrados
- **Compound index**: (media_id, media_type) para lookup único
- **Performance**: Sub-millisecond lookups con índices

### Capacidad Estimada
- **Entrada promedio**: ~500 bytes
- **1000 entradas**: ~500 KB
- **10000 entradas**: ~5 MB
- **Conclusión**: Escalable para cualquier uso realista

## Backup & Export

### Estrategia de Backup
1. **Automático**: Room auto-backup en Android
2. **Manual**: Export a XML de MAL
3. **Cloud**: Futuro sync con AniList

### Restore
1. **Desde XML**: Import de MAL XML
2. **Desde AniList**: Futuro sync inicial
3. **Database file**: Copia directa en caso de emergencia
