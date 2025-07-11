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
 * Enhanced response handler that uses backend error messages
 */
fun <T> handleResponse(response: Response<ApiResponse<T>>): Result<T> {
    return when {
        response.isSuccessful -> {
            val apiResponse = response.body()
            when {
                apiResponse == null -> Result.failure(
                    NetworkException("Yanıt alınamadı")
                )

                apiResponse.success && apiResponse.data != null -> {
                    Result.success(apiResponse.data)
                }

                else -> {
                    val errorMessage = apiResponse.message.ifEmpty { "API hatası oluştu" }
                    Result.failure(ApiException(errorMessage))
                }
            }
        }

        else -> Result.failure(response.code().toHttpException())
    }
}

/**
 * Converts HTTP status codes to appropriate exceptions
 * Based on Spring Boot backend responses
 */
private fun Int.toHttpException(): Exception = when (this) {
    400 -> ClientException("Geçersiz veri girişi")
    401 -> AuthException("Kullanıcı adı veya şifre hatalı")
    409 -> ConflictException("Bu kullanıcı adı veya email zaten kullanımda")
    500 -> ServerException("Sunucu hatası oluştu")
    in 400..499 -> ClientException("İstek hatası")
    in 500..599 -> ServerException("Sunucu hatası")
    else -> NetworkException("Bilinmeyen hata: $this")
}

/**
 * Converts exceptions to appropriate network exceptions
 */
fun Exception.toNetworkException(): Exception = when (this) {
    is SocketTimeoutException -> NetworkException("Bağlantı zaman aşımına uğradı")
    is UnknownHostException -> NetworkException("İnternet bağlantınızı kontrol edin")
    is IOException -> NetworkException("Ağ hatası oluştu")
    is NetworkException -> this
    else -> NetworkException("Beklenmeyen hata")
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

// ==================== USER MESSAGE EXTENSIONS ====================

/**
 * Enhanced Throwable extension with specific backend error handling
 * Works with Result.onFailure { throwable -> throwable.getUserMessage() }
 */
fun Throwable.getUserMessage(): String = when (this) {
    is AuthException -> handleAuthError()
    is ConflictException -> handleConflictError()
    is ApiException -> handleApiError()
    is ClientException -> "Girilen bilgiler geçersiz"
    is ServerException -> "Sunucu geçici olarak erişilebilir değil"
    is NetworkException -> handleNetworkError()
    is SocketTimeoutException -> "Bağlantı zaman aşımına uğradı"
    is UnknownHostException -> "İnternet bağlantınızı kontrol edin"
    is IOException -> "Ağ hatası oluştu"
    else -> message?.takeIf { it.isNotBlank() } ?: "Bir hata oluştu, tekrar deneyin"
}

/**
 * Handle authentication specific errors
 */
private fun AuthException.handleAuthError(): String = when {
    message?.contains("Invalid username or password", ignoreCase = true) == true ->
        "Kullanıcı adı veya şifre hatalı"
    message?.contains("Session expired", ignoreCase = true) == true ->
        "Oturum süreniz doldu, tekrar giriş yapın"
    message?.contains("Account locked", ignoreCase = true) == true ->
        "Hesabınız geçici olarak kilitlendi"
    else -> "Kimlik doğrulama hatası"
}

/**
 * Handle conflict specific errors
 */
private fun ConflictException.handleConflictError(): String = when {
    message?.contains("username", ignoreCase = true) == true ->
        "Bu kullanıcı adı zaten kullanımda"
    message?.contains("email", ignoreCase = true) == true ->
        "Bu email adresi zaten kullanımda"
    else -> "Bu bilgiler zaten kullanımda"
}

/**
 * Handle API specific errors with backend validation messages
 */
private fun ApiException.handleApiError(): String = when {
    message?.contains("Email should be valid", ignoreCase = true) == true ->
        "Geçerli bir email adresi girin"
    message?.contains("Password must be at least", ignoreCase = true) == true ->
        "Şifre en az 6 karakter olmalı"
    message?.contains("Username must be between", ignoreCase = true) == true ->
        "Kullanıcı adı 3-20 karakter arası olmalı"
    message?.contains("validation", ignoreCase = true) == true ->
        "Girilen bilgiler geçersiz"
    message?.contains("not found", ignoreCase = true) == true ->
        "Aranan bilgi bulunamadı"
    else -> message ?: "Bir hata oluştu"
}

/**
 * Handle network specific errors
 */
private fun NetworkException.handleNetworkError(): String = when {
    message?.contains("timeout", ignoreCase = true) == true ->
        "Bağlantı zaman aşımına uğradı"
    message?.contains("connection", ignoreCase = true) == true ->
        "İnternet bağlantınızı kontrol edin"
    else -> "İnternet bağlantınızı kontrol edin"
}