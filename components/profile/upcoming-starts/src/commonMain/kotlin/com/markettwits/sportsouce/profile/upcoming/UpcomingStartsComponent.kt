package com.markettwits.sportsouce.profile.upcoming

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.ComponentKoinContext
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponent
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponentBase
import com.markettwits.sportsouce.notification.api.NotificationApi
import com.markettwits.sportsouce.notification.api.NotificationComponent
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.root.RootRegistrationsComponent
import com.markettwits.sportsouce.profile.registrations.presentation.root.RootRegistrationsComponentBase
import com.markettwits.sportsouce.profile.registrations.presentation.root.di.userStartRegistrationModule
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.ToolbarDestination
import com.markettwits.sportsouce.settings.root.RootSettingsComponent
import com.markettwits.sportsouce.settings.root.RootSettingsComponentBase
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase
import com.markettwits.sportsouce.start.search.root.RootStartsSearchComponent
import com.markettwits.sportsouce.start.search.root.RootStartsSearchComponentBase
import com.markettwits.sportsouce.starts.albums.root.RootStartsAlbumsComponent
import com.markettwits.sportsouce.starts.albums.root.RootStartsAlbumsComponentBase
import com.markettwits.sportsouce.starts.cloud.di.sportSauceStartsCloudModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class UpcomingStartsComponent(
    context: ComponentContext,
    private val notifications: NotificationApi,
    private val updateStoreFactory: SelfUpdateStoreFactory,
    private val openStarts: () -> Unit,
) : ComponentContext by context {
    private val dependencies = instanceKeeper.getOrCreate { ComponentKoinContext() }
        .getOrCreateKoinScope(listOf(userStartRegistrationModule, sportSauceStartsCloudModule))
    private val navigation = StackNavigation<Config>()
    private val notificationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    internal val unreadCount = notifications.unreadCount

    internal val childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Dashboard,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    init {
        lifecycle.doOnResume { notificationScope.launch { notifications.refreshUnreadCount() } }
        lifecycle.doOnDestroy { notificationScope.cancel() }
    }

    fun popToRoot() = navigation.popTo(0)

    fun clearSession() {
        (childStack.value.items.first().instance as Child.Dashboard).component.obtainEvent(UpcomingStartsStore.Intent.ClearSession)
        popToRoot()
    }

    private fun createChild(config: Config, context: ComponentContext): Child = when (config) {
        Config.Dashboard -> Child.Dashboard(
            UpcomingStartsDashboardComponent(context, dependencies.get(), UpcomingStartCatalog(dependencies.get())::load) { label ->
                when (label) {
                    UpcomingStartsStore.Label.Browse -> openStarts()
                    UpcomingStartsStore.Label.History -> navigation.pushNew(Config.History)
                    is UpcomingStartsStore.Label.Toolbar -> navigation.pushNew(when (label.destination) {
                        ToolbarDestination.Search -> Config.Search
                        ToolbarDestination.Settings -> Config.Settings
                        ToolbarDestination.Inbox -> Config.Inbox
                        ToolbarDestination.PhotoArchive -> Config.PhotoArchive
                    })
                    is UpcomingStartsStore.Label.SignIn -> navigation.pushNew(Config.Auth(label.openHistoryAfter))
                    is UpcomingStartsStore.Label.Details -> navigation.pushNew(Config.Registration(label.order))
                }
            },
        )
        Config.History -> Child.Registrations(RootRegistrationsComponentBase(context, pop = navigation::pop))
        is Config.Registration -> Child.Registrations(
            RootRegistrationsComponentBase(context, pop = navigation::pop, initialOrder = config.order),
        )
        is Config.Auth -> Child.Auth(RootAuthFlowComponentBase(
            context = context,
            goBack = { navigation.pop() },
            goProfile = {
                if (config.openHistoryAfter) navigation.navigate { it.dropLast(1) + Config.History }
                else navigation.pop()
            },
        ))
        Config.Search -> Child.Search(RootStartsSearchComponentBase(context, pop = navigation::pop))
        Config.PhotoArchive -> Child.PhotoArchive(RootStartsAlbumsComponentBase(context, navigation::pop))
        Config.Settings -> Child.Settings(RootSettingsComponentBase(context, pop = navigation::pop, updateStoreFactory = updateStoreFactory))
        Config.Inbox -> Child.Inbox(notifications.createComponent(
            context = context,
            onBack = { navigation.pop() },
            onOpenStart = { id -> navigation.pushNew(Config.Start(id)) },
        ))
        is Config.Start -> Child.Start(RootStartScreenComponentBase(context, StartScreenInput.Id(config.id), pop = navigation::pop))
    }

    @Serializable
    internal sealed interface Config {
        @Serializable data object Dashboard : Config
        @Serializable data object History : Config
        @Serializable data class Registration(val order: StartOrderInfo) : Config
        @Serializable data class Auth(val openHistoryAfter: Boolean) : Config
        @Serializable data object Search : Config
        @Serializable data object Settings : Config
        @Serializable data object Inbox : Config
        @Serializable data object PhotoArchive : Config
        @Serializable data class Start(val id: Int) : Config
    }

    internal sealed interface Child {
        data class Dashboard(val component: UpcomingStartsDashboardComponent) : Child
        data class Registrations(val component: RootRegistrationsComponent) : Child
        data class Auth(val component: RootAuthFlowComponent) : Child
        data class Search(val component: RootStartsSearchComponent) : Child
        data class Settings(val component: RootSettingsComponent) : Child
        data class Inbox(val component: NotificationComponent) : Child
        data class PhotoArchive(val component: RootStartsAlbumsComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class UpcomingStartsDashboardComponent(
    context: ComponentContext,
    repository: StartOrderRegistrationRepository,
    loadStartDetails: suspend (Set<Int>) -> Map<Int, UpcomingStartDetails>,
    onLabel: (UpcomingStartsStore.Label) -> Unit,
) {
    private val store = context.instanceKeeper.getStore { upcomingStartsStore(repository, loadStartDetails = loadStartDetails) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    val state = store.stateFlow

    init {
        scope.launch { store.labels.collect(onLabel) }
        context.lifecycle.doOnResume { obtainEvent(UpcomingStartsStore.Intent.Resume) }
        context.lifecycle.doOnPause { obtainEvent(UpcomingStartsStore.Intent.Pause) }
        context.lifecycle.doOnDestroy { scope.cancel() }
    }

    fun obtainEvent(intent: UpcomingStartsStore.Intent) = store.accept(intent)
}
