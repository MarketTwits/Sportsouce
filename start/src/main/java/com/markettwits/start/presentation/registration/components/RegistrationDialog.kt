package com.markettwits.start.presentation.registration.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.domain.StartStatement

@Composable
fun RegistrationDialog(
    modifier: Modifier = Modifier,
    values: List<String>,
    label: String,
    selected: String,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = modifier
                .padding(10.dp)
                .clip(Shapes.medium)
                .background(Color.White)
        ) {
            Text(
                modifier = modifier.padding(10.dp),
                text = label,
                color = SportSouceColor.SportSouceBlue,
                fontFamily = FontNunito.bold,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis
            )
            FilterBody(items = values, selected = selected, selectedItem = {
                onValueChange(it)
                onDismissRequest()
            })
        }
    }
}

@Composable
fun FilterBody(
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
            FilterPosition(
                item = item,
                checked = selected == item
            ) {
                selectedItem(item)
            }
        }
    }
}

@Composable
fun FilterPosition(
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
            colors = CheckboxDefaults.colors(checkedColor = SportSouceColor.SportSouceBlue),
            checked = checked,
            onCheckedChange = { onClick(item) })
        Text(
            text = item,
            color = SportSouceColor.SportSouceBlue,
            fontFamily = FontNunito.medium,
            fontSize = 14.sp
        )
    }
}