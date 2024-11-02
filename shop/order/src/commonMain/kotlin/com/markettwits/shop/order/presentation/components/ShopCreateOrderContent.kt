package com.markettwits.shop.order.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.top_bar.TopBarBase
import com.markettwits.shop.cart.domain.formatPrice
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.domain.model.ShopRecipient
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore

@Composable
internal fun ShopCreateOrderContent(
    state : ShopCreateOrderStore.State,
    modifier: Modifier = Modifier,
    onClickPaymentType : (ShopPaymentType) -> Unit,
    onClickDeliveryType: (ShopDeliveryType) -> Unit,
    onClickCreateOrder : () -> Unit,
    onChangeRecipient : (ShopRecipient) -> Unit,
    onClickGoBack : () -> Unit
) {

    var isShopRecipientBottomSheetExpanded by rememberSaveable{
        mutableStateOf(false)
    }

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
            ShopDeliveryTypeDescriptionContent(
                deliveryTitle = state.deliveryType.selectedOption.mapTitleToString(),
                deliveryPlace = state.deliveryType.selectedOption.mapPlaceToString()
            )
            ShopOrderPaymentType(
                shopFilterPaymentTypes = state.paymentType.options,
                selectedPaymentType = state.paymentType.selectedOption,
                selectedShopDeliveryType = state.deliveryType.selectedOption,
                onClickChangePaymentType = onClickPaymentType
            )
            ShopOrderRecipient(
                shopRecipient = state.shopRecipient,
                onClick = {
                    isShopRecipientBottomSheetExpanded = true
                }
            )
            ShopOrderComposition(shopItems = state.shopOrderItems)
            ShopOrderPriceContent(
                totalCost = state.shopOrderPrice.totalPrice.formatPrice(),
                discount = state.shopOrderPrice.totalDiscount.formatPrice(),
                itemsCount = state.shopOrderPrice.productCount.toString()
            )
            ShopCreateOrderButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onClickCreateOrder
            )
        }
    }
    if (isShopRecipientBottomSheetExpanded){
        ShopOrderRecipientBottomSheet(
            recipient = state.shopRecipient,
            onChangeRecipient = onChangeRecipient,
            onDismiss = {
                isShopRecipientBottomSheetExpanded  = false
            }
        )
    }

}