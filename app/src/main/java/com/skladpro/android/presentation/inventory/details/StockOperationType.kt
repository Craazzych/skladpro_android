package com.skladpro.android.presentation.inventory.details

enum class StockOperationType(
    val title: String,
    val actionLabel: String,
    val sign: Double
) {
    Receipt(
        title = "Поступление",
        actionLabel = "Добавить",
        sign = 1.0
    ),
    WriteOff(
        title = "Списание",
        actionLabel = "Списать",
        sign = -1.0
    )
}
