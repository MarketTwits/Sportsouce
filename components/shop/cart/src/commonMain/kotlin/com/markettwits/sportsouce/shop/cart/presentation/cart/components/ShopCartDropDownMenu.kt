package com.markettwits.sportsouce.shop.cart.presentation.cart.components

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
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor

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
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            items.forEach {
                DropdownMenuItem(text = {
                    Text(
                        modifier = Modifier
                            .padding(2.dp)
                            .animateContentSize(),
                        text = it.title,
                        fontSize = 12.sp,
                        fontFamily = FontNunito.medium(),
                        color = if (it.isBase) MaterialTheme.colorScheme.onPrimary else SportSouceColor.SportSouceLightRed
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