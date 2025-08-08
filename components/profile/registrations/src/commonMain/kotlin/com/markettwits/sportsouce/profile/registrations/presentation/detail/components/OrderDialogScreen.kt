package com.markettwits.sportsouce.profile.registrations.presentation.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.*
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartOrderProfileDialogScreen(component: StartOrderComponent) {
    val state by component.state.collectAsState()
    var componentsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        componentsVisible = true
    }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.outlineVariant,
        dragHandle = null,
        onDismissRequest = {
            component.obtainEvent(StartOrderStore.Intent.Dismiss)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AnimatedVisibility(
                visible = componentsVisible,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 300, delayMillis = 0)
                ) + slideInVertically(
                    animationSpec = tween(durationMillis = 300),
                    initialOffsetY = { it / 4 }
                )
            ) {
                OrderDetailCard(
                    modifier = Modifier.fillMaxWidth(),
                    orderInfo = state.startOrderInfo,
                    onClick = {
                        component.obtainEvent(StartOrderStore.Intent.OnClickStart(state.startOrderInfo.startId))
                    }
                )
            }

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