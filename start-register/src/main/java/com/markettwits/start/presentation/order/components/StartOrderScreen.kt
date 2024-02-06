package com.markettwits.start.presentation.order.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
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
    Scaffold(
        topBar = {
            StartRegistrationTopBar(goBack = {
                component.obtainEvent(OrderStore.Intent.GoBack)
            })
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
        state.startStatement?.let { order ->
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
                    PaymentTypeBox()
                    PromoBox(onClick = {
                        component.obtainEvent(OrderStore.Intent.OnClickPromo(order.promo))
                    })
                    OrderComponentBox(modifier = Modifier.padding(10.dp), price = order.orderPrice)
                }
            }
        }


    }
}

@Preview(device = Devices.DEFAULT)
@Composable
private fun StartOrderScreenPreview() {
    //  StartOrderScreen()
}