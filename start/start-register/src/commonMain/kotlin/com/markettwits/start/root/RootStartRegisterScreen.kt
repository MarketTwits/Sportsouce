package com.markettwits.start.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.start.register.presentation.member.screen.MemberScreen
import com.markettwits.start.register.presentation.order.presentation.screen.StartOrderScreen
import com.markettwits.start.register.presentation.promo.components.RegistrationPromoScreen
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationPage
import com.markettwits.start.register.presentation.success.RegisterSuccessScreen

@Composable
fun RootStartRegisterScreen(component: RootStartRegister) {

    val childSlot by component.childSlot.subscribeAsState()

    val childStack by component.childStack.subscribeAsState()

    childSlot.child?.instance?.also {
        when (it) {
            is RootStartRegister.ChildSlot.StartPromo -> RegistrationPromoScreen(it.component)
        }
    }

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartRegister.ChildStack.StartOrder -> StartOrderScreen(component = child.component)
            is RootStartRegister.ChildStack.StartRegistrationMember -> MemberScreen(component = child.component)
            is RootStartRegister.ChildStack.StartRegistrationSuccess -> RegisterSuccessScreen(component = child.component)
            is RootStartRegister.ChildStack.StartRegistrationPage -> StartRegistrationPage(component = child.component)
        }
    }
}