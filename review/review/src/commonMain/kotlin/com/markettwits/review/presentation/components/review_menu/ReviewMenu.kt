package com.markettwits.review.presentation.components.review_menu

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsRugby
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SportSouceColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReviewMenu(modifier: Modifier = Modifier, onClick : (Int) -> Unit) {
    LazyVerticalGrid(
        userScrollEnabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        columns = GridCells.Fixed(2),
    ) {
        items(menu()) {
            ReviewMenuButton(
                title = it.title,
                icon = it.icon,
                background = it.background,
                fontColor = it.fontColor,
                onClick = {onClick(it.id)},
            )
        }
    }
}

private fun menu() = listOf<ReviewMenuItem>(
    ReviewMenuItem(
        0,
        "Популярное",
        Icons.Filled.LocalFireDepartment,
        SportSouceColor.SportSouceStartEndedPink.copy(alpha = 0.1f),
        SportSouceColor.SportSouceStartEndedPink
    ),
    ReviewMenuItem(
        1,
        "Расписание",
        Icons.Filled.TableChart,
        SportSouceColor.SportSouceLighBlue.copy(alpha = 0.1f),
        SportSouceColor.SportSouceLighBlue
    ),
    ReviewMenuItem(
        2,
        "Рандом",
        Icons.Filled.AutoAwesome,
        SportSouceColor.SportSouceRegistryCommingSoonYellow.copy(alpha = 0.1f),
        SportSouceColor.SportSouceRegistryCommingSoonYellow
    ),
    ReviewMenuItem(
        3,
        "Фильтр",
        Icons.Filled.Settings,
        SportSouceColor.SportSouceRegistryOpenGreen.copy(alpha = 0.1f),
        SportSouceColor.SportSouceRegistryOpenGreen
    ),
    ReviewMenuItem(
        4,
        "Клуб",
        Icons.Filled.SportsRugby,
        SportSouceColor.SportSouceLightRed.copy(alpha = 0.1f),
        SportSouceColor.SportSouceLightRed
    )
)

@Immutable
data class ReviewMenuItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val background: Color,
    val fontColor: Color
)