package com.skladpro.android.presentation.inventory

import androidx.compose.runtime.Composable
import com.skladpro.android.domain.model.InventoryItem

@Composable
fun AddInventoryItemRoute(
    viewModel: InventoryViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    AddInventoryItemScreen(
        title = "Новый товар",
        subtitle = "Карточка будет добавлена в список запасов",
        actionLabel = "Добавить товар",
        onBack = onBack,
        onSave = { draft ->
            viewModel.addItem(draft.toInventoryItem())
            onSaved()
        }
    )
}

@Composable
fun EditInventoryItemRoute(
    itemId: String,
    viewModel: InventoryViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val item = viewModel.getItem(itemId)

    AddInventoryItemScreen(
        title = "Редактирование товара",
        subtitle = "Измените карточку, место хранения или поставку",
        actionLabel = "Сохранить изменения",
        initialDraft = item?.toDraft() ?: InventoryItemDraft(),
        onBack = onBack,
        onSave = { draft ->
            if (item != null) {
                viewModel.updateItem(draft.toInventoryItem(id = item.id))
            }
            onSaved()
        }
    )
}

private fun InventoryItemDraft.toInventoryItem(
    id: String = "item-${System.currentTimeMillis()}"
): InventoryItem {
    return InventoryItem(
        id = id,
        name = name.trim(),
        sku = sku.trim(),
        category = category.trim(),
        unit = unit.trim(),
        quantity = quantityText.replace(',', '.').toDoubleOrNull() ?: 0.0,
        minQuantity = minQuantityText.replace(',', '.').toDoubleOrNull() ?: 0.0,
        department = department.trim(),
        shelf = shelf.trim(),
        cell = cell.trim(),
        expectedDeliveryDate = deliveryDate.trim().ifBlank { null },
        expectedDeliveryQuantity = deliveryQuantityText.replace(',', '.').toDoubleOrNull()
    )
}

private fun InventoryItem.toDraft(): InventoryItemDraft {
    return InventoryItemDraft(
        name = name,
        sku = sku,
        category = category,
        unit = unit,
        quantityText = formatQuantity(quantity),
        minQuantityText = formatQuantity(minQuantity),
        department = department,
        shelf = shelf,
        cell = cell,
        deliveryDate = expectedDeliveryDate.orEmpty(),
        deliveryQuantityText = expectedDeliveryQuantity?.let(::formatQuantity).orEmpty()
    )
}

private fun formatQuantity(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}
