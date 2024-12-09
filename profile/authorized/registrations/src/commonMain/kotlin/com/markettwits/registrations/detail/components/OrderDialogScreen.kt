package com.markettwits.registrations.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.registrations.detail.component.StartOrderComponent
import com.markettwits.registrations.detail.components.start.OrderDialogPaymentStatus
import com.markettwits.registrations.detail.components.start.OrderDialogStartStatus
import com.markettwits.registrations.detail.components.start.OrderStartCard
import com.markettwits.registrations.detail.components.start.RegistrationButton
import com.markettwits.registrations.detail.store.store.StartOrderStore

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
                modifier = Modifier.padding(vertical = 4.dp),
                item = state.startOrderInfo,
                onClickStart = {
                    component.obtainEvent(StartOrderStore.Intent.OnClickStart(it))
                }
            )
            OrderDialogPaymentStatus(
                modifier = Modifier.padding(vertical = 4.dp),
                paymentStatus = state.startOrderInfo.payment.title
            )
            AnimatedVisibility(state.isFailed){
                Box(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clip(Shapes.medium)
                    .background(SportSouceColor.SportSouceLightRed.copy(alpha = 0.1f))
                ){
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(10.dp),
                        text = "Не удалось обновить стоимость заказа",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.bold(),
                        overflow = TextOverflow.Ellipsis,
                        color = SportSouceColor.SportSouceLightRed
                    )
                }
            }
            if (!state.startOrderInfo.payment.payment) {
                RegistrationButton(
                    title = "Оплатить ${state.startOrderInfo.cost} ₽",
                    isLoading = state.isLoading,
                    isError = state.isFailed
                ) {
                    component.obtainEvent(StartOrderStore.Intent.OnClickPay(state.startOrderInfo.id))
                }
            }
        }
    }
}