package com.markettwits.sportsouce.review.review.presentation.components.review_menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing

@Composable
fun ReviewMenu(modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    val menuItems = menu()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(horizontal = 10.dp)
            .padding(vertical = SauceSpacing.medium),
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
                    shape = reviewMenuShape(i, menuItems.size),
                    onClick = { onClick(menuItems[i].id) },
                    modifier = Modifier.weight(1f)
                )
                if (i + 1 < menuItems.size) {
                    ReviewMenuButton(
                        title = menuItems[i + 1].title,
                        icon = menuItems[i + 1].icon,
                        accentColor = menuItems[i + 1].accentColor,
                        backgroundColor = menuItems[i + 1].backgroundColor,
                        shape = reviewMenuShape(i + 1, menuItems.size),
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

/** Only the exposed corners of the two-column menu carry the large card radius. */
internal fun reviewMenuShape(index: Int, count: Int): RoundedCornerShape {
    val outer = SauceShapes.card.topStart
    val inner = androidx.compose.foundation.shape.CornerSize(SauceSpacing.extraSmall)
    val isLastInLeftColumn = index % 2 == 0 && index + 2 >= count
    val isLastInRightColumn = index % 2 == 1 && index + 2 >= count
    return RoundedCornerShape(
        topStart = if (index == 0) outer else inner,
        topEnd = if (index == 1 || count == 1) outer else inner,
        bottomStart = if (isLastInLeftColumn) outer else inner,
        bottomEnd = if (isLastInRightColumn || index == count - 1) outer else inner,
    )
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
        "Фотоархив",
        Icons.Filled.PhotoLibrary,
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
