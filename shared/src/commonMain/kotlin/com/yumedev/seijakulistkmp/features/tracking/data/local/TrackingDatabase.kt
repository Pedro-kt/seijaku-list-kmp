package com.yumedev.seijakulistkmp.features.tracking.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.yumedev.seijakulistkmp.features.profile.data.local.dao.UserProfileDao
import com.yumedev.seijakulistkmp.features.profile.data.local.entity.UserProfileEntity
import com.yumedev.seijakulistkmp.features.tracking.data.local.dao.MediaListDao
import com.yumedev.seijakulistkmp.features.tracking.data.local.entity.MediaListEntryEntity

@Database(
    entities = [
        MediaListEntryEntity::class,
        UserProfileEntity::class,
    ],
    version = 3,
    exportSchema = true
)
abstract class TrackingDatabase : RoomDatabase() {
    abstract fun mediaListDao(): MediaListDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        const val DATABASE_NAME = "seijaku_tracking.db"
    }
}

expect object TrackingDatabaseBuilder {
    fun create(): RoomDatabase.Builder<TrackingDatabase>
}
