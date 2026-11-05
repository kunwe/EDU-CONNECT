package com.example.educonnect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.educonnect.data.local.entities.GradeRecord

@Dao
interface GradeDao {
    @Query("SELECT * FROM grade_records WHERE subjectId = :subjectId")
    suspend fun getGradesForSubject(subjectId: String): List<GradeRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(gradeRecord: GradeRecord)
}
