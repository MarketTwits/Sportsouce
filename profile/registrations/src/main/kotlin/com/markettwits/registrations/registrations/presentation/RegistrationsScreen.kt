package com.markettwits.registrations.registrations.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.registrations.registrations.presentation.components.RegistrationsEmpty
import com.markettwits.registrations.registrations.presentation.components.RegistrationsPayButton
import com.markettwits.registrations.registrations.presentation.components.RegistrationsStart
import com.markettwits.registrations.registrations.presentation.components.RegistrationsTopBar

@Composable
fun MyRegistrationsScreen(component: RegistrationsComponent) {
    val state by component.value.collectAsState()

    Scaffold(containerColor = Color.White, topBar = {
        RegistrationsTopBar(title = "Регистрации других участников") {
            component.obtainEvent(RegistrationsStore.Intent.Pop)
        }
    }) {
        if (state.info.isNotEmpty()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())) {
                RegistrationsStart(state.info, isRefreshing = state.isLoading, onRefresh = {
                    component.obtainEvent(RegistrationsStore.Intent.LoadData)
                }, onClick = {
                    component.obtainEvent(RegistrationsStore.Intent.OnCLickItem(it))
                })
                if (state.paymentState.paymentList.isNotEmpty()) {
                    RegistrationsPayButton(
                        count = state.paymentState.count,
                        cost = state.paymentState.totalCost
                    ) {
                        component.obtainEvent(RegistrationsStore.Intent.ShowPaymentDialog(state.paymentState))
                    }
                }
            }
        }
        if (state.info.isEmpty() && state.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = SportSouceColor.SportSouceBlue
                )
            }
        }
        if (state.info.isEmpty() && !state.isLoading && !state.isError) {
            RegistrationsEmpty()
        }
        if (state.isError) {
            Box(modifier = Modifier.fillMaxSize()) {
                FailedScreen(
                    modifier = Modifier.align(Alignment.Center),
                    message = state.message,
                    onClickHelp = { /*TODO*/ }) {
                    component.obtainEvent(RegistrationsStore.Intent.LoadData)
                }
            }
        }
    }
}

@Preview
@Composable
fun MyRegistrationsScreenPreview() {
    MyRegistrationsScreen(RegistrationsComponentMock())
}