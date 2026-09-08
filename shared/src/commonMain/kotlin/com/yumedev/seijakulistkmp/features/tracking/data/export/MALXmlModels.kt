package com.yumedev.seijakulistkmp.features.tracking.data.export

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("myanimelist", "", "")
data class MALXmlRoot(
    @XmlElement(true)
    val myinfo: MyInfo,

    @XmlSerialName("anime", "", "")
    @XmlElement(true)
    val anime: List<AnimeEntry> = emptyList(),

    @XmlSerialName("manga", "", "")
    @XmlElement(true)
    val manga: List<MangaEntry> = emptyList()
)

@Serializable
@XmlSerialName("myinfo", "", "")
data class MyInfo(
    @XmlSerialName("user_export_type", "", "")
    @XmlElement(true)
    val userExportType: Int,

    @XmlSerialName("user_total_anime", "", "")
    @XmlElement(true)
    val userTotalAnime: Int = 0,

    @XmlSerialName("user_total_watching", "", "")
    @XmlElement(true)
    val userTotalWatching: Int = 0,

    @XmlSerialName("user_total_completed", "", "")
    @XmlElement(true)
    val userTotalCompleted: Int = 0,

    @XmlSerialName("user_total_onhold", "", "")
    @XmlElement(true)
    val userTotalOnhold: Int = 0,

    @XmlSerialName("user_total_dropped", "", "")
    @XmlElement(true)
    val userTotalDropped: Int = 0,

    @XmlSerialName("user_total_plantowatch", "", "")
    @XmlElement(true)
    val userTotalPlantowatch: Int = 0,

    @XmlSerialName("user_total_manga", "", "")
    @XmlElement(true)
    val userTotalManga: Int = 0,

    @XmlSerialName("user_total_reading", "", "")
    @XmlElement(true)
    val userTotalReading: Int = 0,

    @XmlSerialName("user_total_plantoread", "", "")
    @XmlElement(true)
    val userTotalPlantoread: Int = 0
)

@Serializable
@XmlSerialName("anime", "", "")
data class AnimeEntry(
    @XmlSerialName("series_animedb_id", "", "")
    @XmlElement(true)
    val seriesAnimedbId: Int,

    @XmlSerialName("series_title", "", "")
    @XmlElement(true)
    val seriesTitle: String,

    @XmlSerialName("series_synonyms", "", "")
    @XmlElement(true)
    val seriesSynonyms: String = "",

    @XmlSerialName("series_type", "", "")
    @XmlElement(true)
    val seriesType: Int = 0,

    @XmlSerialName("series_episodes", "", "")
    @XmlElement(true)
    val seriesEpisodes: Int = 0,

    @XmlSerialName("series_status", "", "")
    @XmlElement(true)
    val seriesStatus: Int = 1,

    @XmlSerialName("series_start", "", "")
    @XmlElement(true)
    val seriesStart: String = "0000-00-00",

    @XmlSerialName("series_end", "", "")
    @XmlElement(true)
    val seriesEnd: String = "0000-00-00",

    @XmlSerialName("series_image", "", "")
    @XmlElement(true)
    val seriesImage: String = "",

    @XmlSerialName("my_id", "", "")
    @XmlElement(true)
    val myId: Int = 0,

    @XmlSerialName("my_watched_episodes", "", "")
    @XmlElement(true)
    val myWatchedEpisodes: Int = 0,

    @XmlSerialName("my_start_date", "", "")
    @XmlElement(true)
    val myStartDate: String = "0000-00-00",

    @XmlSerialName("my_finish_date", "", "")
    @XmlElement(true)
    val myFinishDate: String = "0000-00-00",

    @XmlSerialName("my_score", "", "")
    @XmlElement(true)
    val myScore: Int = 0,

    @XmlSerialName("my_status", "", "")
    @XmlElement(true)
    val myStatus: String,

    @XmlSerialName("my_rewatching", "", "")
    @XmlElement(true)
    val myRewatching: Int = 0,

    @XmlSerialName("my_rewatching_ep", "", "")
    @XmlElement(true)
    val myRewatchingEp: Int = 0,

    @XmlSerialName("my_last_updated", "", "")
    @XmlElement(true)
    val myLastUpdated: Long,

    @XmlSerialName("my_tags", "", "")
    @XmlElement(true)
    val myTags: String = ""
)

@Serializable
@XmlSerialName("manga", "", "")
data class MangaEntry(
    @XmlSerialName("series_mangadb_id", "", "")
    @XmlElement(true)
    val seriesMangadbId: Int,

    @XmlSerialName("series_title", "", "")
    @XmlElement(true)
    val seriesTitle: String,

    @XmlSerialName("series_synonyms", "", "")
    @XmlElement(true)
    val seriesSynonyms: String = "",

    @XmlSerialName("series_type", "", "")
    @XmlElement(true)
    val seriesType: Int = 0,

    @XmlSerialName("series_chapters", "", "")
    @XmlElement(true)
    val seriesChapters: Int = 0,

    @XmlSerialName("series_volumes", "", "")
    @XmlElement(true)
    val seriesVolumes: Int = 0,

    @XmlSerialName("series_status", "", "")
    @XmlElement(true)
    val seriesStatus: Int = 1,

    @XmlSerialName("series_start", "", "")
    @XmlElement(true)
    val seriesStart: String = "0000-00-00",

    @XmlSerialName("series_end", "", "")
    @XmlElement(true)
    val seriesEnd: String = "0000-00-00",

    @XmlSerialName("series_image", "", "")
    @XmlElement(true)
    val seriesImage: String = "",

    @XmlSerialName("my_id", "", "")
    @XmlElement(true)
    val myId: Int = 0,

    @XmlSerialName("my_read_chapters", "", "")
    @XmlElement(true)
    val myReadChapters: Int = 0,

    @XmlSerialName("my_read_volumes", "", "")
    @XmlElement(true)
    val myReadVolumes: Int = 0,

    @XmlSerialName("my_start_date", "", "")
    @XmlElement(true)
    val myStartDate: String = "0000-00-00",

    @XmlSerialName("my_finish_date", "", "")
    @XmlElement(true)
    val myFinishDate: String = "0000-00-00",

    @XmlSerialName("my_score", "", "")
    @XmlElement(true)
    val myScore: Int = 0,

    @XmlSerialName("my_status", "", "")
    @XmlElement(true)
    val myStatus: String,

    @XmlSerialName("my_rereading", "", "")
    @XmlElement(true)
    val myRereading: Int = 0,

    @XmlSerialName("my_rereading_chap", "", "")
    @XmlElement(true)
    val myRereadingChap: Int = 0,

    @XmlSerialName("my_last_updated", "", "")
    @XmlElement(true)
    val myLastUpdated: Long,

    @XmlSerialName("my_tags", "", "")
    @XmlElement(true)
    val myTags: String = ""
)
