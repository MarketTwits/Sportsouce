package com.markettwits.start.presentation.order.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_extensions.openWebPage
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.common.EventEffectTest
import com.markettwits.start.presentation.order.component.OrderComponentComponent
import com.markettwits.start.presentation.order.components.distance_info.StartDistanceInfoBox
import com.markettwits.start.presentation.order.components.members.StartMembers
import com.markettwits.start.presentation.order.components.order.OrderComponentBox
import com.markettwits.start.presentation.order.components.payment.PaymentTypeBox
import com.markettwits.start.presentation.order.components.promo.PromoBox
import com.markettwits.start.presentation.order.store.OrderStore

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
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        it.calculateTopPadding()
        if (state.isLoading) {
            LoadingFullScreen()
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickHelp = { /*TODO*/ }
            ) {

            }
        }
        state.orderStatement?.let { order ->
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    StartDistanceInfoBox(
                        modifier = Modifier.padding(5.dp),
                        format = order.distanceInfo.format,
                        distances = order.distanceInfo.distances
                    )
                    StartMembers(members = order.members, onClickMember = { member, id ->
                        component.obtainEvent(OrderStore.Intent.OnClickMember(member, id))
                    })
                    PaymentTypeBox(
                        paymentDisabled = order.paymentDisabled,
                        paymentType = order.paymentType,
                        members = order.members,
                        distances = order.distanceInfo.distances,
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
                    OrderComponentBox(
                        modifier = Modifier.padding(10.dp),
                        price = order.orderPrice,
                        paymentDisabled = order.paymentDisabled,
                        button = state.button,
                        politics = order.checkPolitics,
                        onClickCheckRules = {
                            component.obtainEvent(OrderStore.Intent.OnClickCheckedRules)
                        },
                        onClickRegistry = {
                            component.obtainEvent(OrderStore.Intent.OnClickRegistry)
                        }
                    )
                }
            }
        }
        val context = LocalContext.current
        EventEffectTest(
            event = state.event,
            onConsumed = {
                component.obtainEvent(OrderStore.Intent.OnConsumedEvent)
            },
        ) {
            processLink(it.message, linkBlock = {
                openWebPage(it.message, context)
            }, notLinkBlock = {
                snackBarColor = if (it.success)
                    SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
                snackBarHostState.showLongMessageWithDismiss(message = it.message)
            })
        }
    }
}

suspend fun processLink(link: String, linkBlock: () -> Unit, notLinkBlock: suspend () -> Unit) {
    val regex = Regex("^(https?|ftp)://[a-zA-Z0-9+&@#/%?=~_|!:,.;-]*[a-zA-Z0-9+&@#/%=~_|]")
    if (link.matches(regex)) {
        linkBlock()
    } else {
        notLinkBlock()
    }
}