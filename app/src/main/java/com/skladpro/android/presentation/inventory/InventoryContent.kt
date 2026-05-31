package com.skladpro.android.presentation.inventory

import androidx.compose.runtime.Composable
import com.skladpro.android.domain.model.InventoryItem

@Composable
fun InventoryContent(
    state: InventoryUiState,
    onRetry: () -> Unit,
    onItemClick: (InventoryItem) -> Unit
) {
    when {
        state.isLoading -> LoadingPlaceholder()
        state.isError -> ErrorPlaceholder(onRetry = onRetry)
        state.isEmptyResult -> EmptyPlaceholder(query = state.query)
        else -> InventoryItemsList(
            items = state.items,
            onItemClick = onItemClick
        )
    }
}
