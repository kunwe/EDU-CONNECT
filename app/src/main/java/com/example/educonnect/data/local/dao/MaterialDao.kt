package com.example.educonnect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.educonnect.data.local.entities.Material

@Dao
interface MaterialDao {
    @Query("SELECT * FROM materials WHERE subjectId = :subjectId")
    suspend fun getMaterialsForSubject(subjectId: String): List<Material>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: Material)
}
