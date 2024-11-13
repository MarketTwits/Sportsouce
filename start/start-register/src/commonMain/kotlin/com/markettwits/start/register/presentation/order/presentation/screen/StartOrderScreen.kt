package com.markettwits.start.register.presentation.order.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.start.register.presentation.common.EventEffectTest
import com.markettwits.start.register.presentation.order.presentation.component.OrderComponentComponent
import com.markettwits.start.register.presentation.order.presentation.components.distance_info.StartDistanceInfoBox
import com.markettwits.start.register.presentation.order.presentation.components.members.StartMembers
import com.markettwits.start.register.presentation.order.presentation.components.order.OrderComponentBox
import com.markettwits.start.register.presentation.order.presentation.components.payment.PaymentTypeBox
import com.markettwits.start.register.presentation.order.presentation.components.promo.PromoBox
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore


@Composable
fun StartOrderScreen(component: OrderComponentComponent) {

    val state by component.model.collectAsState()

    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLighBlue)
    }
    Scaffold(
        topBar = {
            TopBarWithClip(title = "Регистрация") {
                component.obtainEvent(OrderStore.Intent.GoBack)
            }
        },
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
        },
    ) {
        state.orderStatement?.let { order ->
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.padding(5.dp))
                Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                    StartDistanceInfoBox(
                        startTitle = order.orderTitle,
                        distances = order.distanceInfo,
                        visibleIndex = order.currentOrderDistanceVisibleIndex,
                        onClickDistance = {
                            component.obtainEvent(OrderStore.Intent.OnClickDistance(it))
                        }
                    )
                    StartMembers(
                        members = order.distanceInfo,
                        visibleIndex = order.currentOrderDistanceVisibleIndex,
                        onClickMember = { member, id ->
                            component.obtainEvent(OrderStore.Intent.OnClickMember(member, id))
                        })
                    PaymentTypeBox(
                        paymentDisabled = order.paymentDisabled,
                        paymentType = order.paymentType,
                        members = order.distanceInfo.flatMap { it.members },
                        distances = order.distanceInfo.map { it.distance },
                        payNow = order.payNow,
                        onClickChangePayment = {
                            component.obtainEvent(OrderStore.Intent.ChangePaymentType(it))
                        }
                    )
                    if (!order.paymentDisabled) {
                        PromoBox(onClick = {
                            component.obtainEvent(OrderStore.Intent.OnClickPromo(order.promo))
                        })
                    }
                }
                OrderComponentBox(
                    price = order.orderPrice,
                    paymentDisabled = order.paymentDisabled,
                    button = state.button,
                    politics = order.checkPolitics,
                    onClickCheckRules = {
                        component.obtainEvent(OrderStore.Intent.OnClickCheckedRules)
                    },
                    onClickRegistry = {
                        component.obtainEvent(OrderStore.Intent.OnClickRegistry)
                    },
                    onClickPrivacyPolicy = {
                        component.obtainEvent(OrderStore.Intent.OnClickUrl(it))
                    }

                )
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
        if (state.isLoading) {
            LoadingFullScreen()
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickRetry = {
                    component.obtainEvent(OrderStore.Intent.Retry)
                }
            )
        }
        EventEffectTest(
            event = state.event,
            onConsumed = {
                component.obtainEvent(OrderStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarColor = if (it.success)
                SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }
}