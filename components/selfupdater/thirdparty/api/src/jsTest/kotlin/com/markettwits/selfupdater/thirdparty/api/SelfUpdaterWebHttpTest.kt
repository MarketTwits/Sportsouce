package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import org.w3c.dom.HTMLScriptElement
import kotlin.js.Promise
import kotlin.test.*

/** Chrome → Karma HTTP server → production DOM/Fetch adapter, without replacing fetch. */
class SelfUpdaterWebHttpTest {
    private fun browserTest(scenario: String, block: suspend () -> Unit): Promise<Unit> =
        CoroutineScope(Dispatchers.Default).promise {
            if (js("typeof window === 'undefined'") as Boolean) return@promise
            val location = window.location.href
            val script = document.createElement("script") as HTMLScriptElement
            script.type = "application/json"
            script.src = window.location.origin + "/__updater_qa__/$scenario/composeApp.js?v=old#module"
            document.head!!.appendChild(script)
            try {
                block()
                assertEquals(location, window.location.href, "Update checks must not navigate or reload the site")
            } finally { script.parentNode?.removeChild(script) }
        }

    private fun updater() = SelfUpdaterWeb(11, 10421, ::fetchWebDeployment)

    @Test
    fun readsRealHttpResponseFromScriptSubdirectoryAndOnlyOffersManualRefresh() = browserTest("valid") {
        val updater = updater()
        val offer = assertIs<SelfUpdateResult.SelfUpdateReady>(updater.checkUpdate(false))
        assertEquals("web:12", offer.releaseId)
        assertEquals("Исправления браузерной версии", offer.description)
        assertTrue(offer.informationalOnly)
        assertEquals(SelfUpdateResult.Unsupported, updater.installUpdate())
    }

    @Test
    fun foregroundCheckReadsChangedMetadataEvenWhenTheServerAllowsCaching() = browserTest("changing") {
        val updater = updater()
        assertEquals("web:12", assertIs<SelfUpdateResult.SelfUpdateReady>(updater.checkUpdate(false)).releaseId)
        assertEquals("web:13", assertIs<SelfUpdateResult.SelfUpdateReady>(updater.resumeUpdate()).releaseId)
    }

    @Test
    fun serverRollbackStopsAdvertisingANewerDeployment() = browserTest("rollback") {
        val updater = updater()
        assertIs<SelfUpdateResult.SelfUpdateReady>(updater.checkUpdate(false))
        assertEquals(SelfUpdateResult.NoUpdates, updater.resumeUpdate())
    }

    @Test
    fun rejectsHttp503() = browserTest("unavailable") { assertIs<SelfUpdateResult.Error>(updater().checkUpdate(false)) }

    @Test
    fun rejectsHtmlFallbackReturnedWithHttp200() = browserTest("html") { assertIs<SelfUpdateResult.Error>(updater().checkUpdate(false)) }

    @Test
    fun rejectsTruncatedJson() = browserTest("malformed") { assertIs<SelfUpdateResult.Error>(updater().checkUpdate(false)) }

    @Test
    fun rejectsOversizedResponse() = browserTest("oversized") { assertIs<SelfUpdateResult.Error>(updater().checkUpdate(false)) }

    @Test
    fun rejectsUnsupportedSchema() = browserTest("schema") { assertIs<SelfUpdateResult.Error>(updater().checkUpdate(false)) }

    @Test
    fun timesOutAnUnresponsiveHttpServer() = browserTest("slow") {
        val result = assertIs<SelfUpdateResult.Error>(updater().checkUpdate(false))
        assertTrue(result.exception.cause?.message.orEmpty().contains("timed out"))
    }

    @Test
    fun cancellingARealHttpRequestAbortsFetchWithoutTurningCancellationIntoAnError() = browserTest("cancel") {
        val original: dynamic = window.asDynamic().fetch
        val started = CompletableDeferred<Unit>()
        var signal: dynamic = null
        window.asDynamic().fetch = fun(url: dynamic, options: dynamic): Promise<dynamic> {
            signal = options.signal
            val response = original.call(window, url, options)
            started.complete(Unit)
            return response
        }
        try {
            coroutineScope {
                val request = async { updater().checkUpdate(false) }
                started.await()
                request.cancelAndJoin()
                assertTrue(request.isCancelled)
                assertEquals(true, signal.aborted as Boolean)
            }
        } finally { window.asDynamic().fetch = original }
    }
}
