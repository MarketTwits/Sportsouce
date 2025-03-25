package com.markettwits.sportsouce.shop.item.presentation.components.panes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemDescriptionOrOptions
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemExtraOptions
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemImagePager
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemPriceRow
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemTitle

@Composable
internal fun ShopItemLargePane(
    modifier: Modifier = Modifier,
    item: ShopItem,
    options: List<ShopExtraOptions>,
    cartContent: @Composable (Modifier) -> Unit,
    onClickOption: (String) -> Unit,

    ) {
    Row(modifier = modifier.padding(top = 24.dp)) {
        ShopItemImagePager(
            modifier = Modifier
                .weight(1.5f),
            imageUrl = item.visual.imageUrl
        )
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.padding(8.dp))
            ShopItemTitle(title = item.visual.displayName)
            Spacer(modifier = Modifier.padding(8.dp))
            ShopItemPriceRow(price = item.price)
            Spacer(modifier = Modifier.padding(8.dp))
            ShopItemExtraOptions(
                extraOption = options,
                onClickOption = onClickOption
            )
            Spacer(modifier = Modifier.padding(8.dp))
            ShopItemDescriptionOrOptions(
                description = item.visual.description,
                options = item.options
            )
            cartContent(
                Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )
        }
    }
}