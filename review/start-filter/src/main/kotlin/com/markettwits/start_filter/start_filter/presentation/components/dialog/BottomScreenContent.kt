package com.markettwits.start_filter.start_filter.presentation.components.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.components.BaseDivider
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi

@Composable
fun BottomScreenContent(
    modifier: Modifier = Modifier,
    filterUi: StartFilterUi.FilterGroupItemUi,
    onDismissRequest: () -> Unit,
    selectedItem: (String, Int) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(modifier = modifier
            .padding(10.dp)
            .clip(Shapes.medium)
            .background(Color.White)
        ) {
            Text(
                modifier = modifier.padding(10.dp),
                text = filterUi.label,
                color = SportSouceColor.SportSouceBlue,
                fontFamily = FontNunito.bold,
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