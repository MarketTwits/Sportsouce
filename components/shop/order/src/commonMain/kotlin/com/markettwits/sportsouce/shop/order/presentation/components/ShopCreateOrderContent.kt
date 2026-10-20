package com.markettwits.sportsouce.shop.order.presentation.components

import com.markettwits.core_ui.items.theme.SauceTheme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core_ui.items.components.navigation.SauceActionFooter
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.event.EventEffect
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

    var feedbackSuccess by remember { mutableStateOf(true) }
    val feedbackColors = if (feedbackSuccess) SauceTheme.colors.success else SauceTheme.colors.danger

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopBarBase(title = "Оформление заказа", goBack = onClickGoBack) },
        bottomBar = {
            SauceActionFooter {
            AdaptivePane(fillHeight = false) {
                ShopCreateOrderButton(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    isLoading = state.shopCreateOrderState.isLoading,
                    isAvailable = state.shopCreateOrderState.isAvailable,
                    message = state.shopCreateOrderState.message, onClick = onClickCreateOrder)
            }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = feedbackColors.content,
                    containerColor = feedbackColors.container,
                    snackbarData = it
                )
            }
        }
    ) { paddingValues ->
        AdaptivePane {
            Column(
                modifier = Modifier
                    .fillMaxSize().padding(paddingValues).consumeWindowInsets(paddingValues)
                    .verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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

            }
        }
    }
    EventEffect(
        event = state.message,
        onConsumed = onConsumedMessage,
    ) {
        feedbackSuccess = it.success
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
