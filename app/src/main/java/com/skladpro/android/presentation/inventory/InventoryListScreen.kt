package com.skladpro.android.presentation.inventory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.UserRole

@Composable
fun InventoryListScreen(
    role: UserRole,
    darkTheme: Boolean,
    state: InventoryUiState,
    onAction: (InventoryAction) -> Unit,
    onDarkThemeChange: (Boolean) -> Unit,
    onOpenEmployees: () -> Unit,
    onAddItem: () -> Unit,
    onLogout: () -> Unit,
    onOpenDetails: (itemId: String) -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            InventoryHeader(role = role)
            Spacer(modifier = Modifier.height(12.dp))
            ThemeSwitchRow(
                darkTheme = darkTheme,
                onDarkThemeChange = onDarkThemeChange,
                onLogout = onLogout
            )
            if (role == UserRole.Admin) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(onClick = onOpenEmployees) {
                        Text("Сотрудники")
                    }
                    Button(onClick = onAddItem) {
                        Text("Добавить товар")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            InventorySearchBar(
                query = state.query,
                history = state.searchHistory,
                onQueryChange = { onAction(InventoryAction.QueryChanged(it)) },
                onClearQuery = { onAction(InventoryAction.ClearQueryClicked) },
                onClearHistory = { onAction(InventoryAction.ClearHistoryClicked) },
                onHistoryClick = { onAction(InventoryAction.ItemClicked(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            InventoryContent(
                state = state,
                onRetry = { onAction(InventoryAction.RetryClicked) },
                onItemClick = {
                    onAction(InventoryAction.ItemClicked(it))
                    onOpenDetails(it.id)
                }
            )
        }
    }
}

@Composable
private fun InventoryHeader(role: UserRole) {
    Text(
        text = "Запасы",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Роль: ${role.title} · актуальные остатки предприятия",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ThemeSwitchRow(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Темная тема",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = darkTheme,
            onCheckedChange = onDarkThemeChange
        )
        TextButton(onClick = onLogout) {
            Text("Выйти")
        }
    }
}
