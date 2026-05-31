package com.skladpro.android.data.repository

import com.skladpro.android.data.AppSession
import com.skladpro.android.data.remote.SkladProApi
import com.skladpro.android.data.remote.toDomain
import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.repository.AuthRepository

class NetworkAuthRepository(
    private val api: SkladProApi,
    private val session: AppSession
) : AuthRepository {
    override suspend fun login(login: String, password: String): EmployeeProfile {
        val response = api.login(login, password)
        session.start(response.employee.id, response.token)
        return response.employee.toDomain()
    }

    override suspend fun activate(
        login: String,
        temporaryPassword: String,
        newPassword: String
    ) {
        api.activate(login, temporaryPassword, newPassword)
    }
}
