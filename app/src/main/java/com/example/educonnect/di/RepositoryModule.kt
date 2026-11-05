package com.example.educonnect.di

import com.example.educonnect.data.local.dao.SubjectDao
import com.example.educonnect.data.local.dao.UserDao
import com.example.educonnect.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        userDao: UserDao,
        subjectDao: SubjectDao
    ): AuthRepository = AuthRepository(firebaseAuth, firestore, userDao, subjectDao)
}
