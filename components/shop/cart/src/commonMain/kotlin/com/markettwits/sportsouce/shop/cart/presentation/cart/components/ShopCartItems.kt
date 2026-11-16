package com.markettwits.sportsouce.shop.cart.presentation.cart.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.ShopCartItems(
    modifier: Modifier = Modifier,
    items: List<ShopItemCart>,
    onClickItem: (ShopItemCart) -> Unit,
    onClickIncrease: (ShopItemCart) -> Unit,
    onClickDecrease: (ShopItemCart) -> Unit,
    onClickDelete : (ShopItemCart) -> Unit,
) {
    items(items = items, key = { it.item.id }) { item ->
        AnimationBox {
            ShopCartItem(
                modifier = modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                shopCartItemCart = item,
                onItemClick = onClickItem,
                onClickIncrease = onClickIncrease,
                onClickDecrease = onClickDecrease,
                onClickDelete = onClickDelete
            )
        }
    }
}

@Composable
private fun <T> T.AnimationBox(
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable T.() -> Unit
) {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = enter,
        exit = exit
    ) { content() }
}
