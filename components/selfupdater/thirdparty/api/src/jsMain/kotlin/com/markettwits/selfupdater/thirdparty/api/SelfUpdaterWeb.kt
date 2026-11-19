package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.buildkonfig.BuildKonfig
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.selfupdater.models.UpdateTarget
import com.markettwits.selfupdater.models.ReleaseNotesLocale
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.await
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.Json
import org.w3c.dom.HTMLScriptElement

/** Announces a deployment; checking and returning to the tab never reload the page. */
class SelfUpdaterWeb internal constructor(
    private val currentSequence: Long,
    private val currentBuild: Long,
    private val loadDeployment: suspend () -> WebDeployment,
    private val language: () -> String = { ReleaseNotesLocale.Default },
) : SelfUpdaterSourceApi {
    constructor() : this(BuildKonfig.WEB_DEPLOYMENT_SEQUENCE, BuildKonfig.APP_VERSION_NUMBER.toLong(),
        ::fetchWebDeployment)

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
        if (currentSequence <= 0 || currentBuild <= 0) return SelfUpdateResult.Unsupported
        return try {
            val deployment = loadDeployment()
            if (!deployment.isNewerThan(currentSequence, currentBuild)) SelfUpdateResult.NoUpdates
            else SelfUpdateResult.SelfUpdateReady(
                versionName = deployment.versionName,
                description = deployment.description(language()),
                informationalOnly = true,
                releaseId = "web:${deployment.sequence}",
            )
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            SelfUpdateResult.Error(IllegalStateException("Unable to check web deployment", failure))
        }
    }

    // A deployed page may remain open for days. Recheck when its tab returns to the foreground.
    override suspend fun resumeUpdate(): SelfUpdateResult = checkUpdate(false)
    override fun updateTarget() = UpdateTarget.Web
    override fun getInstallSourceName(): String = "Веб-версия"
    override fun isSelfUpdateCanManualCheck(): Boolean = false
}

internal fun webMetadataUrl(scriptUrl: String): String =
    scriptUrl.substringBefore('#').substringBefore('?').substringBeforeLast('/') + "/version.json"

internal suspend fun fetchWebDeployment(): WebDeployment {
    val script = (0 until document.scripts.length).asSequence()
        .mapNotNull { document.scripts.item(it) as? HTMLScriptElement }
        .firstOrNull { it.src.substringBefore('#').substringBefore('?').endsWith("/composeApp.js") }
        ?: error("Web application script was not found")
    val url = webMetadataUrl(script.src) + "?check=" + js("Date.now()")
    val controller: dynamic = js("new AbortController()")
    val options: dynamic = js("({ cache: 'no-store', credentials: 'same-origin' })")
    options.signal = controller.signal
    try {
        return withTimeoutOrNull(10_000) {
            val response = window.fetch(url, options).await()
            check(response.ok) { "Web deployment metadata is unavailable" }
            val text = response.text().await()
            require(text.length <= 131_072) { "Web deployment metadata is too large" }
            Json { ignoreUnknownKeys = true }.decodeFromString<WebDeployment>(text)
        } ?: error("Web deployment check timed out")
    } finally {
        // Timeout and a hidden/closed tab must not leave a detached fetch in flight.
        controller.abort()
    }
}
