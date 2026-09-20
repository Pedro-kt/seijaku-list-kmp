package com.yumedev.seijakulistkmp.features.schedule.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.yumedev.seijakulistkmp.features.schedule.data.local.entity.AiringScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiringScheduleDao {

    @Query(
        """
        SELECT * FROM airing_schedules
        WHERE airing_at >= :startTime AND airing_at <= :endTime
        ORDER BY airing_at ASC
        """
    )
    suspend fun getScheduleForRange(startTime: Long, endTime: Long): List<AiringScheduleEntity>

    @Query(
        """
        SELECT * FROM airing_schedules
        WHERE week_start = :weekStart
        ORDER BY airing_at ASC
        """
    )
    suspend fun getScheduleForWeek(weekStart: Long): List<AiringScheduleEntity>

    @Query(
        """
        SELECT * FROM airing_schedules
        WHERE week_start = :weekStart
        ORDER BY airing_at ASC
        """
    )
    fun observeScheduleForWeek(weekStart: Long): Flow<List<AiringScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<AiringScheduleEntity>)

    @Query("DELETE FROM airing_schedules WHERE airing_at < :timestamp")
    suspend fun deleteOldSchedules(timestamp: Long)

    @Query("DELETE FROM airing_schedules WHERE week_start = :weekStart")
    suspend fun deleteWeekSchedules(weekStart: Long)

    @Query("DELETE FROM airing_schedules")
    suspend fun deleteAllSchedules()

    @Query("SELECT COUNT(*) FROM airing_schedules WHERE week_start = :weekStart")
    suspend fun getScheduleCountForWeek(weekStart: Long): Int
}
