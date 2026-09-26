package com.yumedev.seijakulistkmp.features.tracking.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.yumedev.seijakulistkmp.features.detail.data.local.dao.FavoriteEpisodeDao
import com.yumedev.seijakulistkmp.features.detail.data.local.entity.FavoriteEpisodeEntity
import com.yumedev.seijakulistkmp.features.profile.data.local.dao.UserProfileDao
import com.yumedev.seijakulistkmp.features.profile.data.local.entity.UserProfileEntity
import com.yumedev.seijakulistkmp.features.schedule.data.local.dao.AiringScheduleDao
import com.yumedev.seijakulistkmp.features.schedule.data.local.entity.AiringScheduleEntity
import com.yumedev.seijakulistkmp.features.tracking.data.local.dao.MediaListDao
import com.yumedev.seijakulistkmp.features.tracking.data.local.entity.MediaListEntryEntity

@Database(
    entities = [
        MediaListEntryEntity::class,
        UserProfileEntity::class,
        AiringScheduleEntity::class,
        FavoriteEpisodeEntity::class,
    ],
    version = 6,
    exportSchema = true
)
abstract class TrackingDatabase : RoomDatabase() {
    abstract fun mediaListDao(): MediaListDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun airingScheduleDao(): AiringScheduleDao
    abstract fun favoriteEpisodeDao(): FavoriteEpisodeDao

    companion object {
        const val DATABASE_NAME = "seijaku_tracking.db"
    }
}

expect object TrackingDatabaseBuilder {
    fun create(): RoomDatabase.Builder<TrackingDatabase>
}
