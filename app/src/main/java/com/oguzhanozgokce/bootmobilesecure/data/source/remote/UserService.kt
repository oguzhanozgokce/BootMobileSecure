package com.oguzhanozgokce.bootmobilesecure.data.source.remote

import com.oguzhanozgokce.bootmobilesecure.data.model.ApiResponse
import com.oguzhanozgokce.bootmobilesecure.data.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {

    @GET("users/profile")
    suspend fun getCurrentUser(): Response<ApiResponse<UserResponse>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<ApiResponse<UserResponse>>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Long): Response<ApiResponse<String>>

    @Multipart
    @POST("users/profile/image")
    suspend fun updateProfileImage(
        @Part image: MultipartBody.Part,
    ): Response<ApiResponse<UserResponse>>
}