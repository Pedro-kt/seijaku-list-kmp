package com.yumedev.seijakulistkmp.features.tracking.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

actual object TrackingDatabaseBuilder {
    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    actual fun create(): RoomDatabase.Builder<TrackingDatabase> {
        val dbFile = appContext.getDatabasePath(TrackingDatabase.DATABASE_NAME)
        return Room.databaseBuilder<TrackingDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
