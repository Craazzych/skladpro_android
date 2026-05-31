package com.skladpro.android.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.InventoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryItemCard(
    item: InventoryItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${item.sku} · ${item.category}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (item.requiresPurchase) {
                    AssistChip(
                        onClick = {},
                        label = { Text("Требует закупки") }
                    )
                } else if (item.isLowStock) {
                    AssistChip(
                        onClick = {},
                        label = { Text("Низкий остаток") }
                    )
                }
            }
            Text(
                text = "Остаток: ${formatQuantity(item.quantity)} ${item.unit} · минимум ${formatQuantity(item.minQuantity)} ${item.unit}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Место: ${item.department}, стеллаж ${item.shelf}, ячейка ${item.cell}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatQuantity(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}
