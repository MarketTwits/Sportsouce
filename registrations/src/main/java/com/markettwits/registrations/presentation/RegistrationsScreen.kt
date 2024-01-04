package com.markettwits.registrations.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.registrations.presentation.components.RegistrationsStart
import com.markettwits.registrations.presentation.components.RegistrationsTopBar

@Composable
fun MyRegistrationsScreen(component: RegistrationsComponent) {
    val state by component.value.collectAsState()
    if (!state.isLoading && !state.isError) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                RegistrationsTopBar(title = "Регистрации других участников") {
                    component.obtainEvent(RegistrationsStore.Intent.Pop)
                }
                RegistrationsStart(state.info) {
                    component.obtainEvent(RegistrationsStore.Intent.OnCLickItem(it))
                }
            }
            if (state.paymentList.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .background(SportSouceColor.SportSouceLighBlue)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceLighBlue),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "Оплатить")
                    }
                }
            }

        }
    }
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = SportSouceColor.SportSouceBlue
            )
        }
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