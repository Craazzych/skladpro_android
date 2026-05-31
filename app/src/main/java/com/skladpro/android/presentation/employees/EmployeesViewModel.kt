package com.skladpro.android.presentation.employees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skladpro.android.data.AppContainer
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmployeesViewModel(
    private val repository: EmployeeRepository = AppContainer.employeeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EmployeesUiState(isLoading = true))
    val uiState: StateFlow<EmployeesUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { repository.getEmployees() }
                .onSuccess { employees ->
                    _uiState.value = EmployeesUiState(employees = employees)
                }
                .onFailure(::showError)
        }
    }

    fun createEmployee(
        fullName: String,
        login: String,
        role: UserRole,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            runCatching { repository.createEmployee(fullName, login, role) }
                .onSuccess {
                    refresh()
                    onSuccess()
                }
                .onFailure(::showError)
        }
    }

    fun deleteEmployee(employeeId: String) {
        viewModelScope.launch {
            runCatching { repository.deleteEmployee(employeeId) }
                .onSuccess { refresh() }
                .onFailure(::showError)
        }
    }

    private fun showError(error: Throwable) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = error.message ?: "Не удалось выполнить запрос"
            )
        }
    }
}
