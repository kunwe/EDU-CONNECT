package com.example.educonnect.data.remote

import retrofit2.http.*

interface ApiService {

    // Auth Endpoints
    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Map<String, String>

    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): Map<String, String>

    @POST("auth/google")
    suspend fun googleAuth(@Body body: Map<String, String>): Map<String, String>

    @POST("auth/refresh")
    suspend fun refreshTokens(@Body body: Map<String, String>): Map<String, String>

    @POST("auth/logout")
    suspend fun logout(): Map<String, String>

    // Subjects & Quizzes
    @GET("subjects")
    suspend fun getSubjects(): List<Map<String, Any>>

    @POST("subjects")
    suspend fun createSubject(@Body body: Map<String, Any>): Map<String, Any>

    @GET("quizzes")
    suspend fun getQuizzes(): List<Map<String, Any>>

    @POST("quizzes")
    suspend fun createQuiz(@Body body: Map<String, Any>): Map<String, Any>

    @GET("quizzes/{id}")
    suspend fun getQuizById(@Path("id") id: String): Map<String, Any>

    @PUT("quizzes/{id}")
    suspend fun updateQuiz(@Path("id") id: String, @Body body: Map<String, Any>): Map<String, Any>

    @DELETE("quizzes/{id}")
    suspend fun deleteQuiz(@Path("id") id: String): Map<String, Any>

    @POST("quizzes/{id}/take")
    suspend fun submitQuizAttempt(@Path("id") id: String, @Body body: Map<String, Any>): Map<String, Any>

    @GET("quizzes/{id}/results")
    suspend fun getQuizResults(@Path("id") id: String): Map<String, Any>

    // User & Analytics
    @GET("users/me")
    suspend fun getProfile(): Map<String, Any>

    @PUT("users/me")
    suspend fun updateProfile(@Body body: Map<String, Any>): Map<String, Any>

    @GET("progress/overview")
    suspend fun getProgressOverview(): Map<String, Any>
}
