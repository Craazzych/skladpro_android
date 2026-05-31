package com.skladpro.android.presentation.inventory

data class InventoryItemDraft(
    val name: String = "",
    val sku: String = "",
    val category: String = "",
    val unit: String = "",
    val quantityText: String = "",
    val minQuantityText: String = "",
    val department: String = "",
    val shelf: String = "",
    val cell: String = "",
    val deliveryDate: String = "",
    val deliveryQuantityText: String = ""
) {
    val deliveryDateError: String?
        get() = validateDeliveryDate(deliveryDate)

    val quantity: Double?
        get() = quantityText.parseDecimalOrNull()

    val minQuantity: Double?
        get() = minQuantityText.parseDecimalOrNull()

    val deliveryQuantity: Double?
        get() = deliveryQuantityText.parseDecimalOrNull()

    val canSave: Boolean
        get() = name.isNotBlank() &&
            sku.isNotBlank() &&
            category.isNotBlank() &&
            unit.isNotBlank() &&
            quantityIsValid &&
            minQuantityIsValid &&
            department.isNotBlank() &&
            shelf.isNotBlank() &&
            cell.isNotBlank() &&
            deliveryDateError == null &&
            deliveryFieldsAreValid

    private val quantityIsValid: Boolean
        get() = quantity?.let { it >= 0.0 } == true

    private val minQuantityIsValid: Boolean
        get() = minQuantity?.let { it >= 0.0 } == true

    private val deliveryFieldsAreValid: Boolean
        get() {
            val hasDate = deliveryDate.isNotBlank()
            val hasQuantity = deliveryQuantityText.isNotBlank()
            val quantity = deliveryQuantity
            return when {
                !hasDate && !hasQuantity -> true
                hasDate && quantity != null && quantity > 0.0 -> true
                else -> false
            }
        }
}

private fun String.parseDecimalOrNull(): Double? {
    return trim().replace(',', '.').toDoubleOrNull()
}
