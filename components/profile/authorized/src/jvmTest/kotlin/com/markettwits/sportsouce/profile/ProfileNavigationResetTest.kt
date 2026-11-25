package com.markettwits.sportsouce.profile

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.notification.api.NotificationApi
import com.markettwits.sportsouce.notification.api.NotificationComponent
import com.markettwits.sportsouce.notification.api.store.NotificationStore
import com.markettwits.selfupdater.components.notification.di.notificationModule
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.sportsouce.profile.authorized.root.RootAuthorizedProfileComponent
import com.markettwits.sportsouce.profile.authorized.root.RootAuthorizedProfileComponentBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
            val root = RootAuthorizedProfileComponentBase(DefaultComponentContext(lifecycle), factory, {}, object : NotificationApi {
                override fun invalidateInbox() = Unit
                override val unreadCount = MutableStateFlow(0)
                override suspend fun refreshUnreadCount() = Unit
                override fun clear() = Unit
                override fun createComponent(context: ComponentContext, onBack: () -> Unit, onOpenStart: (Int) -> Unit) =
                    object : NotificationComponent {
                        override val state = MutableStateFlow(NotificationStore.State())
                        override val authSlot get() = error("The sign-in slot is not part of this test")
                        override fun obtainEvent(intent: NotificationStore.Intent) = Unit
                    }
            })
            val dashboard = assertIs<RootAuthorizedProfileComponent.Child.AuthProfile>(root.childStack.value.active.instance)
            dashboard.component.obtainOutput(AuthorizedProfileComponent.Output.Notifications)
            val inbox = assertIs<RootAuthorizedProfileComponent.Child.Inbox>(root.childStack.value.active.instance)
            root.popToRoot()
            assertEquals(1, root.childStack.value.items.size)
            assertSame(dashboard, root.childStack.value.active.instance)
            root.popToRoot()
            assertSame(dashboard, root.childStack.value.active.instance)
            dashboard.component.obtainOutput(AuthorizedProfileComponent.Output.Notifications)
            assertNotSame(inbox.component, assertIs<RootAuthorizedProfileComponent.Child.Inbox>(root.childStack.value.active.instance).component)
        } finally { lifecycle.destroy(); dependencies.onDestroy(); runCurrent() }
    }
}
