package com.markettwits.shop.order.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.top_bar.TopBarBase
import com.markettwits.shop.cart.domain.formatPrice
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore

@Composable
internal fun ShopCreateOrderContent(
    state : ShopCreateOrderStore.State,
    modifier: Modifier = Modifier,
    onClickPaymentType : (ShopPaymentType) -> Unit,
    onClickDeliveryType: (ShopDeliveryType) -> Unit,
    onClickGoBack : () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBarBase(title = "Оформление заказа", goBack = onClickGoBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = paddingValues.calculateTopPadding())
                .padding(10.dp)
        ) {
            ShopOrderDeliveryType(
                shopDeliveryType = state.deliveryType.options,
                selectedOption = state.deliveryType.selectedOption,
                onClickOption = onClickDeliveryType
            )
            ShopOrderPaymentType(
                shopFilterPaymentTypes = state.paymentType.options,
                selectedPaymentType = state.paymentType.selectedOption,
                selectedShopDeliveryType = state.deliveryType.selectedOption,
                onClickChangePaymentType = onClickPaymentType
            )
            ShopOrderRecipient(shopRecipient = state.shopRecipient)
            ShopOrderComposition(shopItems = state.shopOrderItems)
            ShopOrderPriceContent(
                totalCost = state.shopOrderPrice.totalPrice.formatPrice(),
                discount = state.shopOrderPrice.totalDiscount.formatPrice(),
                itemsCount = state.shopOrderPrice.productCount.toString()
            )
        }
    }
}