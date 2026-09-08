package com.yumedev.seijakulistkmp.features.tracking.data.local.migrations

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_2_3 = object : Migration(2, 3) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS user_profiles (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                anilistId INTEGER,
                name TEXT NOT NULL,
                avatarLarge TEXT,
                avatarMedium TEXT,
                banner TEXT,
                about TEXT,
                animeCount INTEGER NOT NULL DEFAULT 0,
                animeMeanScore REAL NOT NULL DEFAULT 0.0,
                minutesWatched INTEGER NOT NULL DEFAULT 0,
                episodesWatched INTEGER NOT NULL DEFAULT 0,
                mangaCount INTEGER NOT NULL DEFAULT 0,
                mangaMeanScore REAL NOT NULL DEFAULT 0.0,
                chaptersRead INTEGER NOT NULL DEFAULT 0,
                volumesRead INTEGER NOT NULL DEFAULT 0,
                displayAdultContent INTEGER NOT NULL DEFAULT 0,
                titleLanguage TEXT NOT NULL DEFAULT 'ROMAJI',
                isLocal INTEGER NOT NULL DEFAULT 1,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """.trimIndent())
    }
}
