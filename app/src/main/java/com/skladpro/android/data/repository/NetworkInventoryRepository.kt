package com.skladpro.android.data.repository

import com.skladpro.android.data.remote.SkladProApi
import com.skladpro.android.data.remote.toDomain
import com.skladpro.android.data.remote.toRequest
import com.skladpro.android.domain.model.InventoryItem
import com.skladpro.android.domain.repository.InventoryRepository

class NetworkInventoryRepository(
    private val api: SkladProApi
) : InventoryRepository {
    override suspend fun getItems(): List<InventoryItem> =
        api.getItems().map { it.toDomain() }

    override suspend fun createItem(item: InventoryItem): InventoryItem =
        api.createItem(item.toRequest()).toDomain()

    override suspend fun updateItem(item: InventoryItem): InventoryItem =
        api.updateItem(item.id, item.toRequest()).toDomain()

    override suspend fun deleteItem(itemId: String) {
        api.deleteItem(itemId)
    }

    override suspend fun applyStockOperation(
        itemId: String,
        quantityDelta: Double
    ): InventoryItem = api.applyStockOperation(itemId, quantityDelta).toDomain()

    override suspend fun updateDelivery(
        itemId: String,
        expectedDeliveryDate: String?,
        expectedDeliveryQuantity: Double?
    ): InventoryItem = api
        .updateDelivery(itemId, expectedDeliveryDate, expectedDeliveryQuantity)
        .toDomain()
}
