package com.skladpro.android.presentation.employees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.model.EmployeeStatus
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.ui.theme.SkladProTheme

@Composable
fun EmployeesScreen(
    state: EmployeesUiState,
    onBack: () -> Unit,
    onAddEmployee: () -> Unit,
    onDeleteEmployee: (employeeId: String) -> Unit,
    onRetry: () -> Unit,
    currentEmployeeId: String?
) {
    var employeeToDelete by remember { mutableStateOf<EmployeeProfile?>(null) }
    val adminCount = state.employees.count { it.role == UserRole.Admin }

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("Назад")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Сотрудники",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Учетные записи предприятия",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(onClick = onAddEmployee) {
                    Text("Добавить")
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            state.errorMessage?.let { message ->
                Text(text = message, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = onRetry) {
                    Text("Повторить")
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = state.employees,
                    key = { it.id }
                ) { employee ->
                    EmployeeCard(
                        employee = employee,
                        deletionUnavailableReason = deletionUnavailableReason(
                            employee = employee,
                            currentEmployeeId = currentEmployeeId,
                            adminCount = adminCount
                        ),
                        onDelete = { employeeToDelete = employee }
                    )
                }
            }
        }
    }

    employeeToDelete?.let { employee ->
        DeleteEmployeeDialog(
            employee = employee,
            onDismiss = { employeeToDelete = null },
            onConfirm = {
                onDeleteEmployee(employee.id)
                employeeToDelete = null
            }
        )
    }

}

@Composable
private fun DeleteEmployeeDialog(
    employee: EmployeeProfile,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удалить профиль?") },
        text = { Text(employee.fullName) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Preview(
    name = "Список сотрудников",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun EmployeesScreenPreview() {
    SkladProTheme {
        EmployeesScreen(
            state = EmployeesUiState(
                employees = listOf(
                    EmployeeProfile(
                        id = "1",
                        fullName = "Иванов Сергей Петрович",
                        login = "s.ivanov",
                        role = UserRole.Worker,
                        status = EmployeeStatus.Active
                    ),
                    EmployeeProfile(
                        id = "2",
                        fullName = "Орлова Мария Андреевна",
                        login = "m.orlova",
                        role = UserRole.Worker,
                        status = EmployeeStatus.PendingActivation,
                        temporaryPassword = "TMP-654321"
                    )
                )
            ),
            onBack = {},
            onAddEmployee = {},
            onDeleteEmployee = {},
            onRetry = {},
            currentEmployeeId = "1"
        )
    }
}

private fun deletionUnavailableReason(
    employee: EmployeeProfile,
    currentEmployeeId: String?,
    adminCount: Int
): String? {
    return when {
        employee.id == currentEmployeeId -> "Нельзя удалить собственный профиль"
        employee.role == UserRole.Admin && adminCount <= 1 ->
            "Нельзя удалить последнего администратора"
        else -> null
    }
}
