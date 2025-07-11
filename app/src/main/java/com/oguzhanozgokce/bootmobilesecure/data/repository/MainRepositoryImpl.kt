package com.oguzhanozgokce.bootmobilesecure.data.repository

import com.oguzhanozgokce.bootmobilesecure.data.model.AuthResponse
import com.oguzhanozgokce.bootmobilesecure.data.model.LoginRequest
import com.oguzhanozgokce.bootmobilesecure.data.model.RegisterRequest
import com.oguzhanozgokce.bootmobilesecure.data.model.toUiUser
import com.oguzhanozgokce.bootmobilesecure.data.network.AuthException
import com.oguzhanozgokce.bootmobilesecure.data.network.TokenManager
import com.oguzhanozgokce.bootmobilesecure.data.network.safeCall
import com.oguzhanozgokce.bootmobilesecure.data.source.remote.AuthService
import com.oguzhanozgokce.bootmobilesecure.data.source.remote.UserService
import com.oguzhanozgokce.bootmobilesecure.domain.model.User
import com.oguzhanozgokce.bootmobilesecure.domain.repository.MainRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userService: UserService,
    private val tokenManager: TokenManager
) : MainRepository {

    override suspend fun login(loginRequest: LoginRequest): Result<AuthResponse> {
        return safeCall { authService.login(loginRequest) }
            .onSuccess { authResponse ->
                tokenManager.saveToken(authResponse.token, authResponse.tokenType)
            }
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<AuthResponse> {
        return safeCall { authService.register(registerRequest) }
            .onSuccess { authResponse ->
                tokenManager.saveToken(authResponse.token, authResponse.tokenType)
            }
    }

    override suspend fun refreshToken(): Result<AuthResponse> {
        return safeCall { authService.refreshToken() }
            .onSuccess { authResponse ->
                tokenManager.saveToken(authResponse.token, authResponse.tokenType)
            }
            .onFailure { exception ->
                if (exception is AuthException) {
                    tokenManager.clearToken()
                }
            }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return safeCall { userService.getCurrentUser() }
            .map { userResponse -> userResponse.toUiUser() }
            .onFailure { exception ->
                if (exception is AuthException) {
                    tokenManager.clearToken()
                }
            }
    }

    override suspend fun getUserById(id: Long): Result<User> {
        return safeCall { userService.getUserById(id) }
            .map { userResponse -> userResponse.toUiUser() }
            .onFailure { exception ->
                if (exception is AuthException) {
                    tokenManager.clearToken()
                }
            }
    }

    override suspend fun deleteUser(id: Long): Result<String> {
        return safeCall { userService.deleteUser(id) }
            .onFailure { exception ->
                if (exception is AuthException) {
                    tokenManager.clearToken()
                }
            }
    }

    override suspend fun updateProfileImage(imageFile: File): Result<User> {
        return try {
            val mediaType = when (imageFile.extension.lowercase()) {
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "gif" -> "image/gif"
                else -> "image/jpeg"
            }.toMediaTypeOrNull()

            val requestBody = imageFile.asRequestBody(mediaType)
            val imagePart = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestBody
            )

            safeCall { userService.updateProfileImage(imagePart) }
                .map { userResponse -> userResponse.toUiUser() }
                .onFailure { exception ->
                    if (exception is AuthException) {
                        tokenManager.clearToken()
                    }
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
