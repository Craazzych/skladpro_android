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
fun PasswordSetupScreen(
    onBackToActivation: () -> Unit,
    onPasswordSaved: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    var repeatPassword by rememberSaveable { mutableStateOf("") }
    val passwordsMatch = password == repeatPassword
    val canSave = password.length >= 6 && repeatPassword.length >= 6 && passwordsMatch

    AuthScreenFrame(
        title = "Новый пароль",
        subtitle = "Завершение активации"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Новый пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Повторите пароль") },
                singleLine = true,
                isError = repeatPassword.isNotBlank() && !passwordsMatch,
                supportingText = {
                    if (repeatPassword.isNotBlank() && !passwordsMatch) {
                        Text("Пароли не совпадают")
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onPasswordSaved,
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить пароль")
            }
            TextButton(
                onClick = onBackToActivation,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад к активации")
            }
        }
    }
}

@Preview(
    name = "Создание пароля",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun PasswordSetupScreenPreview() {
    SkladProTheme {
        PasswordSetupScreen(
            onBackToActivation = {},
            onPasswordSaved = {}
        )
    }
}
