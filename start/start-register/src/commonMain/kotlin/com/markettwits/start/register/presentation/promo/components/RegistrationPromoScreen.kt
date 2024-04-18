package com.markettwits.start.register.presentation.promo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.presentation.promo.component.RegistrationPromoComponent
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPromoScreen(component: RegistrationPromoComponent) {
    val state by component.state.collectAsState()
    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = {
            component.obtainEvent(RegistrationPromoStore.Intent.Dismiss)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.primary)
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
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                IconButton(onClick = {
                    component.obtainEvent(RegistrationPromoStore.Intent.Dismiss)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

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