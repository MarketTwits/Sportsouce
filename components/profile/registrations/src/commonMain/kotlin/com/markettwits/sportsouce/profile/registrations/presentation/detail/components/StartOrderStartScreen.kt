package com.markettwits.sportsouce.profile.registrations.presentation.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.*
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore

@Composable
fun StartOrderStartScreen(component: StartOrderComponent) {
    val state by component.state.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithClip(title = "Детали заказа") {
                component.obtainEvent(StartOrderStore.Intent.Dismiss)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        AdaptivePane {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                OrderDetailCard(
                    modifier = Modifier.fillMaxWidth(),
                    orderInfo = state.startOrderInfo,
                    onClick = {
                        component.obtainEvent(StartOrderStore.Intent.OnClickStart(state.startOrderInfo.startId))
                    }
                )

                OrderMembersCard(
                    modifier = Modifier.fillMaxWidth(),
                    startOrderMembers = state.startOrderInfo.members
                )

                OrderDialogPaymentStatus(
                    modifier = Modifier.fillMaxWidth(),
                    paymentStatus = state.startOrderInfo.payment
                )

                OrderPromocodeCard(
                    modifier = Modifier.fillMaxWidth(),
                    promocode = state.startOrderInfo.promo
                )

                OrderDialogPaymentButton(
                    modifier = Modifier.fillMaxWidth(),
                    priceState = state.startPriceResult,
                ) {
                    component.obtainEvent(StartOrderStore.Intent.OnClickPay(state.startOrderInfo.id))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}