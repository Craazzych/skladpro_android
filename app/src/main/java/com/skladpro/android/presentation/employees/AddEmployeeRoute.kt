package com.skladpro.android.presentation.employees

import androidx.compose.runtime.Composable

@Composable
fun AddEmployeeRoute(
    viewModel: EmployeesViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    AddEmployeeScreen(
        onBack = onBack,
        onSave = { fullName, login, role ->
            viewModel.createEmployee(fullName, login, role)
            onSaved()
        }
    )
}
