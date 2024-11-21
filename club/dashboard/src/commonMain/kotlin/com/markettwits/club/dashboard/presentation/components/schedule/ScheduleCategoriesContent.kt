package com.markettwits.club.dashboard.presentation.components.schedule

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import kotlin.math.truncate

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ScheduleCategoriesContent(
    modifier: Modifier = Modifier,
    categories : List<String>,
    selectedCategory : String,
    onClick : (String) -> Unit
) {
    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 3
    ) {
        categories.forEach {
            ScheduleButton(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f),
                isSelected = selectedCategory == it,
                title = it,
                onClick = { onClick(it) }
            )
        }
    }
}

@Composable
private fun ScheduleButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    title: String,
    onClick: () -> Unit
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
    ButtonContentBase(
        modifier = modifier,
        containerColor = containerColor,
        onClick = {
            onClick()
        },
        showContent = true,
        shape = Shapes.small,
        content = {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = title,
                fontSize = 12.sp,
                overflow = TextOverflow.Clip,
                fontFamily = FontNunito.bold(),
                color = Color.White
            )
        }
    )
}