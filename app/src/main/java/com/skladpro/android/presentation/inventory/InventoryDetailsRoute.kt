package com.skladpro.android.presentation.inventory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.presentation.inventory.details.InventoryDetailsScreen

@Composable
fun InventoryDetailsRoute(
    itemId: String,
    role: UserRole,
    viewModel: InventoryViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val item = state.items.firstOrNull { it.id == itemId } ?: viewModel.getItem(itemId)

    InventoryDetailsScreen(
        role = role,
        item = item,
        onBack = onBack,
        onStockOperation = { quantityDelta ->
            viewModel.applyStockOperation(itemId, quantityDelta)
        },
        onDeliverySaved = { date, quantity ->
            viewModel.updateDelivery(itemId, date, quantity)
        },
        onEdit = onEdit,
        onDelete = {
            viewModel.deleteItem(itemId, onSuccess = onDeleted)
        }
    )
}
