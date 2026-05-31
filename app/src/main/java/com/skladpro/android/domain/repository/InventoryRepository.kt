package com.skladpro.android.domain.repository

import com.skladpro.android.domain.model.InventoryItem

interface InventoryRepository {
    fun getItems(): List<InventoryItem>

    fun searchItems(query: String): List<InventoryItem>
}
