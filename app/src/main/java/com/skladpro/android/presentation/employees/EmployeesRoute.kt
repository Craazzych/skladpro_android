package com.skladpro.android.presentation.employees

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun EmployeesRoute(
    viewModel: EmployeesViewModel,
    onBack: () -> Unit,
    onAddEmployee: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    EmployeesScreen(
        state = state,
        onBack = onBack,
        onAddEmployee = onAddEmployee,
        onDeleteEmployee = viewModel::deleteEmployee
    )
}
