package com.skladpro.android.presentation.inventory

import com.skladpro.android.domain.model.InventoryItem

data class InventoryUiState(
    val query: String = "",
    val items: List<InventoryItem> = emptyList(),
    val searchHistory: List<InventoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
) {
    val isEmptyResult: Boolean
        get() = !isLoading && !isError && query.isNotBlank() && items.isEmpty()
}
