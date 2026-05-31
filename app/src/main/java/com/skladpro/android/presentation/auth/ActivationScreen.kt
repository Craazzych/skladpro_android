package com.skladpro.android.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
fun ActivationScreen(
    onBackToLogin: () -> Unit,
    onActivate: (login: String, temporaryPassword: String) -> Unit,
    errorMessage: String? = null
) {
    var login by rememberSaveable { mutableStateOf("") }
    var temporaryPassword by rememberSaveable { mutableStateOf("") }

    val canActivate = login.isNotBlank() && temporaryPassword.isNotBlank()

    AuthScreenFrame(
        title = "Активация",
        subtitle = "Первичный вход сотрудника"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = temporaryPassword,
                onValueChange = { temporaryPassword = it },
                label = { Text("Временный пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            errorMessage?.let {
                Text(text = it, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = {
                    onActivate(login.trim(), temporaryPassword)
                },
                enabled = canActivate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Активировать")
            }
            TextButton(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Вернуться ко входу")
            }
        }
    }
}

@Preview(
    name = "Экран активации",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ActivationScreenPreview() {
    SkladProTheme {
        ActivationScreen(
            onBackToLogin = {},
            onActivate = { _, _ -> }
        )
    }
}
