package com.markettwits.registrations.root_registrations

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.isFront
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.registrations.paymant_dialog.RegistrationsPaymentComponent
import com.markettwits.registrations.paymant_dialog.RegistrationsPaymentScreen
import com.markettwits.registrations.registrations.presentation.MyRegistrationsScreen
import com.markettwits.start.root.RootStartScreen

@OptIn(FaultyDecomposeApi::class)
@Composable
fun RootRegistrationsScreen(component: RootRegistrationsComponent) {

    val childStack by component.childStack.subscribeAsState()
    val childSlot by component.childSlot.subscribeAsState()

    childSlot.child?.instance?.also {
        when (it) {
            is RootRegistrationsComponent.ChildSlot.PaymentDialog -> ModalBottomSheetScreen(
                component = it.component,
                onDismissRequest = component::dismissSlotChild
            )
        }
    }

    Children(
        stack = childStack,
        animation = stackAnimation { from, to, direction ->
            if (direction.isFront) {
                slide() + fade()
            } else {
                scale(frontFactor = 1F, backFactor = 0.7F) + fade()
            }
        },
    ) {
        when (val child = it.instance) {
            is RootRegistrationsComponent.ChildStack.Registrations ->
                MyRegistrationsScreen(component = child.component)

            is RootRegistrationsComponent.ChildStack.Start ->
                RootStartScreen(component = child.component)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalBottomSheetScreen(
    component: RegistrationsPaymentComponent,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = { onDismissRequest() }) {
        RegistrationsPaymentScreen(
            component = component
        )
    }
}

