package com.markettwits.club.info.presentation.components.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
//import com.markettwits.core_ui.items.text.HtmlText
//import eu.wewox.lazytable.LazyTable

//import eu.wewox.lazytable.LazyTable
//import eu.wewox.lazytable.LazyTableItem

@Composable
fun ScheduleContent(
    modifier: Modifier = Modifier,
    schedule: List<ScheduleUi>
) {

    Column(modifier = modifier.padding(10.dp)) {
        Text(
            textAlign = TextAlign.Start,
            text = "Расписание тренировок",
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onPrimary
        )
//        ScheduleContentTable(
//            modifier = Modifier,
//            items = schedule
//        )

    }
}

//@Composable
//fun ScheduleContentTable(
//    modifier: Modifier = Modifier,
//    items: List<ScheduleUi>
//) {
//    val columns = 5
//    val rows = items.size
//    if (items.isNotEmpty()) {
//        LazyTable(
//            modifier = modifier
//        ) {
//            items(
//                count = columns * rows,
//                layoutInfo = {
////                    LazyTableItem(
////                        column = it % columns,
////                        row = it / columns + 1,
////                    )
//                }
//            ) { index ->
//                Cell(pokemon = items[index / columns], column = index % columns)
//            }
//            items(
//                count = columns,
//                layoutInfo = {
////                    LazyTableItem(
////                        column = it % columns,
////                        row = 0,
////                    )
//                },
//            ) {
//                HeaderCell(column = it)
//            }
//        }
//    }
//}
//
//@Composable
//private fun Cell(
//    pokemon: ScheduleUi,
//    column: Int,
//) {
//    if (pokemon is ScheduleUi.Row) {
//        val content = when (column) {
//            0 -> pokemon.kindOfSport
//            1 -> pokemon.address
//            2 -> pokemon.trainerFullName
//            3 -> pokemon.startDate
//            4 -> pokemon.description
//            else -> error("Unknown column index: $column")
//        }
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.fillMaxWidth()
//                .border(1.dp, MaterialTheme.colorScheme.outline)
//                .background(MaterialTheme.colorScheme.primary)
//        ) {
//            HtmlText(
//                modifier = Modifier.padding(2.dp),
//                text = content,
//                color = MaterialTheme.colorScheme.outline,
//                textAlign = TextAlign.Center,
//                fontFamily = FontNunito.bold(),
//                fontSize = 12.sp,
//                overflow = TextOverflow.Visible
//            )
//        }
//    }
//}
//
//@Composable
//private fun HeaderCell(column: Int) {
//    val content = when (column) {
//        0 -> "Вид спорта"
//        1 -> "Место проведения"
//        2 -> "Тренер"
//        3 -> "Дата и время"
//        4 -> "Описание"
//        else -> error("")
//    }
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxWidth()
//            .border(1.dp, MaterialTheme.colorScheme.outline)
//            .background(MaterialTheme.colorScheme.secondary)
//    ) {
//        Text(
//            modifier = Modifier.padding(2.dp),
//            text = content,
//            color = MaterialTheme.colorScheme.onSecondary,
//            textAlign = TextAlign.Center,
//            fontFamily = FontNunito.bold(),
//            fontSize = 12.sp,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}