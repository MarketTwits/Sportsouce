package com.markettwits.start.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.presentation.album.presentation.component.StartAlbumComponent
import com.markettwits.start.presentation.comments.comments.component.StartCommentsComponent
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.component.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.start.component.StartScreenComponent
import com.markettwits.start_support.presentation.component.StartSupportComponent
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
            val discounts: List<DistanceItem.Discount>,
            val distanceInfo: DistanceItem,
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
            val commentsComponent: StartCommentsComponent,
            val supportComponent: StartSupportComponent
        ) : Child()

        data class StartAlbum(val component: StartAlbumComponent) : Child()
        data class StartRegistration(val component: RootStartRegister) : Child()
        data class StartMembers(val component: StartMembersScreenComponent) : Child()
        data class StartMembersFilter(val component: StartMembersFilterScreen) : Child()
    }
}