package com.example.perfilapp.data.remote

import com.example.perfilapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @GET("api/users/me")
    suspend fun getMe(@Header("Authorization") bearerToken: String): Response<ProfileResponse>
}
