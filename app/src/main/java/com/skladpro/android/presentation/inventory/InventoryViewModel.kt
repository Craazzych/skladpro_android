package com.skladpro.android.presentation.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skladpro.android.data.AppContainer
import com.skladpro.android.domain.model.InventoryItem
import com.skladpro.android.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: InventoryRepository = AppContainer.inventoryRepository
) : ViewModel() {
    private val historyIds = mutableListOf<String>()
    private var allItems = emptyList<InventoryItem>()

    private val _uiState = MutableStateFlow(InventoryUiState(isLoading = true))
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun onAction(action: InventoryAction) {
        when (action) {
            is InventoryAction.QueryChanged -> updateQuery(action.query)
            InventoryAction.ClearQueryClicked -> updateQuery("")
            InventoryAction.RetryClicked -> refresh()
            InventoryAction.ClearHistoryClicked -> clearHistory()
            is InventoryAction.ItemClicked -> addToHistory(action.item)
        }
    }

    fun getItem(itemId: String): InventoryItem? {
        return allItems.firstOrNull { it.id == itemId }
    }

    fun applyStockOperation(itemId: String, quantityDelta: Double) {
        runRequest {
            replaceItem(repository.applyStockOperation(itemId, quantityDelta))
        }
    }

    fun addItem(item: InventoryItem, onSuccess: () -> Unit) {
        runRequest(onSuccess) {
            allItems = listOf(repository.createItem(item)).plus(allItems)
            updateVisibleItems()
        }
    }

    fun updateItem(item: InventoryItem, onSuccess: () -> Unit) {
        runRequest(onSuccess) {
            replaceItem(repository.updateItem(item))
        }
    }

    fun deleteItem(itemId: String, onSuccess: () -> Unit) {
        runRequest(onSuccess) {
            repository.deleteItem(itemId)
            allItems = allItems.filterNot { it.id == itemId }
            historyIds.remove(itemId)
            updateVisibleItems()
        }
    }

    fun updateDelivery(
        itemId: String,
        expectedDeliveryDate: String?,
        expectedDeliveryQuantity: Double?
    ) {
        runRequest {
            replaceItem(
                repository.updateDelivery(
                    itemId = itemId,
                    expectedDeliveryDate = expectedDeliveryDate,
                    expectedDeliveryQuantity = expectedDeliveryQuantity
                )
            )
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            runCatching { repository.getItems() }
                .onSuccess { items ->
                    allItems = items
                    updateVisibleItems()
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(isLoading = false, isError = true)
                    }
                }
        }
    }

    private fun runRequest(
        onSuccess: () -> Unit = {},
        request: suspend () -> Unit
    ) {
        viewModelScope.launch {
            runCatching { request() }
                .onSuccess { onSuccess() }
                .onFailure {
                    _uiState.update { state -> state.copy(isError = true) }
                }
        }
    }

    private fun replaceItem(updatedItem: InventoryItem) {
        allItems = allItems.map { item ->
            if (item.id == updatedItem.id) updatedItem else item
        }
        updateVisibleItems()
    }

    private fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
        updateVisibleItems()
    }

    private fun updateVisibleItems() {
        _uiState.update {
            it.copy(
                items = searchItems(it.query),
                searchHistory = historyItems(),
                isLoading = false,
                isError = false
            )
        }
    }

    private fun addToHistory(item: InventoryItem) {
        historyIds.remove(item.id)
        historyIds.add(0, item.id)
        if (historyIds.size > 10) historyIds.removeLast()
        _uiState.update { it.copy(searchHistory = historyItems()) }
    }

    private fun clearHistory() {
        historyIds.clear()
        _uiState.update { it.copy(searchHistory = emptyList()) }
    }

    private fun historyItems(): List<InventoryItem> {
        return historyIds.mapNotNull { id -> allItems.firstOrNull { it.id == id } }
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
