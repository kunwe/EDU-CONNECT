package com.example.educonnect.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.educonnect.data.local.dao.*
import com.example.educonnect.data.local.entities.*

@Database(
    entities = [
        User::class,
        Subject::class,
        Assignment::class,
        TimetableEntry::class,
        Quiz::class,
        Question::class,
        QuizAttempt::class,
        Material::class,
        GradeRecord::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun subjectDao(): SubjectDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun timetableDao(): TimetableDao
    abstract fun quizDao(): QuizDao
    abstract fun materialDao(): MaterialDao
    abstract fun gradeDao(): GradeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "educonnect_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
