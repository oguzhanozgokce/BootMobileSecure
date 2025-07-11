package com.oguzhanozgokce.bootmobilesecure.domain.repository

import com.oguzhanozgokce.bootmobilesecure.data.model.AuthResponse
import com.oguzhanozgokce.bootmobilesecure.data.model.LoginRequest
import com.oguzhanozgokce.bootmobilesecure.data.model.RegisterRequest
import com.oguzhanozgokce.bootmobilesecure.domain.model.User
import java.io.File

interface MainRepository {
    // Auth functions
    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse>
    suspend fun register(registerRequest: RegisterRequest): Result<AuthResponse>
    suspend fun refreshToken(): Result<AuthResponse>

    // User functions
    suspend fun getCurrentUser(): Result<User>
    suspend fun getUserById(id: Long): Result<User>
    suspend fun deleteUser(id: Long): Result<String>
    suspend fun updateProfileImage(imageFile: File): Result<User>
}