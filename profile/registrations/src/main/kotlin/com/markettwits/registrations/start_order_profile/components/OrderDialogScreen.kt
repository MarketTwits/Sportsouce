package com.markettwits.registrations.start_order_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.base_extensions.openWebPage
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.start_order_profile.component.StartOrderComponent
import com.markettwits.registrations.start_order_profile.components.start.OrderDialogStatus
import com.markettwits.registrations.start_order_profile.components.start.OrderStartCard
import com.markettwits.registrations.start_order_profile.components.start.RegistrationButton
import com.markettwits.registrations.start_order_profile.store.store.StartOrderStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartOrderProfileDialogScreen(component: StartOrderComponent) {
    val state by component.state.collectAsState()
    val context = LocalContext.current
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
                .padding(
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                )
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OrderDialogStatus(
                state.startOrderInfo.payment,
                state.startOrderInfo.statusCode
            )
            OrderStartCard(
                modifier = Modifier.padding(vertical = 14.dp),
                item = state.startOrderInfo,
                onClickPayment = {}
            )
            if (!state.startOrderInfo.payment) {
                RegistrationButton(
                    title = "Оплатить ${state.startOrderInfo.cost} ₽",
                    isLoading = state.isLoading
                ) {
                    component.obtainEvent(StartOrderStore.Intent.OnClickPay(state.startOrderInfo.id))
                }
            }
        }
        LaunchedEffect(key1 = state.paymentUrl) {
            if (state.paymentUrl.isNotEmpty())
                openWebPage(state.paymentUrl, context)
        }
    }
}

@Preview
@Composable
private fun OrderDialogScreenPreview() {
//    SportSouceTheme {
//        StartOrderProfileDialogScreen()
//    }
}

val fakeOrder = StartOrderInfo(
    id = 1,
    startId = 123,
    name = "John DoeJohn DoeJohn DoeJohn DoeJohn Doe",
    image = "example.jpg",
    dateStart = "2024-02-22",
    statusCode = StartStatus(2, "Старт завершен"),
    team = "Team A",
    payment = true,
    ageGroup = "Adult",
    distance = "10 km",
    member = "John DoeJohn DoeJohn DoeJohn DoeJohn DoeJohn Doe",
    kindOfSport = "Running",
    startTitle = "Marathon MarathonMarathonMarathonMarathonMarathonMarathonMarathon",
    cost = "$10.00"
)