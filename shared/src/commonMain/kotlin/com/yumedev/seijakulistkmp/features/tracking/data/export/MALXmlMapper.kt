package com.yumedev.seijakulistkmp.features.tracking.data.export

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.data.local.entity.MediaListEntryEntity
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus

class MALXmlMapper {
    fun parseMALXml(xmlContent: String, mediaType: MediaType): List<MediaListEntryEntity> {
        // TODO: Implement XML parsing using kotlinx.serialization.xml or similar
        // For now, return empty list
        return emptyList()
    }

    fun generateMALXml(entries: List<MediaListEntryEntity>, mediaType: MediaType): String {
        return buildString {
            appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
            appendLine("<myanimelist>")
            appendLine("  <myinfo>")
            appendLine("    <user_export_type>${if (mediaType == MediaType.ANIME) 1 else 2}</user_export_type>")

            // Statistics
            val stats = calculateStats(entries)
            if (mediaType == MediaType.ANIME) {
                appendLine("    <user_total_anime>${entries.size}</user_total_anime>")
                appendLine("    <user_total_watching>${stats[MediaListStatus.CURRENT] ?: 0}</user_total_watching>")
                appendLine("    <user_total_completed>${stats[MediaListStatus.COMPLETED] ?: 0}</user_total_completed>")
                appendLine("    <user_total_onhold>${stats[MediaListStatus.PAUSED] ?: 0}</user_total_onhold>")
                appendLine("    <user_total_dropped>${stats[MediaListStatus.DROPPED] ?: 0}</user_total_dropped>")
                appendLine("    <user_total_plantowatch>${stats[MediaListStatus.PLANNING] ?: 0}</user_total_plantowatch>")
            } else {
                appendLine("    <user_total_manga>${entries.size}</user_total_manga>")
                appendLine("    <user_total_reading>${stats[MediaListStatus.CURRENT] ?: 0}</user_total_reading>")
                appendLine("    <user_total_completed>${stats[MediaListStatus.COMPLETED] ?: 0}</user_total_completed>")
                appendLine("    <user_total_onhold>${stats[MediaListStatus.PAUSED] ?: 0}</user_total_onhold>")
                appendLine("    <user_total_dropped>${stats[MediaListStatus.DROPPED] ?: 0}</user_total_dropped>")
                appendLine("    <user_total_plantoread>${stats[MediaListStatus.PLANNING] ?: 0}</user_total_plantoread>")
            }
            appendLine("  </myinfo>")

            // Entries
            entries.forEach { entry ->
                appendLine()
                val tag = if (mediaType == MediaType.ANIME) "anime" else "manga"
                appendLine("  <$tag>")

                // Series info
                val dbIdTag = if (mediaType == MediaType.ANIME) "series_animedb_id" else "series_mangadb_id"
                appendLine("    <$dbIdTag>${entry.mediaId}</$dbIdTag>")
                appendLine("    <series_title><![CDATA[${entry.mediaTitle ?: ""}]]></series_title>")
                appendLine("    <series_synonyms><![CDATA[]]></series_synonyms>")
                appendLine("    <series_type>0</series_type>")

                if (mediaType == MediaType.ANIME) {
                    appendLine("    <series_episodes>${entry.mediaTotalEpisodes ?: 0}</series_episodes>")
                } else {
                    appendLine("    <series_chapters>${entry.mediaTotalChapters ?: 0}</series_chapters>")
                    appendLine("    <series_volumes>${entry.mediaTotalVolumes ?: 0}</series_volumes>")
                }

                appendLine("    <series_status>1</series_status>")
                appendLine("    <series_start>0000-00-00</series_start>")
                appendLine("    <series_end>0000-00-00</series_end>")
                appendLine("    <series_image><![CDATA[${entry.mediaCoverImage ?: ""}]]></series_image>")

                // User tracking data
                appendLine("    <my_id>0</my_id>")

                if (mediaType == MediaType.ANIME) {
                    appendLine("    <my_watched_episodes>${entry.progress}</my_watched_episodes>")
                } else {
                    appendLine("    <my_read_chapters>${entry.progress}</my_read_chapters>")
                    appendLine("    <my_read_volumes>${entry.progressVolumes ?: 0}</my_read_volumes>")
                }

                appendLine("    <my_start_date>${entry.startDate ?: "0000-00-00"}</my_start_date>")
                appendLine("    <my_finish_date>${entry.finishDate ?: "0000-00-00"}</my_finish_date>")
                appendLine("    <my_score>${entry.score?.toInt() ?: 0}</my_score>")

                val status = MediaListStatus.valueOf(entry.status)
                appendLine("    <my_status>${status.toMALStatus(mediaType)}</my_status>")

                if (mediaType == MediaType.ANIME) {
                    appendLine("    <my_rewatching>${if (status == MediaListStatus.REPEATING) 1 else 0}</my_rewatching>")
                    appendLine("    <my_rewatching_ep>${if (status == MediaListStatus.REPEATING) entry.progress else 0}</my_rewatching_ep>")
                } else {
                    appendLine("    <my_rereading>${if (status == MediaListStatus.REPEATING) 1 else 0}</my_rereading>")
                    appendLine("    <my_rereading_chap>${if (status == MediaListStatus.REPEATING) entry.progress else 0}</my_rereading_chap>")
                }

                appendLine("    <my_last_updated>${entry.updatedAt / 1000}</my_last_updated>")
                appendLine("    <my_tags><![CDATA[${entry.notes ?: ""}]]></my_tags>")

                appendLine("  </$tag>")
            }

            appendLine("</myanimelist>")
        }
    }

    private fun calculateStats(entries: List<MediaListEntryEntity>): Map<MediaListStatus, Int> {
        return entries.groupBy { MediaListStatus.valueOf(it.status) }
            .mapValues { it.value.size }
    }
}
