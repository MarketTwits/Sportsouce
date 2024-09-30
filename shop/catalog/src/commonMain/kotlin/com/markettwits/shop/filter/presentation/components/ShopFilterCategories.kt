package com.markettwits.shop.filter.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.catalog.domain.models.ShopCategoryItem


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShopFilterCategories(
    categories: List<ShopCategoryItem>,
    currentPath: List<ShopCategoryItem>,
    onClickCategory: (ShopCategoryItem?) -> Unit,
) {

    val currentCategories = if (currentPath.isEmpty()) {
        categories
    } else {
        currentPath.last().children
    }

    Column {
        var currentCategory by remember { mutableStateOf("") }
        currentCategory =
            if (currentPath.isNotEmpty()) currentPath.last().title else "Все категории"
        Text(
            text = currentCategory,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            lineHeight = 18.sp,
            softWrap = true,
            textAlign = TextAlign.Start,
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.tertiary
        )

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            if (currentPath.isNotEmpty()) {
                Text(
                    modifier = Modifier.clickable {
                        onClickCategory(null)
                    },
                    text = "Все категории",
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            currentPath.forEachIndexed { index, category ->
                Text(
                    text = " > ",
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    modifier = Modifier.clickable {
                        onClickCategory(category)
                    },
                    text = category.title,
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        FlowRow(
            maxItemsInEachRow = 4,
            verticalArrangement = Arrangement.Center,
        ) {
            currentCategories.forEach { value ->
                OutlinedCard(
                    modifier = Modifier.padding(8.dp),
                    elevation = CardDefaults.elevatedCardElevation(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    shape = Shapes.medium,
                    onClick = {
                        onClickCategory(value)
                    }, content = {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = value.title,
                            maxLines = 1,
                            fontSize = 12.sp,
                            fontFamily = FontNunito.medium(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    })
            }
        }
    }
}
