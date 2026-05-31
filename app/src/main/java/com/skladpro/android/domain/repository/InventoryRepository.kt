package com.skladpro.android.domain.repository

import com.skladpro.android.domain.model.InventoryItem

interface InventoryRepository {
    suspend fun getItems(): List<InventoryItem>

    suspend fun createItem(item: InventoryItem): InventoryItem

    suspend fun updateItem(item: InventoryItem): InventoryItem

    suspend fun deleteItem(itemId: String)

    suspend fun applyStockOperation(itemId: String, quantityDelta: Double): InventoryItem

    suspend fun updateDelivery(
        itemId: String,
        expectedDeliveryDate: String?,
        expectedDeliveryQuantity: Double?
    ): InventoryItem
}
