package com.markettwits.sportsouce.shop.item.presentation.components.panes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemDescriptionOrOptions
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemExtraOptions
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemImageContent
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemPriceRow
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemTitle

@Composable
internal fun ShopItemCompactPane(
    modifier: Modifier = Modifier,
    item: ShopItem,
    options: List<ShopExtraOptions>,
    onClickOption: (String) -> Unit,
) {
    ShopItemImageContent(imageUrl = item.visual.imageUrl)
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        ShopItemTitle(title = item.visual.displayName)
        ShopItemPriceRow(price = item.price)
        ShopItemExtraOptions(
            extraOption = options,
            onClickOption = onClickOption
        )
        ShopItemDescriptionOrOptions(
            description = item.visual.description,
            options = item.options
        )
    }
}