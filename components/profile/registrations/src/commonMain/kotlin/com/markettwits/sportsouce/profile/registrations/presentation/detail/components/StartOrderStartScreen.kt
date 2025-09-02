package com.markettwits.sportsouce.profile.registrations.presentation.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.gradients.GradientDirection
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.gradients.animatedFabGradient
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.*
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore
import kotlinx.coroutines.launch

@Composable
fun StartOrderStartScreen(component: StartOrderComponent) {
    val state by component.state.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var paymentButtonTop by remember { mutableStateOf(0f) }
    var paymentButtonHeight by remember { mutableStateOf(0) }
    var screenHeight by remember { mutableStateOf(0) }

    // Calculate if payment button is visible
    val isPaymentButtonVisible by remember {
        derivedStateOf {
            if (paymentButtonTop == 0f || paymentButtonHeight == 0 || screenHeight == 0) {
                false
            } else {
                val buttonBottom = paymentButtonTop + paymentButtonHeight
                val scrollOffset = scrollState.value
                val visibleTop = scrollOffset.toFloat()
                val visibleBottom = scrollOffset + screenHeight

                // Button is visible if it overlaps with visible area
                buttonBottom > visibleTop && paymentButtonTop < visibleBottom
            }
        }
    }

    // Show FAB only when price is Success and payment button is not visible
    val showFab by remember {
        derivedStateOf {
            state.startPriceResult is StartOrderStore.StartPriceResult.Success &&
                    !isPaymentButtonVisible
        }
    }


    Scaffold(
        topBar = {
            TopBarWithClip(title = "Детали регистрации") {
                component.obtainEvent(StartOrderStore.Intent.Dismiss)
            }
        },
        floatingActionButton = {
            androidx.compose.animation.AnimatedVisibility(
                visible = showFab,
                enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn(),
                exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.scaleOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(paymentButtonTop.toInt())
                        }
                    },
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp
                    ),
                    modifier = Modifier.animatedFabGradient(
                        shape = RoundedCornerShape(16.dp),
                        direction = GradientDirection.Diagonal
                    ),
                    text = {
                        Text("Требуется оплата", color = MaterialTheme.colorScheme.onSecondary)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            tint = MaterialTheme.colorScheme.onSecondary,
                            contentDescription = null
                        )
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.outlineVariant,
        modifier = Modifier.onGloballyPositioned { coordinates ->
            screenHeight = coordinates.size.height
        }
    ) { paddingValues ->
        AdaptivePane {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            paymentButtonTop = coordinates.positionInParent().y
                            paymentButtonHeight = coordinates.size.height
                        },
                    priceState = state.startPriceResult,
                ) {
                    component.obtainEvent(StartOrderStore.Intent.OnClickPay(state.startOrderInfo.id))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}