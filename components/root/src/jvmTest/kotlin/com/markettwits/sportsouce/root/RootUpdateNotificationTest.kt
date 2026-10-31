package com.markettwits.sportsouce.root

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.review.root.RootReviewComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RootUpdateNotificationTest {
    @Test
    fun progressStripReopensTheUpdateScreenWithoutDiscardingProgress() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val root = RootComponentBase(DefaultComponentContext(lifecycle))
            val progress = InAppNotification.SelfUpdateProgress(42, paused = true)
            root.updateNotification.obtainEvent(InAppNotificationStore.Intent.OnNewNotification(progress))
            runCurrent()
            root.updateNotification.obtainEvent(InAppNotificationStore.Intent.OpenNotificationScreen(progress))
            runCurrent()
            val review = assertIs<RootComponent.Child.Review>(root.childStack.value.active.instance)
            assertIs<RootReviewComponent.Child.Notification>(review.component.childStack.value.active.instance)
            assertSame(progress, assertIs<InAppNotificationStore.State.ShownNotification>(root.updateNotification.state.value).notification)
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test
    fun notificationSurvivesTabChangesAndOpensUpdateScreen() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val root = RootComponentBase(DefaultComponentContext(lifecycle))
            val notification = InAppNotification.SelfUpdateReady({}, "1.6.1", "changes", releaseId = "1.6.1.10422")
            root.updateNotification.obtainEvent(InAppNotificationStore.Intent.OnNewNotification(notification))
            runCurrent()
            val bottom = assertIs<RootComponent.Navigation.BottomBar>(root.slotChild.value.child?.instance)
            bottom.component.onClickTabBar(BottomBarConfiguration.Home)
            assertIs<RootComponent.Child.Starts>(root.childStack.value.active.instance)
            assertSame(notification, assertIs<InAppNotificationStore.State.ShownNotification>(root.updateNotification.state.value).notification)
            bottom.component.onClickTabBar(BottomBarConfiguration.Profile)
            assertIs<RootComponent.Child.Profile>(root.childStack.value.active.instance)
            assertSame(notification, assertIs<InAppNotificationStore.State.ShownNotification>(root.updateNotification.state.value).notification)
            root.updateNotification.obtainEvent(InAppNotificationStore.Intent.OpenNotificationScreen(notification))
            runCurrent()
            val review = assertIs<RootComponent.Child.Review>(root.childStack.value.active.instance)
            assertIs<RootReviewComponent.Child.Notification>(review.component.childStack.value.active.instance)
            assertSame(notification, assertIs<InAppNotificationStore.State.ShownNotification>(root.updateNotification.state.value).notification)
            root.updateNotification.obtainEvent(InAppNotificationStore.Intent.OnNewNotification(InAppNotification.SelfUpdateStarted))
            assertSame(InAppNotification.SelfUpdateStarted, assertIs<InAppNotificationStore.State.ShownNotification>(root.updateNotification.state.value).notification)
            root.updateNotification.obtainEvent(InAppNotificationStore.Intent.OnNewNotification(InAppNotification.SelfUpdateProgress(42)))
            assertIs<InAppNotification.SelfUpdateProgress>(assertIs<InAppNotificationStore.State.ShownNotification>(root.updateNotification.state.value).notification)
            assertEquals(emptyList(), assertIs<InAppNotificationStore.State.ShownNotification>(root.updateNotification.state.value).pending)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }
}
