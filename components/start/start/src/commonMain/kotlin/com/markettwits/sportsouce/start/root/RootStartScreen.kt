package com.markettwits.sportsouce.start.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.sportsouce.start.presentation.album.presentation.screen.StartAlbumScreen
import com.markettwits.sportsouce.start.presentation.comments.screen.StartCommentsScreen
import com.markettwits.sportsouce.start.presentation.membres.screen.StartMembersScreen
import com.markettwits.sportsouce.start.presentation.result.screen.StartMemberResultsScreen
import com.markettwits.sportsouce.start.presentation.series.screen.StartSeriesScreen
import com.markettwits.sportsouce.start.presentation.start.screen.StartScreen
import com.markettwits.sportsouce.start.register.root.RootStartRegisterScreen
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowScreen

@Composable
fun RootStartScreen(component: RootStartScreenComponent) {
    val stack by component.childStack.subscribeAsState()
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartScreenComponent.Child.Start -> {
                StartScreen(
                    startComponent = child.component,
                    startSupportComponent = child.supportComponent,
                    isActive = child === stack.active.instance,
                )
            }
            is RootStartScreenComponent.Child.StartMembers -> StartMembersScreen(child.component)
            is RootStartScreenComponent.Child.StartRegistration -> RootStartRegisterScreen(child.component)
            is RootStartScreenComponent.Child.StartAlbum -> StartAlbumScreen(child.component)
            is RootStartScreenComponent.Child.StartMembersResults -> StartMemberResultsScreen(child.component)
            is RootStartScreenComponent.Child.StartComments -> StartCommentsScreen(
                component = child.component,
            )

            is RootStartScreenComponent.Child.StartSeries -> StartSeriesScreen(child.component)
            is RootStartScreenComponent.Child.SupportAuth -> RootAuthFlowScreen(child.component)
        }
    }
}
