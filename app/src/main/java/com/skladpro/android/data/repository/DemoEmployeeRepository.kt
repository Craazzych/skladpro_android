package com.skladpro.android.data.repository

import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.model.EmployeeStatus
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.domain.repository.EmployeeRepository
import java.util.UUID

class DemoEmployeeRepository : EmployeeRepository {
    private val employees = mutableListOf(
        EmployeeProfile(
            id = "employee-1",
            fullName = "Иванов Сергей Петрович",
            login = "s.ivanov",
            role = UserRole.Worker,
            status = EmployeeStatus.Active
        ),
        EmployeeProfile(
            id = "employee-2",
            fullName = "Орлова Мария Андреевна",
            login = "m.orlova",
            role = UserRole.Worker,
            status = EmployeeStatus.PendingActivation,
            temporaryPassword = "TMP-654321"
        )
    )

    override suspend fun getEmployees(): List<EmployeeProfile> = employees.toList()

    override suspend fun createEmployee(
        fullName: String,
        login: String,
        role: UserRole
    ): EmployeeProfile {
        val employee = EmployeeProfile(
            id = UUID.randomUUID().toString(),
            fullName = fullName.trim(),
            login = login.trim(),
            role = role,
            status = EmployeeStatus.PendingActivation,
            temporaryPassword = generateTemporaryPassword()
        )
        employees.add(
            index = 0,
            element = employee
        )
        return employee
    }

    override suspend fun deleteEmployee(employeeId: String) {
        employees.removeIf { it.id == employeeId }
    }

    private fun generateTemporaryPassword(): String {
        return "TMP-${(100000..999999).random()}"
    }
}
