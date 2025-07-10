package com.oguzhanozgokce.bootmobilesecure.domain.model

data class User(
    val id: Long = 0L,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val fullName: String,
)