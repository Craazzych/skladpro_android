package com.skladpro.android.presentation.employees

import androidx.lifecycle.ViewModel
import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.model.EmployeeStatus
import com.skladpro.android.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class EmployeesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        EmployeesUiState(
            employees = listOf(
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
        )
    )
    val uiState: StateFlow<EmployeesUiState> = _uiState.asStateFlow()

    fun createEmployee(
        fullName: String,
        login: String,
        role: UserRole
    ) {
        val password = generateTemporaryPassword()
        val employee = EmployeeProfile(
            id = UUID.randomUUID().toString(),
            fullName = fullName.trim(),
            login = login.trim(),
            role = role,
            status = EmployeeStatus.PendingActivation,
            temporaryPassword = password
        )
        _uiState.update {
            it.copy(
                employees = listOf(employee).plus(it.employees)
            )
        }
    }

    fun deleteEmployee(employeeId: String) {
        _uiState.update {
            it.copy(
                employees = it.employees.filterNot { employee -> employee.id == employeeId }
            )
        }
    }

    private fun generateTemporaryPassword(): String {
        return "TMP-${(100000..999999).random()}"
    }
}
