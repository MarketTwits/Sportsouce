package com.markettwits.start.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.start.presentation.comments.StartCommentsComponent
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.registration.StartRegistrationComponent
import com.markettwits.start.presentation.start.StartScreenComponent
import kotlinx.serialization.Serializable

interface RootStartScreenComponent{
    val childStack: Value<ChildStack<*, Child>>
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
        data class StartRegistration(val startId: Int, val distanceInfo: DistanceInfo) : Config()
    }
    sealed class Child {
        data class Start(val component: StartScreenComponent, val commentsComponent: StartCommentsComponent) : Child()
        data class StartRegistration(val component: StartRegistrationComponent) : Child()
        data class StartMembers(val component: StartMembersScreenComponent) : Child()
        data class StartMembersFilter(val component: StartMembersFilterScreen) : Child()
    }
}