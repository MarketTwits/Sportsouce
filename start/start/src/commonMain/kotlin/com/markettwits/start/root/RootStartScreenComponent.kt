package com.markettwits.start.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.start.presentation.album.presentation.component.StartAlbumComponent
import com.markettwits.start.presentation.comments.component.StartCommentsComponent
import com.markettwits.start.presentation.membres.filter.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.component.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.result.component.StartMemberResultsComponent
import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start.presentation.start.component.StartScreenComponent
import com.markettwits.start.register.presentation.registration.registration.component.StartRegistrationInput
import com.markettwits.start_support.presentation.component.StartSupportComponent
import kotlinx.serialization.Serializable

interface RootStartScreenComponent {
    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed class Config {

        @Serializable
        data class Start(val startId: Int, val isBackEnabled: Boolean) : Config()

        @Serializable
        data class StartMembers(
            val startId: Int,
            val items: List<StartMembersUi>,
            val filter: List<MembersFilterGroup>
        ) : Config()

        @Serializable
        data class StartMembersFilter(val items: List<MembersFilterGroup>) : Config()

        @Serializable
        data class StartMembersResult(val items: List<MemberResult>) : Config()

        @Serializable
        data class StartRegistration(val input: StartRegistrationInput) : Config()

        @Serializable
        data class StartAlbum(val images: List<String>) : Config()
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

        data class StartMembersResults(val component: StartMemberResultsComponent) : Child()

        data class StartMembersFilter(val component: StartMembersFilterScreen) : Child()
    }
}