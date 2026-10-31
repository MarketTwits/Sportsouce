package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.selfupdater.models.UpdateTarget
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class SelfUpdaterWebTest {
    private val deployment = WebDeployment(1, 12, "1.6.1", 10422, mapOf("ru" to "Изменения", "en" to "Changes"))
    @Test
    fun newDeploymentIsInformationalEvenForAManualCheckAndCannotInstall() = runTest {
        val updater = SelfUpdaterWeb(11, 10421, { deployment })
        val result = assertIs<SelfUpdateResult.SelfUpdateReady>(updater.checkUpdate(true))
        assertTrue(result.informationalOnly)
        assertEquals("web:12", result.releaseId)
        assertEquals("Изменения", result.description)
        assertEquals(UpdateTarget.Web, updater.updateTarget())
        assertEquals(SelfUpdateResult.Unsupported, updater.installUpdate())
    }
    @Test
    fun returningToTabFindsNewDeploymentAndNetworkFailureCanBeRetried() = runTest {
        var published = deployment.copy(sequence = 11)
        var offline = false
        val updater = SelfUpdaterWeb(11, 10421, { if (offline) error("offline") else published }, { "en" })
        assertEquals(SelfUpdateResult.NoUpdates, updater.checkUpdate(false))
        offline = true
        assertIs<SelfUpdateResult.Error>(updater.resumeUpdate())
        offline = false; published = deployment
        assertIs<SelfUpdateResult.SelfUpdateReady>(updater.resumeUpdate())
    }
    @Test
    fun invalidOrRolledBackMetadataNeverOffersAnUpdate() = runTest {
        assertIs<SelfUpdateResult.Error>(SelfUpdaterWeb(11, 10421, { deployment.copy(schemaVersion = 2) }, { "en" }).checkUpdate(false))
        assertEquals(SelfUpdateResult.NoUpdates, SelfUpdaterWeb(11, 10423, { deployment }, { "en" }).checkUpdate(false))
        assertEquals(SelfUpdateResult.Unsupported, SelfUpdaterWeb(0, 10421, { error("must not fetch in local build") }, { "en" }).checkUpdate(false))
    }
    @Test
    fun leavingTheTabPropagatesCancellation() = runTest {
        assertFailsWith<CancellationException> {
            SelfUpdaterWeb(11, 10421, { throw CancellationException("hidden") }, { "en" }).checkUpdate(false)
        }
    }
    @Test
    fun metadataIsResolvedBesideTheScriptIncludingSubpathQueryAndFragment() {
        assertEquals("https://example.com/Sportsouce/version.json", webMetadataUrl("https://example.com/Sportsouce/composeApp.js?v=1#module"))
        assertEquals("https://example.com/version.json", webMetadataUrl("https://example.com/composeApp.js"))
    }
}
