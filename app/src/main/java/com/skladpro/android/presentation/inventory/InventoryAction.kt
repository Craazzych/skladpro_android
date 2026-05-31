package com.skladpro.android.presentation.inventory

import com.skladpro.android.domain.model.InventoryItem

sealed interface InventoryAction {
    data class QueryChanged(val query: String) : InventoryAction
    data object ClearQueryClicked : InventoryAction
    data object RetryClicked : InventoryAction
    data object ClearHistoryClicked : InventoryAction
    data class ItemClicked(val item: InventoryItem) : InventoryAction
}
