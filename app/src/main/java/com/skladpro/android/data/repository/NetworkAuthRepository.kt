package com.skladpro.android.data.repository

import com.skladpro.android.data.remote.SkladProApi
import com.skladpro.android.data.remote.toDomain
import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.repository.AuthRepository

class NetworkAuthRepository(
    private val api: SkladProApi
) : AuthRepository {
    override suspend fun login(login: String, password: String): EmployeeProfile =
        api.login(login, password).toDomain()

    override suspend fun activate(
        login: String,
        temporaryPassword: String,
        newPassword: String
    ) {
        api.activate(login, temporaryPassword, newPassword)
    }
}
