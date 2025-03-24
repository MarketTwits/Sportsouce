package com.markettwits.review.presentation.components.review_menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
fun ReviewMenu(modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    val menuItems = menu()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        for (i in menuItems.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                ReviewMenuButton(
                    title = menuItems[i].title,
                    icon = menuItems[i].icon,
                    background = menuItems[i].background,
                    fontColor = menuItems[i].fontColor,
                    onClick = { onClick(menuItems[i].id) },
                    modifier = Modifier.weight(1f)
                )
                if (i + 1 < menuItems.size) {
                    ReviewMenuButton(
                        title = menuItems[i + 1].title,
                        icon = menuItems[i + 1].icon,
                        background = menuItems[i + 1].background,
                        fontColor = menuItems[i + 1].fontColor,
                        onClick = { onClick(menuItems[i + 1].id) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
private fun menu() = listOf<ReviewMenuItem>(
    ReviewMenuItem(
        0,
        "Популярные",
        Icons.Filled.LocalFireDepartment,
        SportSouceColor.SportSouceStartEndedPink.copy(alpha = 0.1f),
        SportSouceColor.SportSouceStartEndedPink
    ),
//    ReviewMenuItem(
//        1,
//        "Расписание",
//        Icons.Filled.TableChart,
//        SportSouceColor.SportSouceLighBlue.copy(alpha = 0.1f),
//        SportSouceColor.SportSouceLighBlue
//    ),
    ReviewMenuItem(
        2,
        "Клуб",
        Icons.Filled.TableChart,
        SportSouceColor.SportSouceLightRed.copy(alpha = 0.1f),
        SportSouceColor.SportSouceLightRed
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
        "Магазин",
        Icons.Filled.ShoppingCart,
        SportSouceColor.SportSouceRegistryCommingSoonYellow.copy(alpha = 0.1f),
        SportSouceColor.SportSouceRegistryCommingSoonYellow
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