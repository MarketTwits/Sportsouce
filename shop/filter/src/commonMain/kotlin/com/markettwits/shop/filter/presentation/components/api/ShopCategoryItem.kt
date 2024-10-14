package com.markettwits.shop.filter.presentation.components.api


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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud_shop.model.common.OptionInfo
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.presentation.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.filter.domain.models.ShopCategoryItem

@Composable
fun ShopCategories(
    modifier: Modifier = Modifier,
    selectedCategoriesPath: List<ShopCategoryItem>,
    onClickCategory: () -> Unit,
) {
    val currentCategoriesPath by remember {
        mutableStateOf(selectedCategoriesPath.filter { it.children.isNotEmpty() })
    }
//    val currentOptionsPath by remember {
//        mutableStateOf(selectedOptionsPath.filter { it.values?.isNotEmpty() ?: false })
//    }
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        currentCategoriesPath.forEach {
            ShopCategoryItem(
                isSelected = true,
                title = it.title,
                onClick =  { onClickCategory() })
        }
//        currentOptionsPath.forEach {
//            ShopCategoryItem(
//                isSelected = true,
//                title = it.name,
//                onClick = {onClickCategory()}
//            )
//        }
    }
}

@Composable
private fun ShopCategoryItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    title: String,
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
        shape = Shapes.medium,
        elevation = ButtonDefaults.elevatedButtonElevation(),
        colors = buttonColor
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
    }
}