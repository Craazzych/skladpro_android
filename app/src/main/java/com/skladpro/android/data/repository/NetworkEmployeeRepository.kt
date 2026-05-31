package com.skladpro.android.data.repository

import com.skladpro.android.data.AppSession
import com.skladpro.android.data.remote.CreateEmployeeRequest
import com.skladpro.android.data.remote.SkladProApi
import com.skladpro.android.data.remote.toDomain
import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.domain.repository.EmployeeRepository

class NetworkEmployeeRepository(
    private val api: SkladProApi,
    private val session: AppSession
) : EmployeeRepository {
    override suspend fun getEmployees(): List<EmployeeProfile> =
        api.getEmployees().map { it.toDomain() }

    override suspend fun createEmployee(
        fullName: String,
        login: String,
        role: UserRole
    ): EmployeeProfile = api.createEmployee(
        CreateEmployeeRequest(
            fullName = fullName,
            login = login,
            role = if (role == UserRole.Admin) "admin" else "worker"
        )
    ).toDomain()

    override suspend fun deleteEmployee(employeeId: String) {
        val actorId = session.currentEmployeeId
            ?: error("Не удалось определить текущего пользователя")
        api.deleteEmployee(employeeId, actorId)
    }
}
