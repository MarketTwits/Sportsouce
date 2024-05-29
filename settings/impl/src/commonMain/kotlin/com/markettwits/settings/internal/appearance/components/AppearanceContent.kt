package com.markettwits.settings.internal.appearance.components

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
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.settings.internal.appearance.store.AppearanceStore

@Composable
internal fun AppearanceContent(
    state: AppearanceStore.State,
    onClickGoBack: () -> Unit,
    onClickItem: (BottomBarUi) -> Unit,
) {
    Dialog(onDismissRequest = onClickGoBack) {
        BottomBarAppearanceContent(
            items = state.items,
            title = "Компактный вид меню",
            dismiss = onClickGoBack,
            selectedItem = {
                onClickItem(it)
            }
        )
    }
}

@Composable
private fun BottomBarAppearanceContent(
    modifier: Modifier = Modifier,
    items: List<BottomBarUi>,
    title: String,
    selectedItem: (BottomBarUi) -> Unit,
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
            fontFamily = FontNunito.bold(),
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
private fun FilterBody(
    modifier: Modifier = Modifier,
    items: List<BottomBarUi>,
    selectedItem: (BottomBarUi) -> Unit,
) = Column(
    modifier = modifier
        .fillMaxWidth()
) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            FilterPosition(
                title = item.title,
                selected = item.isSelected
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
            fontFamily = FontNunito.medium(),
            fontSize = 14.sp
        )
    }
}