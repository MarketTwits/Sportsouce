package com.markettwits.start.presentation.membres.list.compoents


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.markettwits.core_ui.items.theme.FontNunito
//import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
//import com.markettwits.start.presentation.membres.list.models.StartMembersUi
//import eu.wewox.lazytable.LazyTable
//import eu.wewox.lazytable.LazyTableItem
//import eu.wewox.lazytable.LazyTableScrollDirection
//import eu.wewox.lazytable.lazyTableDimensions
//
//
//
//@Composable
//fun TestTable(items: List<StartMembersUi>) {
//    val columns = 5
//    val rows = items.size
//    if (items.isNotEmpty()) {
//        val window = rememberScreenSizeInfo()
//        val dimensionPortrait = lazyTableDimensions(150.dp, 100.dp)
//        val dimensionLandscape = lazyTableDimensions(window.wDP / 5, 60.dp)
//
//        val dimension = if (window.wDP > 1500.dp)
//            dimensionLandscape else dimensionPortrait
//
//        LazyTable(
//            modifier = Modifier
//                .fillMaxWidth(),
//            dimensions = dimension,
//            scrollDirection = LazyTableScrollDirection.BOTH
//        ) {
//            items(
//                count = columns * rows,
//                layoutInfo = {
//                    LazyTableItem(
//                        column = it % columns,
//                        row = it / columns + 1,
//                    )
//                }
//            ) { index ->
//                Cell(pokemon = items[index / columns], column = index % columns)
//            }
//            items(
//                count = columns,
//                layoutInfo = {
//                    LazyTableItem(
//                        column = it % columns,
//                        row = 0,
//                    )
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
//    pokemon: StartMembersUi,
//    column: Int,
//) {
//    if (pokemon is StartMembersUi.Single) {
//        val content = when (column) {
//            0 -> "${pokemon.surname} ${pokemon.name}"
//            1 -> pokemon.distance
//            2 -> pokemon.team
//            3 -> pokemon.group
//            4 -> pokemon.city
//            else -> error("Unknown column index: $column")
//        }
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.fillMaxWidth()
//                .border(1.dp, MaterialTheme.colorScheme.outline)
//                .background(MaterialTheme.colorScheme.primary)
//        ) {
//            if (content.isNotEmpty()) {
//                Text(
//                    modifier = Modifier.padding(2.dp),
//                    text = content,
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    textAlign = TextAlign.Center,
//                    fontFamily = FontNunito.bold(),
//                    fontSize = 12.sp,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        }
//    }
//    if (pokemon is StartMembersUi.Team) {
//        TeamCell(pokemon, column)
//    }
//}
//
//@Composable
//private fun TeamCell(
//    pokemon: StartMembersUi.Team,
//    column: Int,
//) {
//    val content = when (column) {
//        0 -> teamLabel(pokemon.members)
//        1 -> pokemon.distance
//        2 -> pokemon.team
//        3 -> pokemon.group
//        4 -> pokemon.city
//        else -> error("Unknown column index: $column")
//    }
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxWidth()
//            .border(1.dp, MaterialTheme.colorScheme.outline)
//            .background(MaterialTheme.colorScheme.secondary)
//    ) {
//        if (content.isNotEmpty()) {
//            Text(
//                modifier = Modifier.padding(2.dp),
//                text = content,
//                color = MaterialTheme.colorScheme.onSecondary,
//                textAlign = TextAlign.Center,
//                fontFamily = FontNunito.bold(),
//                fontSize = 12.sp,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
//}
//
//private fun teamLabel(team: List<StartMembersUi.TeamMember>): String {
//    val members = team.map {
//        "${it.memberId} ${it.surname} ${it.name}"
//    }
//    return members.joinToString("\n")
//}
//
//@Composable
//private fun HeaderCell(column: Int) {
//    val content = when (column) {
//        0 -> "Участник"
//        1 -> "Дистанция"
//        2 -> "Команда"
//        3 -> "Группа"
//        4 -> "Город"
//        else -> error("")
//    }
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxWidth()
//            .border(1.dp, MaterialTheme.colorScheme.outline)
//            .background(MaterialTheme.colorScheme.secondary)
//    ) {
//        if (content.isNotEmpty()) {
//            Text(
//                modifier = Modifier.padding(2.dp),
//                text = content,
//                color = MaterialTheme.colorScheme.onSecondary,
//                textAlign = TextAlign.Center,
//                fontFamily = FontNunito.bold(),
//                fontSize = 12.sp,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
//}
