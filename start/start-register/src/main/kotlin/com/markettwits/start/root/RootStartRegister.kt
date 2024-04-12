package com.markettwits.start.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.component.RegistrationMemberComponent
import com.markettwits.start.register.presentation.order.presentation.component.OrderComponentComponent
import com.markettwits.start.register.presentation.promo.component.RegistrationPromoComponent
import kotlinx.serialization.Serializable

interface RootStartRegister {

    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<*, ChildSlot>>

    @Serializable
    sealed class ConfigStack {
        @Serializable
        data class StartRegistration(
            val startId: Int,
            val startTitle: String,
            val distanceInfo: com.markettwits.cloud.ext_model.DistanceItem,
            val paymentDisabled: Boolean,
            val paymentType: String
        ) : ConfigStack()

        @Serializable
        data class StartRegistrationMember(
            val memberId: Int,
            val profileMembers: List<ProfileMember>,
            val startStatement: StartStatement,
        ) : ConfigStack()
    }

    sealed interface ChildStack {
        data class StartOrder(val component: OrderComponentComponent) :
            ChildStack

        data class StartRegistrationMember(val component: RegistrationMemberComponent) : ChildStack
    }

    @Serializable
    sealed interface ConfigSlot {
        @Serializable
        data class StartPromo(val startId: Int, val promo: String) : ConfigSlot
    }

    sealed interface ChildSlot {
        data class StartPromo(val component: RegistrationPromoComponent) : ChildSlot
    }
}