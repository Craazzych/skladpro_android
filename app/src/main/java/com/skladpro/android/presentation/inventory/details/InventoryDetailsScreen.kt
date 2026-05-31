package com.skladpro.android.presentation.inventory.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.InventoryItem
import com.skladpro.android.domain.model.UserRole

@Composable
fun InventoryDetailsScreen(
    role: UserRole,
    item: InventoryItem?,
    onBack: () -> Unit,
    onStockOperation: (quantityDelta: Double) -> Unit,
    onDeliverySaved: (date: String?, quantity: Double?) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var operationType by rememberSaveable { mutableStateOf<StockOperationType?>(null) }
    var isDeliveryDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("Назад")
            }

            if (item == null) {
                MissingItemState()
            } else {
                InventoryDetailsContent(
                    role = role,
                    item = item,
                    onReceiptClick = { operationType = StockOperationType.Receipt },
                    onWriteOffClick = { operationType = StockOperationType.WriteOff },
                    onDeliveryClick = { isDeliveryDialogVisible = true },
                    onEditClick = onEdit,
                    onDeleteClick = { isDeleteDialogVisible = true }
                )
            }
        }
    }

    operationType?.let { type ->
        StockOperationDialog(
            type = type,
            unit = item?.unit.orEmpty(),
            onDismiss = { operationType = null },
            onConfirm = { quantity ->
                onStockOperation(quantity * type.sign)
                operationType = null
            }
        )
    }

    if (isDeliveryDialogVisible && item != null) {
        DeliveryDialog(
            item = item,
            onDismiss = { isDeliveryDialogVisible = false },
            onConfirm = { date, quantity ->
                onDeliverySaved(date, quantity)
                isDeliveryDialogVisible = false
            }
        )
    }

    if (isDeleteDialogVisible) {
        DeleteItemDialog(
            onDismiss = { isDeleteDialogVisible = false },
            onConfirm = {
                isDeleteDialogVisible = false
                onDelete()
            }
        )
    }
}

@Composable
private fun InventoryDetailsContent(
    role: UserRole,
    item: InventoryItem,
    onReceiptClick: () -> Unit,
    onWriteOffClick: () -> Unit,
    onDeliveryClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Text(
        text = item.name,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "${role.title} · ${item.sku} · ${item.category}",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    StockStatusCard(item = item)
    DetailRows(item = item)

    RoleActions(
        role = role,
        onReceiptClick = onReceiptClick,
        onWriteOffClick = onWriteOffClick,
        onDeliveryClick = onDeliveryClick,
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick
    )
}

@Composable
private fun StockStatusCard(item: InventoryItem) {
    val statusText = if (item.requiresPurchase) {
        "Требует закупки"
    } else if (item.isLowStock) {
        "Низкий остаток"
    } else {
        "Остаток в норме"
    }
    val containerColor = if (item.requiresPurchase) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Текущий остаток: ${formatQuantity(item.quantity)} ${item.unit}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Минимальный остаток: ${formatQuantity(item.minQuantity)} ${item.unit}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = deliveryText(item),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun DetailRows(item: InventoryItem) {
    Surface(
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            DetailRow(label = "Артикул", value = item.sku)
            DetailRow(label = "Категория", value = item.category)
            DetailRow(label = "Единица измерения", value = item.unit)
            DetailRow(label = "Отдел", value = item.department)
            DetailRow(label = "Стеллаж", value = item.shelf)
            DetailRow(label = "Ячейка", value = item.cell)
            DetailRow(label = "ID позиции", value = item.id)
        }
    }
}

@Composable
private fun RoleActions(
    role: UserRole,
    onReceiptClick: () -> Unit,
    onWriteOffClick: () -> Unit,
    onDeliveryClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (role == UserRole.Admin) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onReceiptClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Поступление")
                }
                OutlinedButton(
                    onClick = onWriteOffClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Списание")
                }
            }
            OutlinedButton(
                onClick = onDeliveryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Указать поставку")
            }
            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Редактировать данные")
            }
            TextButton(
                onClick = onDeleteClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Удалить товар")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        OutlinedButton(
            onClick = onWriteOffClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сообщить о списании")
        }
    }
}

@Composable
private fun DeleteItemDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удалить товар?") },
        text = { Text("Позиция исчезнет из списка запасов.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun MissingItemState() {
    Spacer(modifier = Modifier.height(48.dp))
    Text(
        text = "Позиция не найдена",
        style = MaterialTheme.typography.titleMedium
    )
    Text(
        text = "Вернитесь к списку запасов и выберите товар повторно",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

private fun formatQuantity(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}

private fun deliveryText(item: InventoryItem): String {
    return if (item.hasExpectedDelivery) {
        "Поставка: ${item.expectedDeliveryDate}, ${formatQuantity(item.expectedDeliveryQuantity ?: 0.0)} ${item.unit}"
    } else {
        "Поставка: не запланирована"
    }
}
