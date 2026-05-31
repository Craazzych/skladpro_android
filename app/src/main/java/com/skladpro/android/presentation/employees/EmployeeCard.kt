package com.skladpro.android.presentation.employees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.model.EmployeeStatus

@Composable
fun EmployeeCard(
    employee: EmployeeProfile,
    onDelete: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = employee.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = employee.role.title,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                AssistChip(
                    onClick = {},
                    label = { Text(employee.status.title) }
                )
            }

            CredentialCopyRow(
                label = "Логин",
                value = employee.login,
                copyButtonText = "Копировать логин",
                onCopy = {
                    clipboardManager.setText(AnnotatedString(employee.login))
                }
            )

            if (
                employee.status == EmployeeStatus.PendingActivation &&
                employee.temporaryPassword != null
            ) {
                CredentialCopyRow(
                    label = "Временный пароль",
                    value = employee.temporaryPassword,
                    copyButtonText = "Копировать пароль",
                    onCopy = {
                        clipboardManager.setText(
                            AnnotatedString(employee.temporaryPassword)
                        )
                    }
                )
            }

            TextButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Удалить профиль")
            }
        }
    }
}

@Composable
private fun CredentialCopyRow(
    label: String,
    value: String,
    copyButtonText: String,
    onCopy: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace
        )
        TextButton(onClick = onCopy) {
            Text(copyButtonText)
        }
    }
}
