package com.markettwits.sportsouce.starts.albums.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.router.slot.*
import com.markettwits.sportsouce.starts.albums.filter.component.AlbumsFilterComponentBase
import com.markettwits.sportsouce.starts.albums.presentation.store.StartsAlbumsStore
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.starts.albums.di.startsAlbumsModule
import com.markettwits.sportsouce.starts.albums.presentation.component.StartsAlbumsComponentBase
import com.markettwits.sportsouce.start.presentation.album.presentation.component.StartAlbumComponentBase

class RootStartsAlbumsComponentBase(context: ComponentContext, private val pop: () -> Unit) : RootStartsAlbumsComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootStartsAlbumsComponent.Config>()
    private val slotNavigation = SlotNavigation<RootStartsAlbumsComponent.ConfigSlot>()
    private val koinContext = instanceKeeper.getOrCreate { ComponentKoinContext() }
    private val scope = koinContext.getOrCreateKoinScope(listOf(startsAlbumsModule))
    override val childStack = childStack(source = navigation, serializer = RootStartsAlbumsComponent.Config.serializer(),
        initialConfiguration = RootStartsAlbumsComponent.Config.Albums, handleBackButton = true, childFactory = ::child)
    override val childSlot = childSlot(source = slotNavigation, serializer = RootStartsAlbumsComponent.ConfigSlot.serializer(),
        handleBackButton = true, childFactory = ::slotChild)
    private fun slotChild(config: RootStartsAlbumsComponent.ConfigSlot, context: ComponentContext): RootStartsAlbumsComponent.ChildSlot = when (config) {
        is RootStartsAlbumsComponent.ConfigSlot.Filters -> RootStartsAlbumsComponent.ChildSlot.Filters(
            AlbumsFilterComponentBase(context, config.year, scope.get(), apply = { year ->
                slotNavigation.dismiss {
                    (childStack.value.active.instance as? RootStartsAlbumsComponent.Child.Albums)?.component?.obtainEvent(StartsAlbumsStore.Intent.SelectYear(year))
                }
            }, dismiss = slotNavigation::dismiss))
    }
    private fun child(config: RootStartsAlbumsComponent.Config, context: ComponentContext): RootStartsAlbumsComponent.Child = when (config) {
        RootStartsAlbumsComponent.Config.Albums -> RootStartsAlbumsComponent.Child.Albums(
            StartsAlbumsComponentBase(context, scope.get(), pop,
                openAlbum = { navigation.pushNew(RootStartsAlbumsComponent.Config.Album(it)) },
                openFilters = { slotNavigation.activate(RootStartsAlbumsComponent.ConfigSlot.Filters(it)) }))
        is RootStartsAlbumsComponent.Config.Album -> RootStartsAlbumsComponent.Child.Album(
            StartAlbumComponentBase(componentContext = context, storeFactory = scope.get(), albumId = config.id, pop = navigation::pop))
    }
}
