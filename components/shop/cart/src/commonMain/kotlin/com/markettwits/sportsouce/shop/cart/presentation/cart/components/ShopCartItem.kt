package com.markettwits.sportsouce.shop.cart.presentation.cart.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.commerce.*
import com.markettwits.core_ui.items.theme.*
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart

@Composable
internal fun ShopCartItem(modifier: Modifier = Modifier, shopCartItemCart: ShopItemCart,
    onClickIncrease: (ShopItemCart) -> Unit, onClickDecrease: (ShopItemCart) -> Unit,
    onClickDelete: (ShopItemCart) -> Unit, onItemClick: (ShopItemCart) -> Unit) {
    val item = shopCartItemCart.item
    var expanded by rememberSaveable { mutableStateOf(false) }
    val intent = rememberIntentActionByPlatform()
    OnBackgroundCard(modifier, onClick = { onItemClick(shopCartItemCart) }) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val menu: @Composable () -> Unit = {
                Box {
                    IconButton({ expanded = true }, Modifier.size(SauceSizes.touchTarget)) {
                        Icon(Icons.Default.MoreVert, "Действия с товаром", tint = SauceTheme.colors.textSecondary)
                    }
                    ShopCartDropDownMenu(expanded = expanded, onClickDismiss = { expanded = false },
                        onClickDeleteItem = { expanded = false; onClickDelete(shopCartItemCart) },
                        onClickShare = { expanded = false; intent.sharePlainText(item.fullPathUrl) },
                        onClickCopyArticul = { expanded = false; intent.copyToSystemBuffer(item.code) })
                }
            }
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                if (maxWidth < 260.dp * LocalDensity.current.fontScale) Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth()) {
                        SauceProductImage(item.visual.imageUrl.firstOrNull(), Modifier.size(72.dp, 96.dp).clip(SauceShapes.field))
                        Spacer(Modifier.weight(1f))
                        menu()
                    }
                    Text(item.visual.displayName, style = MaterialTheme.typography.bodyMedium,
                        color = SauceTheme.colors.textPrimary, maxLines = 3, overflow = TextOverflow.Ellipsis)
                } else Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SauceProductImage(item.visual.imageUrl.firstOrNull(), Modifier.size(72.dp, 96.dp).clip(SauceShapes.field))
                    Text(item.visual.displayName, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium,
                        color = SauceTheme.colors.textPrimary, maxLines = 3, overflow = TextOverflow.Ellipsis)
                    menu()
                }
            }
            SaucePrice(item.price.currentPrice, item.price.previousPrice, item.price.discount)
            ShopItemCounter(itemCount = shopCartItemCart.count, isIncreaseAvailable = shopCartItemCart.isIncreaseAvailable(),
                onClickDecrease = { onClickDecrease(shopCartItemCart) }, onClickIncrease = { onClickIncrease(shopCartItemCart) })
        }
    }
}

@Composable
internal fun ShopItemCounter(modifier: Modifier = Modifier, itemCount: Int, isIncreaseAvailable: Boolean,
    onClickDecrease: () -> Unit, onClickIncrease: () -> Unit) {
    SauceQuantityStepper(itemCount, onClickDecrease, onClickIncrease, modifier, canIncrease = isIncreaseAvailable)
}
