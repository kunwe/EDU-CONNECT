package com.example.educonnect.di

import android.content.Context
import com.example.educonnect.data.local.AppDatabase
import com.example.educonnect.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = 
        AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideSubjectDao(database: AppDatabase): SubjectDao = database.subjectDao()

    @Provides
    @Singleton
    fun provideAssignmentDao(database: AppDatabase): AssignmentDao = database.assignmentDao()

    @Provides
    @Singleton
    fun provideTimetableDao(database: AppDatabase): TimetableDao = database.timetableDao()

    @Provides
    @Singleton
    fun provideMaterialDao(database: AppDatabase): MaterialDao = database.materialDao()

    @Provides
    @Singleton
    fun provideGradeDao(database: AppDatabase): GradeDao = database.gradeDao()
}
