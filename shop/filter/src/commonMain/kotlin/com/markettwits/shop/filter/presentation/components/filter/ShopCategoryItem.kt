package com.markettwits.shop.filter.presentation.components.filter


import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import com.markettwits.shop.filter.domain.models.ShopOptionInfo

@Composable
internal fun ShopFilterSelectedParams(
    modifier: Modifier = Modifier,
    selectedCategoriesPath: List<ShopCategoryItem>,
    selectedOptions: List<ShopOptionInfo.Value>,
    onClickCategory: (ShopCategoryItem) -> Unit,
    onClickOptionValue: (ShopOptionInfo.Value) -> Unit,
) {
    val selectedCategory =
        if (selectedCategoriesPath.isNotEmpty()) {
            selectedCategoriesPath.filter { it.children.isNotEmpty() }
            selectedCategoriesPath.last()
        } else {
            null
        }

    val isShow = selectedCategoriesPath.isNotEmpty() || selectedOptions.isNotEmpty()

    if (isShow) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (selectedCategory != null) {
                ShopCategoryItem(
                    title = selectedCategory.title,
                    onClick = { onClickCategory(selectedCategory) }
                )
            }
            selectedOptions.forEach { option ->
                ShopCategoryItem(
                    title = option.name,
                    onClick = { onClickOptionValue(option) }
                )
            }
        }
    }

}

@Composable
private fun ShopCategoryItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    val buttonColor = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    )
    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable(onClick = onClick),
        shape = Shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors = buttonColor
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                lineHeight = 14.sp,
                softWrap = true,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
            )
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.Close,
                tint = buttonColor.contentColor,
                contentDescription = "close $title"
            )
        }
    }
}