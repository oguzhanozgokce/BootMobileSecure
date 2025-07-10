package com.oguzhanozgokce.bootmobilesecure.data.network

import android.util.Log
import com.oguzhanozgokce.bootmobilesecure.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingInterceptor @Inject constructor() : Interceptor {

    companion object {
        private const val TAG = "NetworkModule"
        private const val MAX_LOG_SIZE = 4000 // Android Log limit
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (BuildConfig.DEBUG) {
            logRequest(request)
        }

        val startTime = System.currentTimeMillis()
        val response: Response

        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "❌ REQUEST FAILED: ${request.url}", e)
            }
            throw e
        }

        if (BuildConfig.DEBUG) {
            return logResponse(response, startTime)
        }

        return response
    }

    private fun logRequest(request: okhttp3.Request) {
        logLargeString("🚀 REQUEST: ${request.method} ${request.url}")

        if (request.headers.size > 0) {
            logLargeString("📤 REQUEST HEADERS: ${request.headers}")
        }

        request.body?.let { body ->
            try {
                val buffer = Buffer()
                body.writeTo(buffer)
                val bodyString = buffer.readUtf8()
                if (bodyString.isNotEmpty()) {
                    logLargeString("📤 REQUEST BODY: $bodyString")
                } else {
                    logLargeString("📤 REQUEST BODY: (empty)")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error reading request body", e)
            }
        }
    }

    private fun logResponse(response: Response, startTime: Long): Response {
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        logLargeString("📥 RESPONSE: ${response.code} ${response.message} (${duration}ms)")

        if (response.headers.size > 0) {
            logLargeString("📥 RESPONSE HEADERS: ${response.headers}")
        }

        val responseBody = response.body
        if (responseBody != null) {
            try {
                val bodyString = responseBody.string()
                if (bodyString.isNotEmpty()) {
                    logLargeString("📥 RESPONSE BODY: $bodyString")
                }
                val newResponseBody = bodyString.toResponseBody(responseBody.contentType())
                return response.newBuilder()
                    .body(newResponseBody)
                    .build()
            } catch (e: IOException) {
                Log.e(TAG, "Error reading response body", e)
            }
        }

        return response
    }

    private fun logLargeString(message: String) {
        if (message.length <= MAX_LOG_SIZE) {
            Log.d(TAG, message)
        } else {
            // Split large messages
            var i = 0
            while (i < message.length) {
                val end = minOf(i + MAX_LOG_SIZE, message.length)
                Log.d(TAG, message.substring(i, end))
                i = end
            }
        }
    }
}