package com.oguzhanozgokce.bootmobilesecure.domain.repository

import com.oguzhanozgokce.bootmobilesecure.data.model.AuthResponse
import com.oguzhanozgokce.bootmobilesecure.data.model.LoginRequest
import com.oguzhanozgokce.bootmobilesecure.data.model.RegisterRequest

interface MainRepository {
    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse>
    suspend fun register(registerRequest: RegisterRequest): Result<AuthResponse>
    suspend fun refreshToken(): Result<AuthResponse>
}