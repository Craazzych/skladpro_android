package com.skladpro.android.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.InventoryItem

@Composable
fun InventoryItemsList(
    items: List<InventoryItem>,
    onItemClick: (InventoryItem) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 20.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            InventoryItemCard(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}
