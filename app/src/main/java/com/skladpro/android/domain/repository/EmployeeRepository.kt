package com.skladpro.android.domain.repository

import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.model.UserRole

interface EmployeeRepository {
    suspend fun getEmployees(): List<EmployeeProfile>

    suspend fun createEmployee(
        fullName: String,
        login: String,
        role: UserRole
    ): EmployeeProfile

    suspend fun deleteEmployee(employeeId: String)
}
