package com.markettwits.start.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.start.presentation.album.presentation.screen.StartAlbumScreen
import com.markettwits.start.presentation.membres.filter.screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.screen.StartMembersScreen
import com.markettwits.start.presentation.result.screen.StartMemberResultsScreen
import com.markettwits.start.presentation.start.screen.StartScreen

@Composable
fun RootStartScreen(component: RootStartScreenComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartScreenComponent.Child.Start -> StartScreen(
                startComponent = child.component,
                startCommentsComponent = child.commentsComponent,
                startSupportComponent = child.supportComponent,
            )
            is RootStartScreenComponent.Child.StartMembers -> StartMembersScreen(child.component)
            is RootStartScreenComponent.Child.StartRegistration -> RootStartRegisterScreen(child.component)
            is RootStartScreenComponent.Child.StartMembersFilter -> StartMembersFilterScreen(child.component)
            is RootStartScreenComponent.Child.StartAlbum -> StartAlbumScreen(child.component)
            is RootStartScreenComponent.Child.StartMembersResults -> StartMemberResultsScreen(child.component)
        }
    }
}