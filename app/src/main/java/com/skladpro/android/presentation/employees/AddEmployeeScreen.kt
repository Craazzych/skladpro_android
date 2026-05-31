package com.skladpro.android.presentation.employees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.ui.theme.SkladProTheme

@Composable
fun AddEmployeeScreen(
    onBack: () -> Unit,
    onSave: (fullName: String, login: String, role: UserRole) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(UserRole.Worker) }

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("Назад")
            }
            Text(
                text = "Новый сотрудник",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "После сохранения будет создан временный пароль",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("ФИО") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            RoleSelector(
                selectedRole = role,
                onRoleSelected = { role = it }
            )
            Button(
                onClick = { onSave(fullName, login, role) },
                enabled = fullName.isNotBlank() && login.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Создать профиль")
            }
        }
    }
}

@Composable
private fun RoleSelector(
    selectedRole: UserRole,
    onRoleSelected: (UserRole) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Роль сотрудника")
        UserRole.entries.forEach { role ->
            TextButton(
                onClick = { onRoleSelected(role) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (selectedRole == role) "✓ ${role.title}" else role.title
                )
            }
        }
    }
}

@Preview(
    name = "Создание сотрудника",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun AddEmployeeScreenPreview() {
    SkladProTheme {
        AddEmployeeScreen(
            onBack = {},
            onSave = { _, _, _ -> }
        )
    }
}
