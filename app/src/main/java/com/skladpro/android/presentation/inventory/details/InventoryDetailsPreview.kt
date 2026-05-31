package com.skladpro.android.presentation.inventory.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.skladpro.android.data.repository.FakeInventoryRepository
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.ui.theme.SkladProTheme

private val previewItem = FakeInventoryRepository().getItems().first()
private val lowStockPreviewItem = FakeInventoryRepository().getItems()[1]

@Preview(
    name = "Карточка запаса",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun InventoryDetailsPreview() {
    SkladProTheme {
        InventoryDetailsScreen(
            role = UserRole.Admin,
            item = previewItem,
            onBack = {},
            onStockOperation = {},
            onDeliverySaved = { _, _ -> },
            onEdit = {},
            onDelete = {}
        )
    }
}

@Preview(
    name = "Карточка запаса - мало",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun LowStockInventoryDetailsPreview() {
    SkladProTheme {
        InventoryDetailsScreen(
            role = UserRole.Worker,
            item = lowStockPreviewItem,
            onBack = {},
            onStockOperation = {},
            onDeliverySaved = { _, _ -> },
            onEdit = {},
            onDelete = {}
        )
    }
}
