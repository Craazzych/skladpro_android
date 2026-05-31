package com.skladpro.android.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skladpro.android.data.AppContainer
import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AppContainer.authRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(login: String, password: String, onSuccess: (EmployeeProfile) -> Unit) {
        viewModelScope.launch {
            startRequest()
            runCatching { repository.login(login, password) }
                .onSuccess { employee ->
                    _uiState.value = AuthUiState()
                    onSuccess(employee)
                }
                .onFailure(::showError)
        }
    }

    fun prepareActivation(login: String, temporaryPassword: String) {
        _uiState.update {
            it.copy(
                activationLogin = login.trim(),
                temporaryPassword = temporaryPassword,
                errorMessage = null
            )
        }
    }

    fun activate(newPassword: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            startRequest()
            val state = _uiState.value
            runCatching {
                repository.activate(
                    login = state.activationLogin,
                    temporaryPassword = state.temporaryPassword,
                    newPassword = newPassword
                )
            }.onSuccess {
                _uiState.value = AuthUiState()
                onSuccess()
            }.onFailure(::showError)
        }
    }

    fun logout() {
        AppContainer.session.clear()
        _uiState.value = AuthUiState()
    }

    private fun startRequest() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
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
