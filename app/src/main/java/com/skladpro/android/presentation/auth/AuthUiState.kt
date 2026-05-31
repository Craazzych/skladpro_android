package com.skladpro.android.presentation.auth

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val activationLogin: String = "",
    val temporaryPassword: String = ""
)
