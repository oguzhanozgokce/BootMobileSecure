package com.oguzhanozgokce.bootmobilesecure.data.repository

import com.oguzhanozgokce.bootmobilesecure.data.model.AuthResponse
import com.oguzhanozgokce.bootmobilesecure.data.model.LoginRequest
import com.oguzhanozgokce.bootmobilesecure.data.model.RegisterRequest
import com.oguzhanozgokce.bootmobilesecure.data.network.AuthException
import com.oguzhanozgokce.bootmobilesecure.data.network.TokenManager
import com.oguzhanozgokce.bootmobilesecure.data.network.safeCall
import com.oguzhanozgokce.bootmobilesecure.data.source.remote.AuthService
import com.oguzhanozgokce.bootmobilesecure.domain.repository.MainRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val authService: AuthService,
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
}
