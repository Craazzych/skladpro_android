package com.skladpro.android.presentation.inventory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.InventoryItem

@Composable
fun InventorySearchBar(
    query: String,
    history: List<InventoryItem>,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onClearHistory: () -> Unit,
    onHistoryClick: (InventoryItem) -> Unit
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Поиск по названию, артикулу или категории") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(
                    onClick = {
                        onClearQuery()
                        focusManager.clearFocus()
                    }
                ) {
                    Text("×")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
    )

    if (isFocused && query.isBlank() && history.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        SearchHistory(
            history = history,
            onHistoryClick = {
                onHistoryClick(it)
                onQueryChange(it.name)
                focusManager.clearFocus()
            },
            onClearHistory = onClearHistory
        )
    }
}

@Composable
private fun SearchHistory(
    history: List<InventoryItem>,
    onHistoryClick: (InventoryItem) -> Unit,
    onClearHistory: () -> Unit
) {
    Surface(
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "История поиска",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onClearHistory) {
                    Text("Очистить историю")
                }
            }
            history.forEach { item ->
                Text(
                    text = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHistoryClick(item) }
                        .padding(vertical = 6.dp)
                )
            }
        }
    }
}
