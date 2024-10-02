package com.markettwits.shop.catalog.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.filter.domain.models.ShopCategoryItem

@Composable
internal fun ShopCategories(
    modifier: Modifier = Modifier,
    shopCategoryItem: List<ShopCategoryItem>,
    selectedCategoryItemId: Int,
    onItemClick: (ShopCategoryItem) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        shopCategoryItem.forEach {
            ShopCategoryItem(
                modifier = Modifier.padding(5.dp),
                isSelected = selectedCategoryItemId == it.id,
                shopCategoryItem = it,
                onClick = { onItemClick(it) })
        }
    }
}

@Composable
private fun ShopCategoryItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    shopCategoryItem: ShopCategoryItem,
    onClick: () -> Unit,
) {
    val buttonColor =
        if (isSelected) ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
        else
            ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.tertiary
            )

    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        shape = Shapes.large,
        colors = buttonColor
    ) {
        Text(
            text = shopCategoryItem.title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            lineHeight = 18.sp,
            softWrap = true,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
        )
    }
}