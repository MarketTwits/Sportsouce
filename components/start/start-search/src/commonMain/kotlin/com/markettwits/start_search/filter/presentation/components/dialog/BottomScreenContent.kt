package com.markettwits.start_search.filter.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start_search.filter.presentation.component.StartFilterUi

@Composable
fun BottomScreenContent(
    modifier: Modifier = Modifier,
    filterUi: StartFilterUi.FilterGroupItemUi,
    onDismissRequest: () -> Unit,
    selectedItem: (String, Int) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = modifier
                .padding(10.dp)
                .clip(Shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = modifier.padding(10.dp),
                text = filterUi.label,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold(),
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis
            )
            FilterBody(filterUi = filterUi, selectedItem = selectedItem::invoke)
        }
    }
}

@Composable
fun FilterBody(
    modifier: Modifier = Modifier,
    filterUi: StartFilterUi.FilterGroupItemUi,
    selectedItem: (String, Int) -> Unit,
) = Column(
    modifier = modifier
        .fillMaxWidth()
) {
    LazyColumn {
        itemsIndexed(filterUi.list) { index, filterItemUi ->
            FilterPosition(
                item = filterItemUi,
                checked = filterUi.checkSelected(filterItemUi)
            ) {
                selectedItem.invoke(filterItemUi, index)
            }
        }
    }
}

@Composable
private fun FilterPosition(
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