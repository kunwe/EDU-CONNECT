package com.example.educonnect.data.local.dao

import androidx.room.*
import com.example.educonnect.data.local.entities.Assignment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Query("SELECT * FROM assignments ORDER BY id DESC")
    fun getAllAssignmentsFlow(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Update
    suspend fun updateAssignment(assignment: Assignment)
}
