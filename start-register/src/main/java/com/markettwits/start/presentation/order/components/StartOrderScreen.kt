package com.markettwits.start.presentation.order.components

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.member.store.RegistrationMemberStore
import com.markettwits.start.presentation.order.component.OrderComponentComponent
import com.markettwits.start.presentation.order.components.distance_info.StartDistanceInfoBox
import com.markettwits.start.presentation.order.components.members.StartMembers
import com.markettwits.start.presentation.order.components.order.OrderComponentBox
import com.markettwits.start.presentation.order.components.payment.PaymentTypeBox
import com.markettwits.start.presentation.order.components.promo.PromoBox
import com.markettwits.start.presentation.order.store.OrderStore
import com.markettwits.start.presentation.registration.components.LoadingScreen
import com.markettwits.start.presentation.registration.components.StartRegistrationTopBar

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
            StartRegistrationTopBar(goBack = {
                component.obtainEvent(OrderStore.Intent.GoBack)
            })
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
        containerColor = SportSouceColor.DirtyWhite
    ) {
        it.calculateTopPadding()
        if (state.isLoading) {
            LoadingScreen()
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
                        members = order.members
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
                        rulesIsChecked = order.checkPolitics,
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
        EventEffect(
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

@Preview(device = Devices.DEFAULT)
@Composable
private fun StartOrderScreenPreview() {
    //  StartOrderScreen()
}