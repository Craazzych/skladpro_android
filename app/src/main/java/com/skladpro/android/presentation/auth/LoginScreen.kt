package com.skladpro.android.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skladpro.android.presentation.components.AuthScreenFrame
import com.skladpro.android.ui.theme.SkladProTheme

@Composable
fun LoginScreen(
    onOpenActivation: () -> Unit,
    onDemoWorkerLogin: () -> Unit,
    onDemoAdminLogin: () -> Unit,
    onLogin: (email: String, password: String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    AuthScreenFrame(
        title = "СкладПРО",
        subtitle = "Вход сотрудника"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Логин или email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { onLogin(email.trim(), password) },
                enabled = email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Войти")
            }
            TextButton(
                onClick = onDemoWorkerLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Демо: работник")
            }
            TextButton(
                onClick = onDemoAdminLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Демо: администратор")
            }
            TextButton(
                onClick = onOpenActivation,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Активировать учетную запись",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(
    name = "Экран входа",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun LoginScreenPreview() {
    SkladProTheme {
        LoginScreen(
            onOpenActivation = {},
            onDemoWorkerLogin = {},
            onDemoAdminLogin = {},
            onLogin = { _, _ -> }
        )
    }
}
