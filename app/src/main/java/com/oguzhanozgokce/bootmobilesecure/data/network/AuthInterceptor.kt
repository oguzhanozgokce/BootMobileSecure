package com.oguzhanozgokce.bootmobilesecure.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestPath = originalRequest.url.encodedPath
        val isAuthEndpoint = requestPath.startsWith("/api/auth/")

        return if (isAuthEndpoint) {
            chain.proceed(originalRequest)
        } else {
            val token = tokenManager.getToken()
            val tokenType = tokenManager.getTokenType()

            if (token != null) {
                val authenticatedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "$tokenType $token")
                    .addHeader("Content-Type", "application/json")
                    .build()
                val response = chain.proceed(authenticatedRequest)
                if (response.code == 401) {
                    tokenManager.clearToken()
                }
                response
            } else {
                chain.proceed(originalRequest)
            }
        }
    }
}