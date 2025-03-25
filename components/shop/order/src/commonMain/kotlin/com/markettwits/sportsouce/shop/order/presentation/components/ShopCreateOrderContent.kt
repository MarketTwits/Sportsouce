package com.markettwits.sportsouce.shop.order.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.shop.domain.mapper.formatPriceWithSpaces
import com.markettwits.sportsouce.shop.order.domain.model.ShopDeliveryType
import com.markettwits.sportsouce.shop.order.domain.model.ShopPaymentType
import com.markettwits.sportsouce.shop.order.domain.model.ShopRecipient
import com.markettwits.sportsouce.shop.order.presentation.components.common.ShopCreateOrderButton
import com.markettwits.sportsouce.shop.order.presentation.components.delivery.ShopDeliveryTypeDescriptionContent
import com.markettwits.sportsouce.shop.order.presentation.components.compositions.ShopOrderComposition
import com.markettwits.sportsouce.shop.order.presentation.components.delivery.ShopOrderDeliveryType
import com.markettwits.sportsouce.shop.order.presentation.components.payment.ShopOrderPaymentType
import com.markettwits.sportsouce.shop.order.presentation.components.price.ShopOrderPriceContent
import com.markettwits.sportsouce.shop.order.presentation.components.recipient.ShopOrderRecipient
import com.markettwits.sportsouce.shop.order.presentation.components.recipient.ShopOrderRecipientBottomSheet
import com.markettwits.sportsouce.shop.order.presentation.components.delivery.mapPlaceToString
import com.markettwits.sportsouce.shop.order.presentation.components.delivery.mapTitleToString
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore

@Composable
internal fun ShopCreateOrderContent(
    state: ShopCreateOrderStore.State,
    modifier: Modifier = Modifier,
    onClickPaymentType: (ShopPaymentType) -> Unit,
    onClickDeliveryType: (ShopDeliveryType) -> Unit,
    onClickCreateOrder: () -> Unit,
    onClickChangeRecipient: () -> Unit,
    onChangeRecipient: (ShopRecipient) -> Unit,
    onClickGoBack: () -> Unit,
    onClickShopRecipientSheet: () -> Unit,
    onConsumedMessage: () -> Unit,
) {

    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLighBlue)
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopBarBase(title = "Оформление заказа", goBack = onClickGoBack) },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = snackBarColor,
                    snackbarData = it
                )
            }
        }
    ) { paddingValues ->
        AdaptivePane {
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
                    shopRecipient = state.shopRecipientState.currentShopRecipient,
                    onClick = onClickShopRecipientSheet
                )
                ShopOrderComposition(shopItems = state.shopOrderResultItems)
                ShopOrderPriceContent(
                    totalCost = formatPriceWithSpaces(state.shopOrderPrice.totalPrice),
                    discount = formatPriceWithSpaces(state.shopOrderPrice.totalDiscount),
                    itemsCount = formatPriceWithSpaces(state.shopOrderPrice.productCount)
                )
                ShopCreateOrderButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    isLoading = state.shopCreateOrderState.isLoading,
                    isAvailable = state.shopCreateOrderState.isAvailable,
                    message = state.shopCreateOrderState.message,
                    onClick = onClickCreateOrder
                )
            }
        }
    }
    EventEffect(
        event = state.message,
        onConsumed = onConsumedMessage,
    ) {
        snackBarColor = if (it.success)
            SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
        snackBarHostState.showLongMessageWithDismiss(message = it.message)
    }
    if (state.shopRecipientState.isExpanded) {
        ShopOrderRecipientBottomSheet(
            state = state.shopRecipientState,
            onClickChangeRecipient = onClickChangeRecipient,
            onChangeRecipient = onChangeRecipient,
            onDismiss = onClickShopRecipientSheet
        )
    }
}