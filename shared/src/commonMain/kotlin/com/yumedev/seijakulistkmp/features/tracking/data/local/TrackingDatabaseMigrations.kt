package com.yumedev.seijakulistkmp.features.tracking.data.local

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE media_list_entries ADD COLUMN media_status TEXT DEFAULT NULL")
    }
}
