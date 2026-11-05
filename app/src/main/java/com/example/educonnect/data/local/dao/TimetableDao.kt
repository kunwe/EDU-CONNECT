package com.example.educonnect.data.local.dao

import androidx.room.*
import com.example.educonnect.data.local.entities.TimetableEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {
    @Query("SELECT * FROM timetable ORDER BY id DESC")
    fun getAllTimetableFlow(): Flow<List<TimetableEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableEntry(entry: TimetableEntry)
}
