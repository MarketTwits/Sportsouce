package com.markettwits.selfupdater.googleplay.api

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallErrorCode
import com.google.android.play.core.install.model.InstallStatus
import com.markettwits.activityholder.CurrentActivityHolder
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationListener
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SelfUpdaterGooglePlayTest {
    private class Storage : InAppNotificationStorage {
        val items = mutableListOf<InAppNotification>()
        override fun subscribe(listener: InAppNotificationListener) = Unit
        override fun unsubscribe() = Unit
        override fun addNotification(notification: InAppNotification) { items += notification }
    }

    @Test
    fun activityHolderDoesNotKeepPausedActivityOrClearANewerOne() {
        val first = Robolectric.buildActivity(Activity::class.java).setup()
        val second = Robolectric.buildActivity(Activity::class.java).setup()
        try {
            CurrentActivityHolder.onActivityResumed(first.get())
            CurrentActivityHolder.onActivityResumed(second.get())
            CurrentActivityHolder.onActivityPaused(first.get())
            assertEquals(second.get(), CurrentActivityHolder.getCurrentActivity())
            CurrentActivityHolder.onActivityPaused(second.get())
            assertEquals(null, CurrentActivityHolder.getCurrentActivity())
        } finally {
            first.pause().stop().destroy()
            second.pause().stop().destroy()
        }
    }

    @Test
    fun availablePlayUpdateStartsTheImmediateFlowWithTheResumedActivity() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val activity = controller.get()
        CurrentActivityHolder.onActivityResumed(activity)
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val fake = FakeAppUpdateManager(context)
            fake.setUpdateAvailable(100)
            val updater = SelfUpdaterGooglePlay(context, Storage(), fake)
            assertEquals(SelfUpdateResult.Complete, updater.checkUpdate(false))
            assertTrue(fake.isImmediateFlowVisible)
            assertEquals(AppUpdateType.IMMEDIATE, fake.typeForUpdateInProgress)
        } finally {
            CurrentActivityHolder.onActivityPaused(activity)
            controller.pause().stop().destroy()
            Dispatchers.resetMain()
        }
    }

    @Test
    fun unavailableActivityDoesNotConsumeUpdateAndRetryStartsPlayFlow() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val activity = controller.get()
        CurrentActivityHolder.onActivityPaused(activity)
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val fake = FakeAppUpdateManager(context)
            fake.setUpdateAvailable(100)
            val updater = SelfUpdaterGooglePlay(context, Storage(), fake)
            assertFailsWith<IllegalStateException> { updater.checkUpdate(false) }
            assertEquals(false, fake.isImmediateFlowVisible)
            CurrentActivityHolder.onActivityResumed(activity)
            assertEquals(SelfUpdateResult.Complete, updater.checkUpdate(false))
            assertTrue(fake.isImmediateFlowVisible)
            assertEquals(AppUpdateType.IMMEDIATE, fake.typeForUpdateInProgress)
        } finally {
            CurrentActivityHolder.onActivityPaused(activity)
            controller.pause().stop().destroy()
            Dispatchers.resetMain()
        }
    }

    @Test
    fun cancelledImmediateUpdateDoesNotRestartOnResumeAndCanBeRetried() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val activityController = Robolectric.buildActivity(Activity::class.java).setup()
        val activity = activityController.get()
        CurrentActivityHolder.onActivityResumed(activity)
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val fake = FakeAppUpdateManager(context)
            fake.setUpdateAvailable(100)
            val updater = SelfUpdaterGooglePlay(context, Storage(), fake)
            assertEquals(SelfUpdateResult.Complete, updater.checkUpdate(false))
            assertTrue(fake.isImmediateFlowVisible)
            fake.userRejectsUpdate()
            assertEquals(false, fake.isImmediateFlowVisible)
            assertEquals(SelfUpdateResult.NoUpdates, updater.resumeUpdate())
            assertEquals(false, fake.isImmediateFlowVisible)
            assertEquals(SelfUpdateResult.Complete, updater.checkUpdate(true))
            assertTrue(fake.isImmediateFlowVisible)
            assertEquals(AppUpdateType.IMMEDIATE, fake.typeForUpdateInProgress)
        } finally {
            CurrentActivityHolder.onActivityPaused(activity)
            activityController.pause().stop().destroy()
            Dispatchers.resetMain()
        }
    }

    @Test
    fun downloadedUpdateReportsReadyAndRetriesFailureOnlyFromResumedActivity() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val activity = controller.get()
        CurrentActivityHolder.onActivityResumed(activity)
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val fake = FakeAppUpdateManager(context)
            fake.setUpdateAvailable(100)
            assertTrue(fake.startUpdateFlowForResult(fake.appUpdateInfo.result, AppUpdateType.FLEXIBLE, activity, 1))
            fake.userAcceptsUpdate()
            fake.downloadStarts()
            fake.downloadCompletes()
            val storage = Storage()
            val updater = SelfUpdaterGooglePlay(context, storage, fake)
            assertEquals(SelfUpdateResult.ReadyToInstall, updater.resumeUpdate())
            assertEquals(SelfUpdateResult.ReadyToInstall, updater.resumeUpdate())
            assertEquals(1, storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().size)

            storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().last().onDismiss()
            assertEquals(SelfUpdateResult.ReadyToInstall, updater.resumeUpdate())
            assertEquals(2, storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().size)

            fake.setInstallErrorCode(InstallErrorCode.ERROR_INTERNAL_ERROR)
            storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().last().action()
            runCurrent()
            assertEquals(1, storage.items.filterIsInstance<InAppNotification.SelfUpdateError>().size)
            assertEquals(3, storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().size)

            fake.setInstallErrorCode(InstallErrorCode.NO_ERROR)
            CurrentActivityHolder.onActivityPaused(activity)
            storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().last().action()
            runCurrent()
            assertEquals(2, storage.items.filterIsInstance<InAppNotification.SelfUpdateError>().size)
            assertEquals(4, storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().size)
            CurrentActivityHolder.onActivityResumed(activity)
            storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().last().action()
            runCurrent()
            assertEquals(InstallStatus.INSTALLING, fake.appUpdateInfo.result.installStatus())
        } finally {
            CurrentActivityHolder.onActivityPaused(activity)
            controller.pause().stop().destroy()
            Dispatchers.resetMain()
        }
    }
}
