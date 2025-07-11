package com.oguzhanozgokce.bootmobilesecure.data.model

import com.google.gson.annotations.SerializedName
import com.oguzhanozgokce.bootmobilesecure.domain.model.User

// Base Response Wrapper
data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null,
)

// Auth DTOs
data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String
)

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("user") val user: UserResponse
)

data class UserResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("profileImageUrl") val profileImageUrl: String? = null,
)

// Mapper function to convert UserResponse to UI User model
fun UserResponse.toUiUser(): User {
    // Fix localhost URLs for Android emulator
    val fixedProfileImageUrl = this.profileImageUrl?.let { url ->
        if (url.contains("localhost:8082")) {
            url.replace("localhost:8082", "192.168.1.4:8082")
        } else {
            url
        }
    }

    return User(
        id = this.id ?: 0L,
        username = this.username.orEmpty(),
        email = this.email.orEmpty(),
        firstName = this.firstName.orEmpty(),
        lastName = this.lastName.orEmpty(),
        role = this.role.orEmpty(),
        profileImageUrl = fixedProfileImageUrl,
        fullName = "${this.firstName.orEmpty()} ${this.lastName.orEmpty()}".trim()
    )
}