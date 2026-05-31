package com.skladpro.android.data.repository

import com.skladpro.android.domain.model.InventoryItem
import com.skladpro.android.domain.repository.InventoryRepository

class FakeInventoryRepository : InventoryRepository {
    private val items = listOf(
        InventoryItem(
            id = "1",
            name = "Стальные болты М8",
            sku = "BLT-M8-001",
            category = "Крепеж",
            unit = "шт.",
            quantity = 1240.0,
            minQuantity = 300.0,
            department = "Производственный склад",
            shelf = "A-01",
            cell = "A-01-03"
        ),
        InventoryItem(
            id = "2",
            name = "Промышленное масло ISO VG 46",
            sku = "OIL-046-010",
            category = "Расходные материалы",
            unit = "л",
            quantity = 42.0,
            minQuantity = 50.0,
            department = "Расходные материалы",
            shelf = "B-04",
            cell = "B-04-02",
            expectedDeliveryDate = "15.06.2026",
            expectedDeliveryQuantity = 80.0
        ),
        InventoryItem(
            id = "3",
            name = "Подшипник 6204",
            sku = "BRG-6204",
            category = "Комплектующие",
            unit = "шт.",
            quantity = 86.0,
            minQuantity = 20.0,
            department = "Комплектующие",
            shelf = "C-02",
            cell = "C-02-07"
        ),
        InventoryItem(
            id = "4",
            name = "Картонная коробка 400x300",
            sku = "BOX-400-300",
            category = "Упаковка",
            unit = "шт.",
            quantity = 180.0,
            minQuantity = 200.0,
            department = "Упаковка",
            shelf = "D-01",
            cell = "D-01-01",
            expectedDeliveryDate = "03.06.2026",
            expectedDeliveryQuantity = 500.0
        ),
        InventoryItem(
            id = "5",
            name = "Защитные перчатки",
            sku = "SAFE-GLOVES",
            category = "Средства защиты",
            unit = "пар",
            quantity = 64.0,
            minQuantity = 30.0,
            department = "Охрана труда",
            shelf = "E-03",
            cell = "E-03-04"
        )
    )

    override fun getItems(): List<InventoryItem> = items

    override fun searchItems(query: String): List<InventoryItem> {
        val cleanQuery = query.trim()
        return if (cleanQuery.isBlank()) {
            items
        } else {
            items.filter { item ->
                item.name.contains(cleanQuery, ignoreCase = true) ||
                    item.sku.contains(cleanQuery, ignoreCase = true) ||
                    item.category.contains(cleanQuery, ignoreCase = true)
            }
        }
    }
}
