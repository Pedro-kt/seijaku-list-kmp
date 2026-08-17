package com.yumedev.seijakulistkmp.features.tracking.data.local

import androidx.room3.Room
import androidx.room3.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual object TrackingDatabaseBuilder {
    actual fun create(): RoomDatabase.Builder<TrackingDatabase> {
        val dbFilePath = NSHomeDirectory() + "/Documents/${TrackingDatabase.DATABASE_NAME}"
        return Room.databaseBuilder<TrackingDatabase>(
            name = dbFilePath,
            factory = { TrackingDatabase::class.instantiateImpl() }
        )
    }
}
