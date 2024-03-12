package com.markettwits.start.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.start.presentation.album.presentation.component.StartAlbumComponent
import com.markettwits.start.presentation.comments.comments.StartCommentsComponent
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.component.StartScreenComponent
import kotlinx.serialization.Serializable

interface RootStartScreenComponent {
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
        data class StartRegistration(
            val startId: Int,
            val startTitle: String,
            val distanceInfo: com.markettwits.cloud.ext_model.DistanceItem,
            val paymentDisabled: Boolean,
            val paymentType: String
        ) : Config()

        @Serializable
        data class StartAlbum(
            val images: List<String>
        ) : Config()
    }

    sealed class Child {
        data class Start(
            val component: StartScreenComponent,
            val commentsComponent: StartCommentsComponent
        ) : Child()

        data class StartAlbum(val component: StartAlbumComponent) : Child()
        data class StartRegistration(val component: RootStartRegister) : Child()
        data class StartMembers(val component: StartMembersScreenComponent) : Child()
        data class StartMembersFilter(val component: StartMembersFilterScreen) : Child()
    }
}