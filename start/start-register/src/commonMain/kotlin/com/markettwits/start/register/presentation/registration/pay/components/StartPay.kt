package com.markettwits.start.register.presentation.registration.pay.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.window.Dialog
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.start.register.presentation.promo.components.RegistrationPromoScreen
import com.markettwits.start.register.presentation.registration.pay.component.StartPayComponent
import com.markettwits.start.register.presentation.registration.pay.components.price.ButtonContent
import com.markettwits.start.register.presentation.registration.pay.components.price.StartRegistrationPriceContent


@Composable
internal fun StartPay(
    modifier: Modifier,
    component: StartPayComponent
) {

    val state by component.newState.collectAsState()

    val childSlot by component.childSlot.subscribeAsState()


    childSlot.child?.instance?.also {
        when (it) {
            is StartPayComponent.Child.StartRegistrationPromo ->
                RegistrationPromoScreen(component = it.component)
        }
    }

    if (state.isLoading){
        Dialog(onDismissRequest = {}){
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }


    Column {
        StartRegistrationPriceContent(
            priceResponse = state.state.price,
            isLoading = state.isLoading,
            onClickPromo = component::onClickPromo
        )
        state.state.ButtonContent(
            onClickGoBack = component::onClickGoBack,
            onClickPay = {
                component.onClickRegistry(true)
            },
            onClickSave = {
                component.onClickRegistry(false)
            }
        )
    }
}