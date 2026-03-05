package com.markettwits.sportsouce.review.review.presentation.components.review_menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ReviewMenu(modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    val menuItems = menu()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(horizontal = 10.dp)
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in menuItems.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReviewMenuButton(
                    title = menuItems[i].title,
                    icon = menuItems[i].icon,
                    accentColor = menuItems[i].accentColor,
                    backgroundColor = menuItems[i].backgroundColor,
                    onClick = { onClick(menuItems[i].id) },
                    modifier = Modifier.weight(1f)
                )
                if (i + 1 < menuItems.size) {
                    ReviewMenuButton(
                        title = menuItems[i + 1].title,
                        icon = menuItems[i + 1].icon,
                        accentColor = menuItems[i + 1].accentColor,
                        backgroundColor = menuItems[i + 1].backgroundColor,
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
        accentColor = Color(0xFFFF5E2B),
        backgroundColor = Color(0xFFF5E5E4),
    ),
    ReviewMenuItem(
        1,
        "Новости",
        Icons.AutoMirrored.Filled.Article,
        accentColor = Color(0xFF2D93E6),
        backgroundColor = Color(0xFFDDECF9),
    ),
    ReviewMenuItem(
        2,
        "Клуб",
        Icons.Filled.TableChart,
        accentColor = Color(0xFFA24AC7),
        backgroundColor = Color(0xFFEBDDF4),
    ),
    ReviewMenuItem(
        3,
        "Поиск",
        Icons.Filled.Settings,
        accentColor = Color(0xFF0F9D95),
        backgroundColor = Color(0xFFD9ECEE),
    ),
    ReviewMenuItem(
        4,
        "Магазин",
        Icons.Filled.ShoppingCart,
        accentColor = Color(0xFF4CAF57),
        backgroundColor = Color(0xFFDDEADE),
    )
)

@Immutable
data class ReviewMenuItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val accentColor: Color,
    val backgroundColor: Color,
)
