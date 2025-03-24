package com.markettwits.shop.cart.presentation.cart.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.markettwits.shop.cart.domain.ShopItemCart

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.ShopCartItems(
    modifier: Modifier = Modifier,
    items: List<ShopItemCart>,
    onClickItem: (ShopItemCart) -> Unit,
    onClickIncrease: (ShopItemCart) -> Unit,
    onClickDecrease: (ShopItemCart) -> Unit,
    onClickDelete : (ShopItemCart) -> Unit,
) {
    itemsIndexed(items = items, itemContent = { index,item ->
        AnimationBox(key = index){
            ShopCartItem(
                modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                shopCartItemCart = item,
                onItemClick = onClickItem,
                onClickIncrease = onClickIncrease,
                onClickDecrease = onClickDecrease,
                onClickDelete = onClickDelete
            )
        }
    })
}

@Composable
private fun <T> T.AnimationBox(
    key : Any = Unit,
    enter: EnterTransition = expandVertically() + fadeIn(),
    exit: ExitTransition = fadeOut() + shrinkVertically(),
    content: @Composable T.() -> Unit
) {
    val state = remember(key) {
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