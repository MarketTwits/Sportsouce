package com.markettwits.start.presentation.promo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.promo.component.RegistrationPromoComponent
import com.markettwits.start.presentation.promo.store.RegistrationPromoStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPromoScreen(component: RegistrationPromoComponent) {
    val state by component.state.collectAsState()
    //TODO add close effect when close screen after apply promo
    val labels by component.labels.collectAsState(RegistrationPromoStore.Label.Empty)

    ModalBottomSheet(
        containerColor = Color.White,
        onDismissRequest = {
            component.obtainEvent(RegistrationPromoStore.Intent.Dismiss)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.White)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Промокод",
                    fontFamily = FontNunito.bold,
                    fontSize = 18.sp,
                    color = SportSouceColor.SportSouceBlue
                )
                Icon(
                    modifier = Modifier.clickable {
                        component.obtainEvent(RegistrationPromoStore.Intent.Dismiss)
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = SportSouceColor.SportSouceBlue
                )
            }
            RegistrationPromoTextField(
                label = "Введите код",
                isError = state.isError,
                message = state.message,
                value = state.promo,
                onValueChange = {
                    component.obtainEvent(RegistrationPromoStore.Intent.OnPromoChanged(it))
                }
            )
            RegistrationButton(
                modifier = Modifier.padding(10.dp),
                title = "Прмиенить",
                isEnabled = !state.isLoading,
                isLoading = state.isLoading,
                onClick = {
                    component.obtainEvent(RegistrationPromoStore.Intent.OnClickPromo)
                })
        }
    }
}