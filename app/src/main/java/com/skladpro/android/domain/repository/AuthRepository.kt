package com.skladpro.android.domain.repository

import com.skladpro.android.domain.model.EmployeeProfile

interface AuthRepository {
    suspend fun login(login: String, password: String): EmployeeProfile

    suspend fun activate(
        login: String,
        temporaryPassword: String,
        newPassword: String
    )
}
