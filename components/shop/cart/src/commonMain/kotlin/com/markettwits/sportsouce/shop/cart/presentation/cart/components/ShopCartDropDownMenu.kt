package com.markettwits.sportsouce.shop.cart.presentation.cart.components

import com.markettwits.core_ui.items.theme.SauceTheme

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ShopCartDropDownMenu(
    modifier: Modifier = Modifier,
    expanded : Boolean,
    onClickDismiss : () -> Unit,
    onClickDeleteItem : () -> Unit,
    onClickShare : () -> Unit,
    onClickCopyArticul : () -> Unit,
) {
    ShopItemsOptionMenu(
        modifier = modifier,
        isShow = expanded,
        items = shopCartDropDownItems(object : ShopCartDropDownMenuAction {
            override fun onClickCopyArticul() = onClickCopyArticul()
            override fun onClickShare() = onClickShare()
            override fun onClickDelete() = onClickDeleteItem()
        },),
        onDismiss = onClickDismiss
    )
}


@Composable
private fun ShopItemsOptionMenu(
    modifier: Modifier = Modifier,
    isShow : Boolean,
    items: List<ShopCartDropDownMenuItem>,
    onDismiss : () -> Unit
){
    DropdownMenu(
        modifier = modifier,
        expanded = isShow,
        onDismissRequest = onDismiss,
        containerColor = SauceTheme.colors.background,
        content = {
            items.forEach {
                DropdownMenuItem(text = {
                    Text(
                        modifier = Modifier
                            .padding(2.dp)
                            .animateContentSize(),
                        text = it.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (it.isBase) SauceTheme.colors.textPrimary else SauceTheme.colors.danger.content
                    )
                }, onClick = {
                    it.action()
                    onDismiss()
                })
            }
        }
    )
}



private fun shopCartDropDownItems(action: ShopCartDropDownMenuAction) = listOf(
    ShopCartDropDownMenuItem(
        title = "Поделиться",
        isBase = true,
        action = action::onClickShare
    ),
    ShopCartDropDownMenuItem(
        title = "Скопировать артикул",
        isBase = true,
        action = action::onClickCopyArticul
    ),
    ShopCartDropDownMenuItem(
        title = "Удалить",
        isBase = false,
        action = action::onClickDelete
    )
)

@Immutable
private data class ShopCartDropDownMenuItem(
    val action : () -> Unit,
    val title : String,
    val isBase : Boolean
)

private interface ShopCartDropDownMenuAction{
    fun onClickCopyArticul()
    fun onClickShare()
    fun onClickDelete()
}
