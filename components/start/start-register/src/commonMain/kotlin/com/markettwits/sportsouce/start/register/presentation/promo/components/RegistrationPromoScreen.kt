package com.markettwits.sportsouce.start.register.presentation.promo.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.start.register.presentation.promo.component.RegistrationPromoComponent
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore

@Composable
fun RegistrationPromoScreen(component: RegistrationPromoComponent) {
    val state by component.state.collectAsState()
    DefaultModalBottomSheet(
        title = "Промокод",
        onDismissRequest = { component.obtainEvent(RegistrationPromoStore.Intent.Dismiss) },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.small),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.large),
        ) {
            RegistrationPromoTextField(
                isError = state.isError,
                message = state.message,
                value = state.promo,
                isEnabled = !state.isLoading,
                onValueChange = { component.obtainEvent(RegistrationPromoStore.Intent.OnPromoChanged(it)) },
            )
            RegistrationButton(
                title = "Применить",
                isEnabled = !state.isLoading,
                isLoading = state.isLoading,
                onClick = { component.obtainEvent(RegistrationPromoStore.Intent.OnClickPromo) },
            )
        }
    }
}
