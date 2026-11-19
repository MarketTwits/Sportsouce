package com.markettwits.selfupdater.thirdparty.api

import android.content.Context
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.googleplay.api.SelfUpdaterGooglePlay
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationListener
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AndroidUpdaterWiringTest {
    private class Storage : InAppNotificationStorage {
        override fun subscribe(listener: InAppNotificationListener) = Unit
        override fun unsubscribe() = Unit
        override fun addNotification(notification: InAppNotification) = Unit
    }

    private fun setChannel(context: Context, channel: String) {
        val info = shadowOf(context.packageManager).getInternalMutablePackageInfo(context.packageName).applicationInfo!!
        info.metaData = Bundle().apply { putString("com.markettwits.selfupdater.CHANNEL", channel) }
    }

    @Test
    fun playFlavorResolvesWithoutGithubParserOrInstallerFactory() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        setChannel(context, "googlePlay")
        val app = koinApplication { modules(module { single<Context> { context }; single<InAppNotificationStorage> { Storage() } }, selfUpdaterThirdPartyModule) }
        try { assertIs<SelfUpdaterGooglePlay>(app.koin.get<SelfUpdaterSourceApi>()) } finally { app.close() }
    }

    @Test
    fun playInstallerWinsOverGithubMarkerWithoutResolvingGithubDependencies() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        setChannel(context, "github")
        shadowOf(context.packageManager).setInstallSourceInfo(context.packageName, "com.android.vending", "com.android.vending")
        val app = koinApplication { modules(module { single<Context> { context }; single<InAppNotificationStorage> { Storage() } }, selfUpdaterThirdPartyModule) }
        try { assertIs<SelfUpdaterGooglePlay>(app.koin.get<SelfUpdaterSourceApi>()) } finally { app.close() }
    }

    @Test
    fun githubFlavorUsesItsRegisteredFactoryAndMissingFactoryFailsClosed() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        setChannel(context, "github")
        val expected = object : SelfUpdaterSourceApi {
            override suspend fun checkUpdate(manual: Boolean) = SelfUpdateResult.NoUpdates
            override fun getInstallSourceName() = "test GitHub"
            override fun isSelfUpdateCanManualCheck() = true
        }
        val parser = object : SelfUpdateParserApi {
            override suspend fun getLastUpdate(): SelfUpdate? = null
            override fun getName() = "test"
        }
        val storage = Storage()
        var calls = 0
        val base = module { single<Context> { context }; single<SelfUpdateParserApi> { parser }; single<InAppNotificationStorage> { storage } }
        val factory = module {
            single<AndroidSideloadUpdaterFactory> {
                AndroidSideloadUpdaterFactory { actualContext, actualParser, actualStorage ->
                    assertSame(context, actualContext)
                    assertSame(parser, actualParser)
                    assertSame(storage, actualStorage)
                    calls++
                    expected
                }
            }
        }
        val app = koinApplication { modules(base, factory, selfUpdaterThirdPartyModule) }
        try {
            assertSame(expected, app.koin.get<SelfUpdaterSourceApi>())
            assertEquals(1, calls)
        } finally { app.close() }
        val missing = koinApplication { modules(base, selfUpdaterThirdPartyModule) }
        try {
            assertEquals(SelfUpdateResult.Unsupported, missing.koin.get<SelfUpdaterSourceApi>().checkUpdate(false))
        } finally { missing.close() }
    }

    @Test
    fun isolatedFeatureKoinUsesAndroidBindingsFromTheAppKoin() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        setChannel(context, "github")
        val expected = object : SelfUpdaterSourceApi {
            override suspend fun checkUpdate(manual: Boolean) = SelfUpdateResult.NoUpdates
            override fun getInstallSourceName() = "GitHub"
            override fun isSelfUpdateCanManualCheck() = true
        }
        val parser = object : SelfUpdateParserApi {
            override suspend fun getLastUpdate(): SelfUpdate? = null
            override fun getName() = "GitHub"
        }
        startKoin { modules(module {
            single<Context> { context }
            single<AndroidSideloadUpdaterFactory> { AndroidSideloadUpdaterFactory { _, _, _ -> expected } }
        }) }
        val app = koinApplication { modules(module {
            single<SelfUpdateParserApi> { parser }
            single<InAppNotificationStorage> { Storage() }
        }, selfUpdaterThirdPartyModule) }
        try {
            assertSame(expected, app.koin.get<SelfUpdaterSourceApi>())
        } finally {
            app.close()
            stopKoin()
        }
    }

    @Test
    fun isolatedPlayKoinUsesAppContextWithoutGithubBindings() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        setChannel(context, "googlePlay")
        startKoin { modules(module { single<Context> { context } }) }
        val app = koinApplication { modules(module {
            single<InAppNotificationStorage> { Storage() }
        }, selfUpdaterThirdPartyModule) }
        try {
            assertIs<SelfUpdaterGooglePlay>(app.koin.get<SelfUpdaterSourceApi>())
        } finally {
            app.close()
            stopKoin()
        }
    }

    @Test
    fun githubAppFactoryResolvesRealUpdaterAndOffersValidRelease() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        setChannel(context, "github")
        val release = SelfUpdate(
            version = "1.6.1.10422",
            name = "Sportsauce-1.6.1-10422.apk",
            downloadUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/Sportsauce-1.6.1-10422.apk",
            description = "Changes",
            sizeBytes = 100,
            sha256 = "a".repeat(64),
        )
        startKoin { modules(module {
            single<Context> { context }
            single<AndroidSideloadUpdaterFactory> {
                AndroidSideloadUpdaterFactory { actualContext, parser, notifications ->
                    SelfUpdaterThirdParty(actualContext, parser, notifications)
                }
            }
        }) }
        val app = koinApplication { modules(module {
            single<InAppNotificationStorage> { Storage() }
            single<SelfUpdateParserApi> { object : SelfUpdateParserApi {
                override suspend fun getLastUpdate() = release
                override fun getName() = "GitHub"
            } }
        }, selfUpdaterThirdPartyModule) }
        try {
            val updater = app.koin.get<SelfUpdaterSourceApi>()
            assertIs<SelfUpdaterThirdParty>(updater)
            assertEquals(SelfUpdateResult.SelfUpdateReady(release.version, release.description, releaseId = release.version),
                updater.checkUpdate(false))
        } finally {
            app.close()
            stopKoin()
        }
    }
}
