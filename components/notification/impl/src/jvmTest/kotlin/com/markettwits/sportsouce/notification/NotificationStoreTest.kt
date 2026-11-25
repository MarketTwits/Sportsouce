package com.markettwits.sportsouce.notification

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.sportsouce.notification.api.model.*
import com.markettwits.sportsouce.notification.api.store.NotificationStore.*
import com.markettwits.sportsouce.notification.impl.api.NotificationApiImpl
import com.markettwits.sportsouce.notification.impl.domain.*
import com.markettwits.sportsouce.notification.impl.presentation.store.NotificationStoreFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationStoreTest {
    @BeforeTest fun main() { Dispatchers.setMain(StandardTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    private val item = InboxNotification(20262, notification = NotificationContent(relatedStartId = 313))

    @Test fun duplicateTapsAreBlockedAndStartOpensOnlyAfterSuccessfulRead() = runTest {
        val repository = FakeRepository()
        val store = NotificationStoreFactory(DefaultStoreFactory(), repository).create()
        val labels = mutableListOf<Label>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { store.labels.collect { labels += it } }
        try {
            store.accept(Intent.Read(item, true))
            store.accept(Intent.Read(item, true))
            runCurrent()
            assertEquals(1, repository.readCalls)
            assertEquals(setOf(item.id), store.state.pending)
            assertTrue(labels.isEmpty())
            store.accept(Intent.ReadAll)
            assertEquals(0, repository.readAllCalls)
            repository.readGate.complete(Unit)
            runCurrent()
            assertEquals(listOf<Label>(Label.OpenStart(313)), labels)
            assertTrue(store.state.pending.isEmpty())
        } finally { store.dispose() }
    }

    @Test fun aFailedReadLeavesTheUserInTheInboxAndExposesAnActionableMessage() = runTest {
        val repository = FakeRepository().apply { failure = IllegalStateException("offline") }
        val store = NotificationStoreFactory(DefaultStoreFactory(), repository).create()
        val labels = mutableListOf<Label>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { store.labels.collect { labels += it } }
        try {
            store.accept(Intent.Read(item, true))
            repository.readGate.complete(Unit)
            runCurrent()
            assertTrue(labels.isEmpty())
            assertTrue(store.state.pending.isEmpty())
            assertTrue(store.state.message.orEmpty().contains("подключение"))
            store.accept(Intent.MessageShown)
            assertNull(store.state.message)
        } finally { store.dispose() }
    }

    @Test fun resumingUsesSilentRefreshAndDisposingCancelsAnUnfinishedWrite() = runTest {
        val repository = FakeRepository()
        val store = NotificationStoreFactory(DefaultStoreFactory(), repository).create()
        store.accept(Intent.Resume)
        assertFalse(store.state.refreshing)
        store.accept(Intent.Refresh)
        assertTrue(store.state.refreshing)
        store.accept(Intent.RefreshFinished)
        assertFalse(store.state.refreshing)
        store.accept(Intent.Read(item))
        runCurrent()
        store.dispose()
        runCurrent()
        assertTrue(repository.writeCancelled)
    }

    @Test fun pushInvalidatesOnlyTheResumedInboxWithoutShowingPullToRefresh() = runTest {
        val repository = FakeRepository()
        val api = NotificationApiImpl(repository, NotificationStoreFactory(DefaultStoreFactory(), repository))
        val active = LifecycleRegistry()
        val inactive = LifecycleRegistry()
        try {
            val visible = api.createComponent(DefaultComponentContext(active), {}, {})
            val hidden = api.createComponent(DefaultComponentContext(inactive), {}, {})
            active.resume()
            inactive.resume()
            inactive.pause()
            runCurrent()
            val visibleVersion = visible.state.value.refreshVersion
            val hiddenVersion = hidden.state.value.refreshVersion

            api.invalidateInbox()
            runCurrent()

            assertEquals(visibleVersion + 1, visible.state.value.refreshVersion)
            assertEquals(hiddenVersion, hidden.state.value.refreshVersion)
            assertFalse(visible.state.value.refreshing)
        } finally {
            active.destroy()
            inactive.destroy()
            runCurrent()
        }
    }

    @Test fun permissionChangesReachTheInboxAndActionsGoThroughTheStore() = runTest {
        val permission = object : PushPermission {
            override val state = MutableStateFlow(PushPermissionState.Hidden)
            var requests = 0
            var settings = 0
            override fun request() { requests++ }
            override fun openSettings() { settings++ }
        }
        val store = NotificationStoreFactory(DefaultStoreFactory(), FakeRepository(), permission).create()
        try {
            permission.state.value = PushPermissionState.Request
            runCurrent()
            assertEquals(PushPermissionState.Request, store.state.pushPermission)
            store.accept(Intent.RequestPushPermission)
            permission.state.value = PushPermissionState.Settings
            runCurrent()
            assertEquals(PushPermissionState.Settings, store.state.pushPermission)
            store.accept(Intent.OpenPushSettings)
            permission.state.value = PushPermissionState.Granted
            runCurrent()
            assertEquals(PushPermissionState.Granted, store.state.pushPermission)
            assertEquals(1, permission.requests)
            assertEquals(1, permission.settings)
        } finally {
            store.dispose()
        }
    }

    private class FakeRepository : NotificationRepository {
        override val unreadCount = MutableStateFlow(20)
        override val readState = MutableStateFlow(NotificationReadState())
        override val inboxUpdates = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
        override fun invalidateInbox() { inboxUpdates.tryEmit(Unit) }
        val readGate = CompletableDeferred<Unit>()
        var readCalls = 0
        var readAllCalls = 0
        var failure: Exception? = null
        var writeCancelled = false
        override suspend fun page(offset: Int, limit: Int) = NotificationPage()
        override suspend fun refreshUnreadCount() = Unit
        override suspend fun read(item: InboxNotification) {
            readCalls++
            try { readGate.await(); failure?.let { throw it } }
            catch (cancelled: CancellationException) { writeCancelled = true; throw cancelled }
        }
        override suspend fun readAll() { readAllCalls++ }
        override fun clear() = Unit
    }
}
