package com.markettwits.start.presentation.membres.compoent

import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable.Orientation
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.membres.StartMembersUi
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.LazyTableScrollDirection
import eu.wewox.lazytable.lazyTableDimensions
import eu.wewox.lazytable.rememberLazyTableState
import kotlinx.serialization.json.JsonNull.content


@Composable
fun StartMembersItemText(text: String) {
    Text(
        modifier = Modifier.padding(5.dp),
        style = LocalTextStyle.current.copy(lineBreak = LineBreak.Paragraph),
        text = text,
        color = SportSouceColor.SportSouceBlue,
        fontFamily = FontNunito.bold,
        fontSize = 12.sp,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TestTable(items : List<StartMembersUi>) {
    val columns = 5
    val rows = items.size
    if (items.isNotEmpty()){
        val configuration = LocalConfiguration.current
        val dimensionPortrait = lazyTableDimensions(110.dp, 50.dp)
        val screenWidth = configuration.screenWidthDp.dp
        val dimensionLandscape = lazyTableDimensions(screenWidth / 5, 60.dp)
        val dimension = when(configuration.orientation){
            Configuration.ORIENTATION_LANDSCAPE -> dimensionLandscape
            Configuration.ORIENTATION_PORTRAIT -> dimensionPortrait
            else -> dimensionLandscape
        }
        LazyTable(
            modifier = Modifier.fillMaxWidth(),
            dimensions = dimension,
            scrollDirection = LazyTableScrollDirection.BOTH
        ) {


            items(
                count = columns * rows,
                layoutInfo = {
                    LazyTableItem(
                        column = it % columns,
                        row = it / columns + 1,
                    )
                }
            ) { index ->
                Cell(pokemon = items[index / columns], column = index % columns)
            }
            items(
                count = columns,
                layoutInfo = {
                    LazyTableItem(
                        column = it % columns,
                        row = 0,
                    )
                },
            ) {
                HeaderCell(column = it)
            }

        }
    }
}

@Composable
private fun Cell(
    pokemon: StartMembersUi,
    column: Int,
) {
    val content = when (column) {
        0 -> "${pokemon.surname} ${pokemon.name}"
        1 -> pokemon.distance
        2 -> pokemon.team
        3 -> pokemon.group
        4 -> pokemon.city
        else -> error("Unknown column index: $column")
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(SportSouceColor.VeryLighBlue)
    ) {
        if (content.isNotEmpty()) {
            StartMembersItemText(content)
        }
    }
}
@Composable
private fun HeaderCell(column: Int) {
    val content = when (column) {
        0 -> "Участник"
        1 -> "Дистанция"
        2 -> "Команда"
        3 -> "Группа"
        4 -> "Город"
        else -> error("")
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(SportSouceColor.SportSouceLighBlue)
    ) {
        StartMembersItemText(content)
    }
}
