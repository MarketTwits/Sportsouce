package com.markettwits.random.presentation.components.review_menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun ReviewMenu(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        userScrollEnabled = false,
        modifier = modifier
            .height(130.dp)
            .fillMaxWidth(),
        columns = GridCells.Fixed(2),
    ) {
        items(menu()) {
            ReviewMenuButton(
                title = it.title,
                icon = it.icon,
                background = it.background,
                fontColor = it.fontColor
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
)

data class ReviewMenuItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val background: Color,
    val fontColor: Color
)