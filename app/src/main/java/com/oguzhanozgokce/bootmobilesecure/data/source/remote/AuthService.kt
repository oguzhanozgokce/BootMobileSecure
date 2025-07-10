package com.oguzhanozgokce.bootmobilesecure.data.source.remote

import com.oguzhanozgokce.bootmobilesecure.data.model.ApiResponse
import com.oguzhanozgokce.bootmobilesecure.data.model.AuthResponse
import com.oguzhanozgokce.bootmobilesecure.data.model.LoginRequest
import com.oguzhanozgokce.bootmobilesecure.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/refresh")
    suspend fun refreshToken(): Response<ApiResponse<AuthResponse>>
}