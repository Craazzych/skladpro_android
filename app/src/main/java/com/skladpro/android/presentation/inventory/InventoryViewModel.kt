package com.skladpro.android.presentation.inventory

import androidx.lifecycle.ViewModel
import com.skladpro.android.data.repository.FakeInventoryRepository
import com.skladpro.android.domain.model.InventoryItem
import com.skladpro.android.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InventoryViewModel(
    private val repository: InventoryRepository = FakeInventoryRepository()
) : ViewModel() {
    private val historyIds = mutableListOf<String>()
    private var allItems = repository.getItems()

    private val _uiState = MutableStateFlow(
        InventoryUiState(items = allItems)
    )
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    fun onAction(action: InventoryAction) {
        when (action) {
            is InventoryAction.QueryChanged -> updateQuery(action.query)
            InventoryAction.ClearQueryClicked -> updateQuery("")
            InventoryAction.RetryClicked -> updateQuery(_uiState.value.query)
            InventoryAction.ClearHistoryClicked -> clearHistory()
            is InventoryAction.ItemClicked -> addToHistory(action.item)
            InventoryAction.SimulateErrorClicked -> showError()
        }
    }

    fun getItem(itemId: String): InventoryItem? {
        return allItems.firstOrNull { it.id == itemId }
    }

    fun applyStockOperation(itemId: String, quantityDelta: Double) {
        if (quantityDelta == 0.0) return

        allItems = allItems.map { item ->
            if (item.id == itemId) {
                val updatedQuantity = (item.quantity + quantityDelta).coerceAtLeast(0.0)
                item.copy(quantity = updatedQuantity)
            } else {
                item
            }
        }
        updateQuery(_uiState.value.query)
        _uiState.update {
            it.copy(searchHistory = historyItems())
        }
    }

    fun addItem(item: InventoryItem) {
        allItems = listOf(item).plus(allItems)
        updateQuery(_uiState.value.query)
    }

    fun updateItem(updatedItem: InventoryItem) {
        allItems = allItems.map { item ->
            if (item.id == updatedItem.id) {
                updatedItem
            } else {
                item
            }
        }
        updateQuery(_uiState.value.query)
        _uiState.update {
            it.copy(searchHistory = historyItems())
        }
    }

    fun deleteItem(itemId: String) {
        allItems = allItems.filterNot { it.id == itemId }
        historyIds.remove(itemId)
        updateQuery(_uiState.value.query)
        _uiState.update {
            it.copy(searchHistory = historyItems())
        }
    }

    fun updateDelivery(
        itemId: String,
        expectedDeliveryDate: String?,
        expectedDeliveryQuantity: Double?
    ) {
        allItems = allItems.map { item ->
            if (item.id == itemId) {
                item.copy(
                    expectedDeliveryDate = expectedDeliveryDate,
                    expectedDeliveryQuantity = expectedDeliveryQuantity
                )
            } else {
                item
            }
        }
        updateQuery(_uiState.value.query)
        _uiState.update {
            it.copy(searchHistory = historyItems())
        }
    }

    private fun updateQuery(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                items = searchItems(query),
                isLoading = false,
                isError = false
            )
        }
    }

    private fun addToHistory(item: InventoryItem) {
        historyIds.remove(item.id)
        historyIds.add(0, item.id)
        if (historyIds.size > 10) {
            historyIds.removeLast()
        }
        _uiState.update {
            it.copy(searchHistory = historyItems())
        }
    }

    private fun clearHistory() {
        historyIds.clear()
        _uiState.update {
            it.copy(searchHistory = emptyList())
        }
    }

    private fun showError() {
        _uiState.update {
            it.copy(
                items = emptyList(),
                isLoading = false,
                isError = true
            )
        }
    }

    private fun historyItems(): List<InventoryItem> {
        return historyIds.mapNotNull { id ->
            allItems.firstOrNull { it.id == id }
        }
    }

    private fun searchItems(query: String): List<InventoryItem> {
        val cleanQuery = query.trim()
        return if (cleanQuery.isBlank()) {
            allItems
        } else {
            allItems.filter { item ->
                item.name.contains(cleanQuery, ignoreCase = true) ||
                    item.sku.contains(cleanQuery, ignoreCase = true) ||
                    item.category.contains(cleanQuery, ignoreCase = true)
            }
        }
    }
}
