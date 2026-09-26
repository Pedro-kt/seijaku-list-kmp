package com.yumedev.seijakulistkmp.features.tracking.data.local

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE media_list_entries ADD COLUMN media_status TEXT DEFAULT NULL")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE media_list_entries ADD COLUMN favorite_position INTEGER DEFAULT NULL")
        connection.execSQL("CREATE INDEX IF NOT EXISTS index_media_list_entries_favorite_position ON media_list_entries(favorite_position)")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS airing_schedules (
                id INTEGER PRIMARY KEY NOT NULL,
                media_id INTEGER NOT NULL,
                media_title_romaji TEXT NOT NULL,
                media_title_english TEXT,
                media_title_native TEXT,
                cover_image_url TEXT,
                cover_image_color TEXT,
                episode INTEGER NOT NULL,
                airing_at INTEGER NOT NULL,
                time_until_airing INTEGER NOT NULL,
                genres TEXT NOT NULL,
                average_score INTEGER,
                format TEXT,
                status TEXT,
                cached_at INTEGER NOT NULL,
                week_start INTEGER NOT NULL
            )
            """.trimIndent()
        )
        connection.execSQL("CREATE INDEX IF NOT EXISTS index_airing_schedules_week_start ON airing_schedules(week_start)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS index_airing_schedules_airing_at ON airing_schedules(airing_at)")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS favorite_episodes (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                user_id TEXT NOT NULL,
                media_id INTEGER NOT NULL,
                episode_number INTEGER NOT NULL,
                episode_title TEXT NOT NULL,
                thumbnail_url TEXT,
                marked_at INTEGER NOT NULL,
                synced_at INTEGER
            )
            """.trimIndent()
        )
        connection.execSQL("CREATE INDEX IF NOT EXISTS index_favorite_episodes_user_id ON favorite_episodes(user_id)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS index_favorite_episodes_media_id ON favorite_episodes(media_id)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_favorite_episodes_user_id_media_id_episode_number ON favorite_episodes(user_id, media_id, episode_number)")
    }
}
