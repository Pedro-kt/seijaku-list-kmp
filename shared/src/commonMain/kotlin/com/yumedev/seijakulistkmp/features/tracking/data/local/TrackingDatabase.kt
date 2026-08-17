package com.yumedev.seijakulistkmp.features.tracking.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.yumedev.seijakulistkmp.features.tracking.data.local.dao.MediaListDao
import com.yumedev.seijakulistkmp.features.tracking.data.local.entity.MediaListEntryEntity

@Database(
    entities = [MediaListEntryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TrackingDatabase : RoomDatabase() {
    abstract fun mediaListDao(): MediaListDao

    companion object {
        const val DATABASE_NAME = "seijaku_tracking.db"
    }
}

expect object TrackingDatabaseBuilder {
    fun create(): RoomDatabase.Builder<TrackingDatabase>
}
