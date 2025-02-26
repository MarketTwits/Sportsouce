package com.markettwits.start.presentation.result.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.result.model.MemberResult

@Composable
internal fun StartMembersResultTable(items: List<MemberResult>) {

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val columnWidth = remember {
            maxOf(maxWidth / 7, 100.dp)
        }

        val rowSingleModifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

        val rowHeaderModifier = Modifier.fillMaxWidth()

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 8.dp)
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState)
        ) {
            TableHeader(
                modifier = rowHeaderModifier,
                tableValue = listOf(
                    TableValue("Место", columnWidth / 2),
                    TableValue("Имя", columnWidth),
                    TableValue("Команда", columnWidth),
                    TableValue("Дистанция", columnWidth),
                    TableValue("Группа", columnWidth),
                    TableValue("Результат", columnWidth),
                    TableValue("Отставание", columnWidth)
                )
            )
            items.forEachIndexed { index, member ->
                val backGroundColor =
                    if (index % 2 == 0)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.background
                TableRow(
                    modifier = rowSingleModifier
                        .background(backGroundColor),
                    tableValue = listOf(
                        TableValue(member.place.toString(), columnWidth / 2),
                        TableValue(member.name, columnWidth),
                        TableValue(member.team, columnWidth),
                        TableValue(member.distance, columnWidth),
                        TableValue(member.group, columnWidth),
                        TableValue(member.result, columnWidth),
                        TableValue(member.shift, columnWidth),
                    ),
                )
            }
        }
        VerticalScrollbarCustom(verticalScrollState, Modifier.align(Alignment.CenterEnd))
        HorizontalScrollbarCustom(horizontalScrollState, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun TableHeader(
    modifier: Modifier = Modifier,
    tableValue: List<TableValue>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        tableValue.forEach { value ->
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(4.dp)
                    .width(value.columnWidth),
                text = value.value,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold(),
            )
        }
    }

}

@Composable
private fun TableRow(
    modifier: Modifier = Modifier,
    tableValue: List<TableValue>,
) {
    Row(
        modifier = modifier
    ) {
        tableValue.forEach { value ->
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(4.dp)
                    .width(value.columnWidth),
                text = value.value,
                fontFamily = FontNunito.regular(),
                fontSize = 14.sp,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private class TableValue(
    val value: String,
    val columnWidth: Dp,
)

@Composable
private fun VerticalScrollbarCustom(scrollState: ScrollState, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxHeight()
            .width(6.dp)
            .background(Color.LightGray.copy(alpha = 0.5f))
    ) {
        val proportion = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
        val scrollbarHeight = size.height * proportion

        drawRect(
            color = Color.DarkGray,
            topLeft = Offset(0f, scrollbarHeight),
            size = Size(size.width, 30.dp.toPx())
        )
    }
}

@Composable
private fun HorizontalScrollbarCustom(scrollState: ScrollState, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(Color.LightGray.copy(alpha = 0.5f))
    ) {
        val proportion = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
        val scrollbarWidth = size.width * proportion

        drawRect(
            color = Color.DarkGray,
            topLeft = Offset(scrollbarWidth, 0f),
            size = Size(30.dp.toPx(), size.height)
        )
    }
}