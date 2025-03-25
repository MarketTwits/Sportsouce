package com.markettwits.sportsouce.profile.registrations.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.profile.registrations.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.detail.components.start.OrderDialogPaymentButton
import com.markettwits.sportsouce.profile.registrations.detail.components.start.OrderDialogPaymentStatus
import com.markettwits.sportsouce.profile.registrations.detail.components.start.OrderStartCard
import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartOrderProfileDialogScreen(component: StartOrderComponent) {
    val state by component.state.collectAsState()
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = {
            component.obtainEvent(StartOrderStore.Intent.Dismiss)
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OrderStartCard(
                modifier = Modifier.padding(vertical = 8.dp),
                item = state.startOrderInfo,
                onClickStart = {
                    component.obtainEvent(StartOrderStore.Intent.OnClickStart(it))
                }
            )
            OrderDialogPaymentStatus(
                modifier = Modifier.padding(vertical = 8.dp),
                paymentStatus = state.startOrderInfo.payment.title
            )
            OrderDialogPaymentButton(
                modifier = Modifier.padding(vertical = 8.dp),
                priceState = state.startPriceResult,
            ) {
                component.obtainEvent(StartOrderStore.Intent.OnClickPay(state.startOrderInfo.id))
            }
        }
    }
}