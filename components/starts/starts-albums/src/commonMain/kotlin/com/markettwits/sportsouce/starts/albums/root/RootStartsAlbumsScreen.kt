package com.markettwits.sportsouce.starts.albums.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.starts.albums.filter.screen.AlbumsFilterScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.sportsouce.start.presentation.album.presentation.screen.StartAlbumScreen
import com.markettwits.sportsouce.starts.albums.presentation.screen.StartsAlbumsScreen

@Composable
fun RootStartsAlbumsScreen(component: RootStartsAlbumsComponent) {
    val slot by component.childSlot.subscribeAsState()
    slot.child?.instance?.let { child ->
        when (child) { is RootStartsAlbumsComponent.ChildSlot.Filters -> AlbumsFilterScreen(child.component) }
    }
    Children(stack = component.childStack, animation = stackAnimation(fade())) {
        when (val child = it.instance) {
            is RootStartsAlbumsComponent.Child.Albums -> StartsAlbumsScreen(child.component)
            is RootStartsAlbumsComponent.Child.Album -> StartAlbumScreen(child.component)
        }
    }
}
