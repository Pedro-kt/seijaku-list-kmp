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
