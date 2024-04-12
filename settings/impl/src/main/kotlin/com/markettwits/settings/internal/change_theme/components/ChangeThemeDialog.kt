package com.markettwits.settings.internal.change_theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.buttons.ButtonContentBase
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun ChangeThemeDialog(
    onDismissRequest: () -> Unit,
    items: List<ColorThemeUi>,
    onClick: (ColorThemeUi) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        ChangeThemeContent(
            items = items,
            title = "Темная тема",
            dismiss = onDismissRequest,
            selectedItem = {
                onClick(it)
            }
        )
    }
}

@Composable
private fun ChangeThemeContent(
    modifier: Modifier = Modifier,
    items: List<ColorThemeUi>,
    title: String,
    selectedItem: (ColorThemeUi) -> Unit,
    dismiss: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            modifier = modifier.padding(10.dp),
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis
        )
        FilterBody(items = items, selectedItem = selectedItem::invoke)
        ButtonContentBase(
            modifier = modifier.padding(10.dp),
            title = "Отмена",
            onClick = { dismiss() },
            borderStroke = BorderStroke(0.1.dp, MaterialTheme.colorScheme.outline)
        )
    }
}

@Composable
fun FilterBody(
    modifier: Modifier = Modifier,
    items: List<ColorThemeUi>,
    selectedItem: (ColorThemeUi) -> Unit,
) = Column(
    modifier = modifier
        .fillMaxWidth()
) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            FilterPosition(
                title = item.title,
                selected = item.checked
            ) {
                selectedItem(item)
            }
        }
    }
}

@Composable
private fun FilterPosition(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    )
    {
        RadioButton(
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.tertiary,
                unselectedColor = MaterialTheme.colorScheme.onTertiary,
            ),
            selected = selected,
            onClick = { onClick() })
        Text(
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.medium,
            fontSize = 14.sp
        )
    }
}