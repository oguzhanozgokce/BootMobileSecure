package com.oguzhanozgokce.bootmobilesecure.data.network

import com.oguzhanozgokce.bootmobilesecure.data.model.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Safe API call wrapper for handling network requests and responses
 * Returns a Result<T> with proper error handling
 */
suspend inline fun <T> safeCall(
    crossinline apiCall: suspend () -> Response<ApiResponse<T>>
): Result<T> = withContext(Dispatchers.IO) {
    try {
        val response = apiCall()
        handleResponse(response)
    } catch (e: Exception) {
        Result.failure(e.toNetworkException())
    }
}

/**
 * Handles ApiResponse<T> wrapped responses
 */
fun <T> handleResponse(response: Response<ApiResponse<T>>): Result<T> {
    return when {
        response.isSuccessful -> {
            val apiResponse = response.body()
            when {
                apiResponse == null -> Result.failure(
                    NetworkException("Empty response body")
                )

                apiResponse.success && apiResponse.data != null -> {
                    Result.success(apiResponse.data)
                }

                else -> Result.failure(
                    ApiException(apiResponse.message.ifEmpty { "API error occurred" })
                )
            }
        }

        else -> Result.failure(response.code().toHttpException())
    }
}

/**
 * Converts HTTP status codes to appropriate exceptions
 * Only handles errors that our Spring Boot API actually returns
 */
fun Int.toHttpException(): Exception = when (this) {
    401 -> AuthException("Session expired")
    409 -> ConflictException("Data already exists")
    in 400..499 -> ClientException("Request error")
    in 500..599 -> ServerException("Server error")
    else -> NetworkException("HTTP error: $this")
}

/**
 * Converts exceptions to appropriate network exceptions
 */
fun Exception.toNetworkException(): Exception = when (this) {
    is SocketTimeoutException -> NetworkException("Connection timeout")
    is UnknownHostException -> NetworkException("No internet connection")
    is IOException -> NetworkException("Network error")
    is NetworkException -> this
    else -> NetworkException("Unexpected error")
}

// ==================== EXCEPTION CLASSES ====================

/**
 * Base class for all API-related exceptions
 */
open class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Authentication errors (401) - User needs to login again
 */
class AuthException(message: String) : NetworkException(message)

/**
 * Client-side errors (400-499)
 */
class ClientException(message: String) : NetworkException(message)

/**
 * Server-side errors (500-599)
 */
class ServerException(message: String) : NetworkException(message)

/**
 * API business logic errors (success=false in response)
 */
class ApiException(message: String) : NetworkException(message)

/**
 * Data conflict errors (409) - Username/email already exists
 */
class ConflictException(message: String) : NetworkException(message)


/**
 * Extension function to get user-friendly error message
 */
fun Exception.getUserMessage(): String = when (this) {
    is AuthException -> "Oturum süreniz doldu, tekrar giriş yapın"
    is ConflictException -> "Bu bilgiler zaten kullanımda"
    is ServerException -> "Sunucu geçici olarak erişilebilir değil"
    is NetworkException -> "İnternet bağlantınızı kontrol edin"
    else -> "Bir hata oluştu, tekrar deneyin"
}

