package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito

@Suppress("NonSkippableComposable")
@Composable
internal fun ItemsDialog(
    modifier: Modifier = Modifier,
    values: List<String>,
    label: String,
    selected: String,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit
) {
    var textFiledValue by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = modifier
                .padding(10.dp)
                .clip(Shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            ItemsDialogHeader(
                modifier = modifier.padding(horizontal = 10.dp),
                label = label,
                onDismissRequest = { onDismissRequest() }
            )
            if (values.size > 10) {
                OutlinedTextFieldBase(
                    modifier = modifier
                        .padding(horizontal = 10.dp),
                    value = textFiledValue,
                    label = "Поиск",
                    onValueChange = {
                        textFiledValue = it
                    }
                )
            }
            ItemsDialogFilterBody(
                items = filterAndSortList(values, textFiledValue),
                selected = selected,
                selectedItem = {
                    onValueChange(it)
                    onDismissRequest()
                })
        }
    }
}

private fun filterAndSortList(list: List<String>, keyword: String): List<String> {
    val filteredList = list.filter { it.contains(keyword, ignoreCase = true) }
    return filteredList.sorted()
}

@Composable
private fun ItemsDialogHeader(modifier: Modifier, label: String, onDismissRequest: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(
            onClick = { onDismissRequest() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Suppress("NonSkippableComposable")
@Composable
private fun ItemsDialogFilterBody(
    modifier: Modifier = Modifier,
    items: List<String>,
    selected: String,
    selectedItem: (String) -> Unit,
) = Column(
    modifier = modifier
        .fillMaxWidth()
) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            ItemsDialogFilterPosition(
                item = item,
                checked = selected == item
            ) {
                selectedItem(item)
            }
        }
        if (items.isEmpty()) {
            item {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp),
                    text = "Список пуст",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.medium(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ItemsDialogFilterPosition(
    item: String,
    checked: Boolean,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(item)
            },
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Checkbox(
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.tertiary,
                checkmarkColor = MaterialTheme.colorScheme.onTertiary
            ),
            checked = checked,
            onCheckedChange = { onClick(item) })
        Text(
            text = item,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.medium(),
            fontSize = 14.sp
        )
    }
}