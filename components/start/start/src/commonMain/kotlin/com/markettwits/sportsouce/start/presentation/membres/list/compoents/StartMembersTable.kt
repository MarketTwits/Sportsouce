package com.markettwits.sportsouce.start.presentation.membres.list.compoents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.markettwits.sportsouce.start.presentation.membres.list.models.StartMembersUi

@Composable
internal fun StartMembersTable(items: List<StartMembersUi>) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val columnWidth = remember {
            maxOf(maxWidth / 6, 100.dp)
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 8.dp)
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TableHeader("Участник(и)", columnWidth)
                TableHeader("Дистанция", columnWidth)
                TableHeader("Команда", columnWidth)
                TableHeader("Группа", columnWidth)
                TableHeader("Город", columnWidth)
            }

            items.forEachIndexed { index, member ->

                val backGroundColor =
                    if (index % 2 == 0)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.background

                when (member) {
                    is StartMembersUi.Single -> {
                        TableRowSingle(
                            modifier = Modifier
                                .background(backGroundColor),
                            data = listOf(
                                "${member.name} ${member.surname}",
                                member.distance,
                                member.team,
                                member.group,
                                member.city
                            ),
                            columnWidth = columnWidth
                        )
                    }

                    is StartMembersUi.Team -> {

                        val data = member.members.map {
                            listOf(
                                "${it.name} ${it.surname}",
                                member.distance,
                                member.team,
                                member.group,
                                member.city
                            )
                        }

                        TableRowTeam(
                            modifier = Modifier.background(backGroundColor),
                            data = data,
                            columnWidth = columnWidth
                        )

                    }
                }
            }
        }
        VerticalScrollbarCustom(verticalScrollState, Modifier.align(Alignment.CenterEnd))
        HorizontalScrollbarCustom(horizontalScrollState, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun TableHeader(text: String, columnWidth: Dp) {
    Text(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .padding(4.dp)
            .width(columnWidth),
        text = text,
        fontSize = 14.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        fontFamily = FontNunito.bold(),
    )
}

@Composable
private fun TableRowSingle(
    modifier: Modifier = Modifier,
    data: List<String>,
    columnWidth: Dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        data.forEach { text ->
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(4.dp)
                    .width(columnWidth),
                text = text,
                fontFamily = FontNunito.regular(),
                fontSize = 14.sp,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TableRowTeam(
    modifier: Modifier = Modifier,
    data: List<List<String>>,
    columnWidth: Dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Column {
            data.forEach { text ->
                Row {
                    text.forEach { value ->
                        Text(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .padding(4.dp)
                                .width(columnWidth),
                            text = value,
                            fontFamily = FontNunito.regular(),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis

                        )
                    }
                }

            }
        }
    }
}

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