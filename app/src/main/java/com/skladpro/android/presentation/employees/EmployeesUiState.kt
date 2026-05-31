package com.skladpro.android.presentation.employees

import com.skladpro.android.domain.model.EmployeeProfile

data class EmployeesUiState(
    val employees: List<EmployeeProfile> = emptyList()
)
