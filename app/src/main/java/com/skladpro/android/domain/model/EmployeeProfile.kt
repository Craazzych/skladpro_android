package com.skladpro.android.domain.model

data class EmployeeProfile(
    val id: String,
    val fullName: String,
    val login: String,
    val role: UserRole,
    val status: EmployeeStatus,
    val temporaryPassword: String? = null
)
