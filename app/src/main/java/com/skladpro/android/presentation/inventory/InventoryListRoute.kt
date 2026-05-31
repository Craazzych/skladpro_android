package com.skladpro.android.presentation.inventory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skladpro.android.domain.model.UserRole

@Composable
fun InventoryListRoute(
    role: UserRole,
    darkTheme: Boolean,
    viewModel: InventoryViewModel = viewModel(),
    onDarkThemeChange: (Boolean) -> Unit,
    onOpenEmployees: () -> Unit,
    onAddItem: () -> Unit,
    onLogout: () -> Unit,
    onOpenDetails: (itemId: String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    InventoryListScreen(
        role = role,
        darkTheme = darkTheme,
        state = state,
        onAction = viewModel::onAction,
        onDarkThemeChange = onDarkThemeChange,
        onOpenEmployees = onOpenEmployees,
        onAddItem = onAddItem,
        onLogout = onLogout,
        onOpenDetails = onOpenDetails
    )
}
