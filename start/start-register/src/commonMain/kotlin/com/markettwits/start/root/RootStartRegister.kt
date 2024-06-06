package com.markettwits.start.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.component.RegistrationMemberComponent
import com.markettwits.start.register.presentation.order.presentation.component.OrderComponentComponent
import com.markettwits.start.register.presentation.promo.component.RegistrationPromoComponent
import com.markettwits.start.register.presentation.success.RegisterSuccessComponent
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
            val distanceInfo: DistanceItem,
            val discounts: List<DistanceItem.Discount>,
            val paymentDisabled: Boolean,
            val paymentType: String
        ) : ConfigStack()

        @Serializable
        data class StartRegistrationMember(
            val memberId: Int,
            val profileMembers: List<ProfileMember>,
            val startStatement: StartStatement,
        ) : ConfigStack()

        @Serializable
        data object StartRegistrationSuccess : ConfigStack()
    }

    sealed interface ChildStack {
        data class StartOrder(val component: OrderComponentComponent) : ChildStack

        data class StartRegistrationMember(val component: RegistrationMemberComponent) : ChildStack
        data class StartRegistrationSuccess(val component: RegisterSuccessComponent) : ChildStack
    }

    @Serializable
    sealed interface ConfigSlot {
        @Serializable
        data class StartPromo(val startId: Int, val promo: String) : ConfigSlot
    }

    sealed interface ChildSlot {
        data class StartPromo(val component: RegistrationPromoComponent) : ChildSlot
    }

    @Serializable
    data class StartRegisterParams(
        val startId: Int,
        val distanceItem: DistanceItem,
        val discounts: List<DistanceItem.Discount>,
        val isPaymentDisabled: Boolean,
        val paymentType: String,
        val startTitle: String,
    )
}