package com.markettwits.sportsouce.starts.albums.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.start.presentation.album.presentation.component.StartAlbumComponent
import com.markettwits.sportsouce.starts.albums.presentation.component.StartsAlbumsComponent
import kotlinx.serialization.Serializable

interface RootStartsAlbumsComponent {
    val childStack: Value<ChildStack<*, Child>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<*, ChildSlot>>
    @Serializable
    sealed interface ConfigSlot {
        @Serializable data class Filters(val year: Int?) : ConfigSlot
    }
    sealed interface ChildSlot {
        data class Filters(val component: com.markettwits.sportsouce.starts.albums.filter.component.AlbumsFilterComponent) : ChildSlot
    }
    @Serializable
    sealed interface Config {
        @Serializable data object Albums : Config
        @Serializable data class Album(val id: Int) : Config
    }
    sealed interface Child {
        data class Albums(val component: StartsAlbumsComponent) : Child
        data class Album(val component: StartAlbumComponent) : Child
    }
}
