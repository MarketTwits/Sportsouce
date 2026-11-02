package com.markettwits.sportsouce.profile

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.markettwits.ComponentKoinContext
import com.markettwits.selfupdater.components.notification.di.notificationModule
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.sportsouce.profile.authorized.root.RootAuthorizedProfileComponent
import com.markettwits.sportsouce.profile.authorized.root.RootAuthorizedProfileComponentBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileNavigationResetTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(StandardTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    @Test fun resettingAuthorizedProfilePreservesItsDashboardAndDisposesThePreviousDestination() = runTest {
        val lifecycle = LifecycleRegistry()
        val dependencies = ComponentKoinContext()
        try {
            val factory = dependencies.getOrCreateKoinScope(listOf(notificationModule)).get<SelfUpdateStoreFactory>()
            val root = RootAuthorizedProfileComponentBase(DefaultComponentContext(lifecycle), factory, {})
            val dashboard = assertIs<RootAuthorizedProfileComponent.Child.AuthProfile>(root.childStack.value.active.instance)
            dashboard.component.obtainOutput(AuthorizedProfileComponent.Output.MyResults)
            val results = assertIs<RootAuthorizedProfileComponent.Child.MyResults>(root.childStack.value.active.instance)
            root.popToRoot()
            assertEquals(1, root.childStack.value.items.size)
            assertSame(dashboard, root.childStack.value.active.instance)
            root.popToRoot()
            assertSame(dashboard, root.childStack.value.active.instance)
            dashboard.component.obtainOutput(AuthorizedProfileComponent.Output.MyResults)
            assertNotSame(results, root.childStack.value.active.instance)
        } finally { lifecycle.destroy(); dependencies.onDestroy(); runCurrent() }
    }
}
