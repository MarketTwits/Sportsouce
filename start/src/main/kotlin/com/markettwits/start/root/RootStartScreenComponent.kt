package com.markettwits.start.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.comments.comments.StartCommentsComponent
import com.markettwits.start.presentation.member.component.RegistrationMemberComponent
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.order.component.OrderComponentComponent
import com.markettwits.start.presentation.promo.component.RegistrationPromoComponent
import com.markettwits.start.presentation.start.component.StartScreenComponent
import kotlinx.serialization.Serializable

interface RootStartScreenComponent{
    val childStack: Value<ChildStack<*, Child>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<*, ChildSlot>>
    @Serializable
    sealed class Config {
        @Serializable
        data class Start(
            val startId: Int,
            val isBackEnabled: Boolean,
        ) : Config()

        @Serializable
        data class StartMembers(
            val startId: Int,
            val items: List<StartMembersUi>,
            val filter: List<MembersFilterGroup>
        ) : Config()

        @Serializable
        data class StartMembersFilter(val items: List<MembersFilterGroup>) : Config()
        @Serializable
        data class StartRegistration(
            val startId: Int,
            val distanceInfo: com.markettwits.cloud.ext_model.DistanceItem,
            val paymentDisabled: Boolean,
            val paymentType: String
        ) : Config()

        @Serializable
        data class StartRegistrationMember(
            val memberId: Int,
            val startStatement: StartStatement
        ) : Config()

    }
    sealed class Child {
        data class Start(val component: StartScreenComponent, val commentsComponent: StartCommentsComponent) : Child()
        data class StartOrder(val component: OrderComponentComponent) : Child()
        data class StartRegistrationMember(val component: RegistrationMemberComponent) : Child()
        data class StartMembers(val component: StartMembersScreenComponent) : Child()
        data class StartMembersFilter(val component: StartMembersFilterScreen) : Child()
    }

    @Serializable
    sealed interface ConfigChild {
        @Serializable
        data class StartPromo(val startId: Int, val promo: String) : ConfigChild
    }

    sealed interface ChildSlot {
        data class StartPromo(val component: RegistrationPromoComponent) : ChildSlot
    }
}