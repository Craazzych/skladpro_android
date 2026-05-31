package com.skladpro.android.presentation.inventory

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.skladpro.android.data.repository.DemoInventoryData
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.ui.theme.SkladProTheme

private val previewItems = DemoInventoryData.items

@Preview(
    name = "Главный экран запасов",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun InventoryListScreenPreview() {
    SkladProTheme {
        InventoryListScreen(
            role = UserRole.Worker,
            darkTheme = false,
            state = InventoryUiState(
                items = previewItems,
                searchHistory = previewItems.take(2)
            ),
            onAction = {},
            onDarkThemeChange = {},
            onOpenEmployees = {},
            onAddItem = {},
            onLogout = {},
            onOpenDetails = {}
        )
    }
}

@Preview(
    name = "Запасы - пустой поиск",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun InventoryListEmptyPreview() {
    SkladProTheme {
        InventoryListScreen(
            role = UserRole.Worker,
            darkTheme = false,
            state = InventoryUiState(
                query = "редуктор",
                items = emptyList()
            ),
            onAction = {},
            onDarkThemeChange = {},
            onOpenEmployees = {},
            onAddItem = {},
            onLogout = {},
            onOpenDetails = {}
        )
    }
}

@Preview(
    name = "Запасы - ошибка",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun InventoryListErrorPreview() {
    SkladProTheme {
        InventoryListScreen(
            role = UserRole.Admin,
            darkTheme = true,
            state = InventoryUiState(isError = true),
            onAction = {},
            onDarkThemeChange = {},
            onOpenEmployees = {},
            onAddItem = {},
            onLogout = {},
            onOpenDetails = {}
        )
    }
}
