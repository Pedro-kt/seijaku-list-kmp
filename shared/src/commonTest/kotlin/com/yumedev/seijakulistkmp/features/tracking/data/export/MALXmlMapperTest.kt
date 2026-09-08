package com.yumedev.seijakulistkmp.features.tracking.data.export

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MALXmlMapperTest {

    private val mapper = MALXmlMapper()

    @Test
    fun `parseMALXml should parse valid anime XML correctly`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
                <user_total_anime>1</user_total_anime>
                <user_total_watching>1</user_total_watching>
                <user_total_completed>0</user_total_completed>
                <user_total_onhold>0</user_total_onhold>
                <user_total_dropped>0</user_total_dropped>
                <user_total_plantowatch>0</user_total_plantowatch>
              </myinfo>

              <anime>
                <series_animedb_id>1</series_animedb_id>
                <series_title><![CDATA[Cowboy Bebop]]></series_title>
                <series_synonyms><![CDATA[]]></series_synonyms>
                <series_type>0</series_type>
                <series_episodes>26</series_episodes>
                <series_status>1</series_status>
                <series_start>0000-00-00</series_start>
                <series_end>0000-00-00</series_end>
                <series_image><![CDATA[https://cdn.myanimelist.net/images/anime/1.jpg]]></series_image>
                <my_id>0</my_id>
                <my_watched_episodes>13</my_watched_episodes>
                <my_start_date>2023-01-15</my_start_date>
                <my_finish_date>0000-00-00</my_finish_date>
                <my_score>8</my_score>
                <my_status>Watching</my_status>
                <my_rewatching>0</my_rewatching>
                <my_rewatching_ep>0</my_rewatching_ep>
                <my_last_updated>1673740800</my_last_updated>
                <my_tags><![CDATA[Great soundtrack]]></my_tags>
              </anime>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.ANIME)

        assertEquals(1, entries.size)
        val entry = entries.first()
        assertEquals(1, entry.mediaId)
        assertEquals(MediaType.ANIME.name, entry.mediaType)
        assertEquals("Cowboy Bebop", entry.mediaTitle)
        assertEquals(13, entry.progress)
        assertEquals(8.0f, entry.score)
        assertEquals("2023-01-15", entry.startDate)
        assertNull(entry.finishDate)
        assertEquals(MediaListStatus.CURRENT.name, entry.status)
        assertEquals("Great soundtrack", entry.notes)
        assertEquals(1673740800000L, entry.updatedAt)
        assertEquals(26, entry.mediaTotalEpisodes)
    }

    @Test
    fun `parseMALXml should parse valid manga XML correctly`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>2</user_export_type>
                <user_total_manga>1</user_total_manga>
                <user_total_reading>0</user_total_reading>
                <user_total_completed>1</user_total_completed>
                <user_total_onhold>0</user_total_onhold>
                <user_total_dropped>0</user_total_dropped>
                <user_total_plantoread>0</user_total_plantoread>
              </myinfo>

              <manga>
                <series_mangadb_id>1</series_mangadb_id>
                <series_title><![CDATA[Monster]]></series_title>
                <series_synonyms><![CDATA[]]></series_synonyms>
                <series_type>0</series_type>
                <series_chapters>162</series_chapters>
                <series_volumes>18</series_volumes>
                <series_status>1</series_status>
                <series_start>0000-00-00</series_start>
                <series_end>0000-00-00</series_end>
                <series_image><![CDATA[https://cdn.myanimelist.net/images/manga/1.jpg]]></series_image>
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
                <my_tags><![CDATA[Masterpiece]]></my_tags>
              </manga>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.MANGA)

        assertEquals(1, entries.size)
        val entry = entries.first()
        assertEquals(1, entry.mediaId)
        assertEquals(MediaType.MANGA.name, entry.mediaType)
        assertEquals("Monster", entry.mediaTitle)
        assertEquals(162, entry.progress)
        assertEquals(18, entry.progressVolumes)
        assertEquals(10.0f, entry.score)
        assertEquals("2023-03-01", entry.startDate)
        assertEquals("2023-06-15", entry.finishDate)
        assertEquals(MediaListStatus.COMPLETED.name, entry.status)
        assertEquals("Masterpiece", entry.notes)
        assertEquals(162, entry.mediaTotalChapters)
        assertEquals(18, entry.mediaTotalVolumes)
    }

    @Test
    fun `parseMALXml should handle zero dates as null`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
              </myinfo>
              <anime>
                <series_animedb_id>1</series_animedb_id>
                <series_title><![CDATA[Test]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>0</my_watched_episodes>
                <my_start_date>0000-00-00</my_start_date>
                <my_finish_date>0000-00-00</my_finish_date>
                <my_score>0</my_score>
                <my_status>Plan to Watch</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.ANIME)

        val entry = entries.first()
        assertNull(entry.startDate)
        assertNull(entry.finishDate)
        assertNull(entry.score)
    }

    @Test
    fun `parseMALXml should convert timestamps from seconds to milliseconds`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
              </myinfo>
              <anime>
                <series_animedb_id>1</series_animedb_id>
                <series_title><![CDATA[Test]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>0</my_watched_episodes>
                <my_status>Watching</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.ANIME)

        val entry = entries.first()
        assertEquals(1673740800000L, entry.updatedAt)
        assertEquals(1673740800000L, entry.createdAt)
    }

    @Test
    fun `parseMALXml should handle REPEATING status correctly for anime`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
              </myinfo>
              <anime>
                <series_animedb_id>1</series_animedb_id>
                <series_title><![CDATA[Test]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>5</my_watched_episodes>
                <my_status>Watching</my_status>
                <my_rewatching>1</my_rewatching>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.ANIME)

        val entry = entries.first()
        assertEquals(MediaListStatus.REPEATING.name, entry.status)
    }

    @Test
    fun `parseMALXml should handle REPEATING status correctly for manga`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>2</user_export_type>
              </myinfo>
              <manga>
                <series_mangadb_id>1</series_mangadb_id>
                <series_title><![CDATA[Test]]></series_title>
                <series_chapters>50</series_chapters>
                <series_volumes>10</series_volumes>
                <my_read_chapters>10</my_read_chapters>
                <my_read_volumes>2</my_read_volumes>
                <my_status>Reading</my_status>
                <my_rereading>1</my_rereading>
                <my_last_updated>1673740800</my_last_updated>
              </manga>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.MANGA)

        val entry = entries.first()
        assertEquals(MediaListStatus.REPEATING.name, entry.status)
    }

    @Test
    fun `parseMALXml should convert zero score to null`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
              </myinfo>
              <anime>
                <series_animedb_id>1</series_animedb_id>
                <series_title><![CDATA[Test]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>0</my_watched_episodes>
                <my_score>0</my_score>
                <my_status>Watching</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.ANIME)

        val entry = entries.first()
        assertNull(entry.score)
    }

    @Test
    fun `parseMALXml should throw exception for wrong export type`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>2</user_export_type>
              </myinfo>
              <manga>
                <series_mangadb_id>1</series_mangadb_id>
                <series_title><![CDATA[Test]]></series_title>
                <series_chapters>50</series_chapters>
                <series_volumes>10</series_volumes>
                <my_read_chapters>0</my_read_chapters>
                <my_status>Reading</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </manga>
            </myanimelist>
        """.trimIndent()

        assertFailsWith<MALXmlParseException> {
            mapper.parseMALXml(xmlContent, MediaType.ANIME)
        }
    }

    @Test
    fun `parseMALXml should throw exception for empty entries`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
              </myinfo>
            </myanimelist>
        """.trimIndent()

        assertFailsWith<MALXmlParseException> {
            mapper.parseMALXml(xmlContent, MediaType.ANIME)
        }
    }

    @Test
    fun `parseMALXml should throw exception for blank XML`() {
        assertFailsWith<IllegalArgumentException> {
            mapper.parseMALXml("", MediaType.ANIME)
        }
    }

    @Test
    fun `parseMALXml should handle multiple entries correctly`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
              </myinfo>
              <anime>
                <series_animedb_id>1</series_animedb_id>
                <series_title><![CDATA[Test 1]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>12</my_watched_episodes>
                <my_status>Completed</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
              <anime>
                <series_animedb_id>2</series_animedb_id>
                <series_title><![CDATA[Test 2]]></series_title>
                <series_episodes>24</series_episodes>
                <my_watched_episodes>5</my_watched_episodes>
                <my_status>Watching</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
              <anime>
                <series_animedb_id>3</series_animedb_id>
                <series_title><![CDATA[Test 3]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>0</my_watched_episodes>
                <my_status>Plan to Watch</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.ANIME)

        assertEquals(3, entries.size)
        assertEquals("Test 1", entries[0].mediaTitle)
        assertEquals(MediaListStatus.COMPLETED.name, entries[0].status)
        assertEquals("Test 2", entries[1].mediaTitle)
        assertEquals(MediaListStatus.CURRENT.name, entries[1].status)
        assertEquals("Test 3", entries[2].mediaTitle)
        assertEquals(MediaListStatus.PLANNING.name, entries[2].status)
    }

    @Test
    fun `parseMALXml should handle empty notes correctly`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
              </myinfo>
              <anime>
                <series_animedb_id>1</series_animedb_id>
                <series_title><![CDATA[Test]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>0</my_watched_episodes>
                <my_status>Watching</my_status>
                <my_last_updated>1673740800</my_last_updated>
                <my_tags><![CDATA[]]></my_tags>
              </anime>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.ANIME)

        val entry = entries.first()
        assertNull(entry.notes)
    }

    @Test
    fun `parseMALXml should parse all status types correctly`() {
        val xmlContent = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <myanimelist>
              <myinfo>
                <user_export_type>1</user_export_type>
              </myinfo>
              <anime>
                <series_animedb_id>1</series_animedb_id>
                <series_title><![CDATA[Watching]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>5</my_watched_episodes>
                <my_status>Watching</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
              <anime>
                <series_animedb_id>2</series_animedb_id>
                <series_title><![CDATA[Completed]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>12</my_watched_episodes>
                <my_status>Completed</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
              <anime>
                <series_animedb_id>3</series_animedb_id>
                <series_title><![CDATA[On-Hold]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>3</my_watched_episodes>
                <my_status>On-Hold</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
              <anime>
                <series_animedb_id>4</series_animedb_id>
                <series_title><![CDATA[Dropped]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>2</my_watched_episodes>
                <my_status>Dropped</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
              <anime>
                <series_animedb_id>5</series_animedb_id>
                <series_title><![CDATA[Plan to Watch]]></series_title>
                <series_episodes>12</series_episodes>
                <my_watched_episodes>0</my_watched_episodes>
                <my_status>Plan to Watch</my_status>
                <my_last_updated>1673740800</my_last_updated>
              </anime>
            </myanimelist>
        """.trimIndent()

        val entries = mapper.parseMALXml(xmlContent, MediaType.ANIME)

        assertEquals(5, entries.size)
        assertEquals(MediaListStatus.CURRENT.name, entries[0].status)
        assertEquals(MediaListStatus.COMPLETED.name, entries[1].status)
        assertEquals(MediaListStatus.PAUSED.name, entries[2].status)
        assertEquals(MediaListStatus.DROPPED.name, entries[3].status)
        assertEquals(MediaListStatus.PLANNING.name, entries[4].status)
    }
}
