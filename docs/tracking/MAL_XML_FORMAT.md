# MyAnimeList XML Export Format

Especificación del formato XML para exportar listas de anime y manga compatible con MyAnimeList y otros trackers.

## Formato Anime

### Estructura Completa

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<myanimelist>
  <myinfo>
    <user_id>0</user_id>
    <user_name>username</user_name>
    <user_export_type>1</user_export_type>
    <user_total_anime>150</user_total_anime>
    <user_total_watching>15</user_total_watching>
    <user_total_completed>100</user_total_completed>
    <user_total_onhold>10</user_total_onhold>
    <user_total_dropped>5</user_total_dropped>
    <user_total_plantowatch>20</user_total_plantowatch>
  </myinfo>

  <anime>
    <!-- Series Information -->
    <series_animedb_id>48</series_animedb_id>
    <series_title><![CDATA[.hack//SIGN]]></series_title>
    <series_synonyms><![CDATA[dot hack SIGN]]></series_synonyms>
    <series_type>1</series_type>
    <series_episodes>26</series_episodes>
    <series_status>2</series_status>
    <series_start>2002-04-04</series_start>
    <series_end>2002-09-25</series_end>
    <series_image><![CDATA[https://cdn.myanimelist.net/images/anime/48.jpg]]></series_image>

    <!-- User Tracking Data -->
    <my_id>0</my_id>
    <my_watched_episodes>26</my_watched_episodes>
    <my_start_date>2023-01-15</my_start_date>
    <my_finish_date>2023-02-20</my_finish_date>
    <my_score>8</my_score>
    <my_status>Completed</my_status>
    <my_rewatching>0</my_rewatching>
    <my_rewatching_ep>0</my_rewatching_ep>
    <my_last_updated>1676419200</my_last_updated>
    <my_tags><![CDATA[Great soundtrack, Mind-bending]]></my_tags>
  </anime>

  <!-- More <anime> entries... -->
</myanimelist>
```

### Campos de myinfo (Anime)

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `user_id` | Int | ID del usuario (0 para export local) |
| `user_name` | String | Nombre de usuario (vacío para local) |
| `user_export_type` | Int | Tipo de export: 1 = Anime |
| `user_total_anime` | Int | Total de entradas |
| `user_total_watching` | Int | Total en estado "Watching" |
| `user_total_completed` | Int | Total completados |
| `user_total_onhold` | Int | Total en pausa |
| `user_total_dropped` | Int | Total abandonados |
| `user_total_plantowatch` | Int | Total planeados |

### Campos de anime

#### Información de la Serie

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `series_animedb_id` | Int | ID del anime (AniList ID) |
| `series_title` | String | Título del anime (CDATA) |
| `series_synonyms` | String | Títulos alternativos (CDATA) |
| `series_type` | Int | 1=TV, 2=OVA, 3=Movie, 4=Special, 5=ONA, 6=Music |
| `series_episodes` | Int | Total de episodios (0 = desconocido) |
| `series_status` | Int | 1=Airing, 2=Finished, 3=Not Yet Aired |
| `series_start` | String | Fecha inicio (YYYY-MM-DD o 0000-00-00) |
| `series_end` | String | Fecha fin (YYYY-MM-DD o 0000-00-00) |
| `series_image` | String | URL de imagen (CDATA) |

#### Datos de Tracking del Usuario

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `my_id` | Int | ID de la entrada (0 para local) |
| `my_watched_episodes` | Int | Episodios vistos |
| `my_start_date` | String | Fecha inicio usuario (YYYY-MM-DD o 0000-00-00) |
| `my_finish_date` | String | Fecha fin usuario (YYYY-MM-DD o 0000-00-00) |
| `my_score` | Int | Score 0-10 (0 = no calificado) |
| `my_status` | String | Watching, Completed, On-Hold, Dropped, Plan to Watch |
| `my_rewatching` | Int | 0=no, 1=sí |
| `my_rewatching_ep` | Int | Episodio actual si rewatching |
| `my_last_updated` | Long | Unix timestamp (segundos) |
| `my_tags` | String | Notas/tags del usuario (CDATA) |

## Formato Manga

### Estructura Completa

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<myanimelist>
  <myinfo>
    <user_id>0</user_id>
    <user_name>username</user_name>
    <user_export_type>2</user_export_type>
    <user_total_manga>200</user_total_manga>
    <user_total_reading>20</user_total_reading>
    <user_total_completed>150</user_total_completed>
    <user_total_onhold>15</user_total_onhold>
    <user_total_dropped>10</user_total_dropped>
    <user_total_plantoread>5</user_total_plantoread>
  </myinfo>

  <manga>
    <!-- Series Information -->
    <series_mangadb_id>1</series_mangadb_id>
    <series_title><![CDATA[Monster]]></series_title>
    <series_synonyms><![CDATA[]]></series_synonyms>
    <series_type>1</series_type>
    <series_chapters>162</series_chapters>
    <series_volumes>18</series_volumes>
    <series_status>2</series_status>
    <series_start>1994-12-05</series_start>
    <series_end>2001-12-20</series_end>
    <series_image><![CDATA[https://cdn.myanimelist.net/images/manga/1.jpg]]></series_image>

    <!-- User Tracking Data -->
    <my_id>0</my_id>
    <my_read_chapters>162</my_read_chapters>
    <my_read_volumes>18</my_read_volumes>
    <my_start_date>2023-03-01</my_start_date>
    <my_finish_date>2023-06-15</my_finish_date>
    <my_score>10</my_score>
    <my_status>Completed</my_status>
    <my_rereading>0</my_rereading>
    <my_rereading_chap>0</my_rereading_chap>
    <my_last_updated>1686787200</my_last_updated>
    <my_tags><![CDATA[Masterpiece, Psychological thriller]]></my_tags>
  </manga>

  <!-- More <manga> entries... -->
</myanimelist>
```

### Campos de myinfo (Manga)

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `user_export_type` | Int | Tipo de export: 2 = Manga |
| `user_total_manga` | Int | Total de entradas |
| `user_total_reading` | Int | Total en estado "Reading" |
| `user_total_completed` | Int | Total completados |
| `user_total_onhold` | Int | Total en pausa |
| `user_total_dropped` | Int | Total abandonados |
| `user_total_plantoread` | Int | Total planeados |

### Campos de manga

#### Información de la Serie

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `series_mangadb_id` | Int | ID del manga (AniList ID) |
| `series_title` | String | Título del manga (CDATA) |
| `series_synonyms` | String | Títulos alternativos (CDATA) |
| `series_type` | Int | 1=Manga, 2=Novel, 3=One-shot, 4=Doujinshi, 5=Manhwa, 6=Manhua, 7=OEL |
| `series_chapters` | Int | Total de capítulos (0 = desconocido) |
| `series_volumes` | Int | Total de volúmenes (0 = desconocido) |
| `series_status` | Int | 1=Publishing, 2=Finished |
| `series_start` | String | Fecha inicio (YYYY-MM-DD o 0000-00-00) |
| `series_end` | String | Fecha fin (YYYY-MM-DD o 0000-00-00) |
| `series_image` | String | URL de imagen (CDATA) |

#### Datos de Tracking del Usuario

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `my_id` | Int | ID de la entrada (0 para local) |
| `my_read_chapters` | Int | Capítulos leídos |
| `my_read_volumes` | Int | Volúmenes leídos |
| `my_start_date` | String | Fecha inicio usuario (YYYY-MM-DD o 0000-00-00) |
| `my_finish_date` | String | Fecha fin usuario (YYYY-MM-DD o 0000-00-00) |
| `my_score` | Int | Score 0-10 (0 = no calificado) |
| `my_status` | String | Reading, Completed, On-Hold, Dropped, Plan to Read |
| `my_rereading` | Int | 0=no, 1=sí |
| `my_rereading_chap` | Int | Capítulo actual si rereading |
| `my_last_updated` | Long | Unix timestamp (segundos) |
| `my_tags` | String | Notas/tags del usuario (CDATA) |

## Mapeo de Estados

### AniList → MAL Status (Anime)

| AniList Status | MAL Status | my_rewatching |
|----------------|------------|---------------|
| CURRENT | Watching | 0 |
| COMPLETED | Completed | 0 |
| PLANNING | Plan to Watch | 0 |
| DROPPED | Dropped | 0 |
| PAUSED | On-Hold | 0 |
| REPEATING | Watching | 1 |

### AniList → MAL Status (Manga)

| AniList Status | MAL Status | my_rereading |
|----------------|------------|--------------|
| CURRENT | Reading | 0 |
| COMPLETED | Completed | 0 |
| PLANNING | Plan to Read | 0 |
| DROPPED | Dropped | 0 |
| PAUSED | On-Hold | 0 |
| REPEATING | Reading | 1 |

## Notas de Implementación

### CDATA Sections
Usar `<![CDATA[...]]>` para:
- Títulos con caracteres especiales
- URLs
- Notas/tags del usuario

### Fechas
- Formato: `YYYY-MM-DD`
- Si no disponible: `0000-00-00`
- Validación: Debe ser fecha válida o `0000-00-00`

### Timestamps
- `my_last_updated`: Unix timestamp en **segundos** (no milisegundos)
- Conversión: `System.currentTimeMillis() / 1000`

### Scores
- Rango: 0-10 (enteros)
- 0 = No calificado
- Nuestra app usa Float (0.0-10.0), redondear al exportar

### Valores por Defecto
- IDs desconocidos: `0`
- Campos vacíos: String vacío en CDATA
- Flags booleanos: `0` o `1`

### Encoding
- UTF-8 obligatorio
- Declaración XML: `<?xml version="1.0" encoding="UTF-8" ?>`

## Ejemplo de Generación en Kotlin

```kotlin
fun generateMALXml(entries: List<MediaListEntryEntity>, mediaType: MediaType): String {
    return buildString {
        appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
        appendLine("<myanimelist>")
        appendLine("  <myinfo>")
        appendLine("    <user_export_type>${if (mediaType == MediaType.ANIME) 1 else 2}</user_export_type>")

        // Statistics...

        appendLine("  </myinfo>")

        entries.forEach { entry ->
            val tag = if (mediaType == MediaType.ANIME) "anime" else "manga"
            appendLine("  <$tag>")

            // Series info
            appendLine("    <series_${if (mediaType == MediaType.ANIME) "animedb" else "mangadb"}_id>${entry.mediaId}</series_${if (mediaType == MediaType.ANIME) "animedb" else "mangadb"}_id>")
            appendLine("    <series_title><![CDATA[${entry.mediaTitle ?: ""}]]></series_title>")

            // User data
            val status = MediaListStatus.valueOf(entry.status)
            appendLine("    <my_status>${status.toMALStatus(mediaType)}</my_status>")

            appendLine("  </$tag>")
        }

        appendLine("</myanimelist>")
    }
}
```

## Validación

### Schema básico para validar
- Elemento raíz: `<myanimelist>`
- Debe contener: `<myinfo>` y uno o más `<anime>` o `<manga>`
- Campos requeridos en myinfo: `user_export_type`
- Campos requeridos por entrada: `series_*db_id`, `my_status`

### Compatibilidad
- MyAnimeList oficial
- AniList import (parcial)
- Taiga Anime Tracker
- Kitsu (con limitaciones)
- MAL-Sync browser extension

## Referencias

- [MyAnimeList XML Export Documentation](https://myanimelist.net/forum/?topicid=500781)
- [MAL API Documentation](https://myanimelist.net/apiconfig/references/api/v2)
