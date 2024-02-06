package com.markettwits.start.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.start.presentation.member.components.MemberScreen
import com.markettwits.start.presentation.membres.filter_screen.screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.screen.StartMembersScreen
import com.markettwits.start.presentation.order.components.StartOrderScreen
import com.markettwits.start.presentation.promo.components.RegistrationPromoScreen
import com.markettwits.start.presentation.start.screen.StartScreen

@Composable
fun RootStartScreen(component: RootStartScreenComponent) {
    val childSlot by component.childSlot.subscribeAsState()

    childSlot.child?.instance?.also {
        when (it) {
            is RootStartScreenComponent.ChildSlot.StartPromo -> RegistrationPromoScreen(
                component = it.component,
            )
        }
    }
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartScreenComponent.Child.Start -> StartScreen(
                component = child.component,
                commentsComponent = child.commentsComponent
            )
            is RootStartScreenComponent.Child.StartMembers -> StartMembersScreen(component = child.component)
            is RootStartScreenComponent.Child.StartOrder -> StartOrderScreen(child.component)
            is RootStartScreenComponent.Child.StartMembersFilter -> StartMembersFilterScreen(
                component = child.component
            )

            is RootStartScreenComponent.Child.StartRegistration -> {

            }

            is RootStartScreenComponent.Child.StartRegistrationMember -> MemberScreen(component = child.component)
        }
    }
}