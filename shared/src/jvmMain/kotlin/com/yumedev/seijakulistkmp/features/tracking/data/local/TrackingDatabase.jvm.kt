package com.yumedev.seijakulistkmp.features.tracking.data.local

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

actual object TrackingDatabaseBuilder {
    actual fun create(): RoomDatabase.Builder<TrackingDatabase> {
        val dbFile = File(System.getProperty("user.home"), ".seijaku/${TrackingDatabase.DATABASE_NAME}")
        dbFile.parentFile?.mkdirs()
        return Room.databaseBuilder<TrackingDatabase>(
            name = dbFile.absolutePath
        )
    }
}
