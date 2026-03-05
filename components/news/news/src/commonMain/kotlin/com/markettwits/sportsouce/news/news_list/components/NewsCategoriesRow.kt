package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.news.common.model.NewsCategory

@Composable
fun NewsCategoriesRow(
    modifier: Modifier = Modifier,
    categories: List<NewsCategory>,
    selectedCategoryId: Int?,
    onSelectCategory: (Int?) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        item(key = "all") {
            NewsCategoryChip(
                text = "Все",
                isSelected = selectedCategoryId == null,
                onClick = { onSelectCategory(null) }
            )
        }

        items(categories, key = { it.id }) { category ->
            NewsCategoryChip(
                text = category.name,
                isSelected = category.id == selectedCategoryId,
                onClick = { onSelectCategory(category.id) }
            )
        }
    }
}

@Composable
private fun NewsCategoryChip(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val categoryColor = if (text.equals("Все", ignoreCase = true)) {
        resolveNewsCategoryColor("Новости")
    } else {
        resolveNewsCategoryColor(text)
    }.copy(alpha = 0.8f)
    Surface(
        modifier = modifier.clip(RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) categoryColor else MaterialTheme.colorScheme.primary,
        border = BorderStroke(1.5.dp, categoryColor.copy(alpha = if (isSelected) 0f else 0.55f)),
        tonalElevation = if (isSelected) 0.dp else 2.dp,
        shadowElevation = if (isSelected) 0.dp else 2.dp
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(animationSpec = tween(durationMillis = 220)) + expandHorizontally(
                    animationSpec = tween(
                        durationMillis = 220
                    )
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 160)) + shrinkHorizontally(
                    animationSpec = tween(
                        durationMillis = 160
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(RoundedCornerShape(50))
                        .background(categoryBadgeTextColor(categoryColor))
                )
            }
            Text(
                modifier = Modifier.padding(start = if (isSelected) 8.dp else 0.dp),
                text = text,
                color = if (isSelected) categoryBadgeTextColor(categoryColor) else MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.medium(),
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
