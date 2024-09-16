package com.markettwits.shop.item.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.components.FullImageContent
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.item.domain.models.ShopPageItem
import com.markettwits.shop.item.presentation.store.ShopItemPageStore

@Composable
internal fun ShopItemPageContent(
    state: ShopItemPageStore.State,
    onClickOption: (String) -> Unit,
    onClickRetry: () -> Unit,
    onClickGoBack: () -> Unit,
) {
    if (state.isError) {
        FailedScreen(
            message = state.message,
            onClickRetry = onClickRetry,
            onClickBack = onClickGoBack
        )
    }
    if (state.isLoading) {
        LoadingFullScreen(onClickBack = onClickGoBack)
    }
    if (state.item != null) {
        ProductScreen(state.item, onClickOption)
    }
}

@Composable
fun ProductScreen(item: ShopPageItem, onClickOption: (String) -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FullImageContent(imageUrl = item.visual.imageUrl)
            ShopItemTitle(title = item.visual.displayName)
            ShopItemPriceRow(price = item.price)
            ShopItemExtraOptions(extraOption = item.extraOptions, onClickOption = onClickOption)
            ShopItemDescriptionOrOptions(
                description = item.visual.description,
                options = item.options
            )
        }

    }
}


@Composable
private fun ShopItemTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        color = MaterialTheme.colorScheme.tertiary,
        textAlign = TextAlign.Start,
        fontSize = 16.sp,
        fontFamily = FontNunito.bold(),
    )
}

