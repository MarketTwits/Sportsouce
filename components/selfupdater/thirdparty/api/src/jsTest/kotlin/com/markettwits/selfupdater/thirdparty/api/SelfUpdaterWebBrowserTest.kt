package com.markettwits.selfupdater.thirdparty.api

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLScriptElement
import kotlin.js.Promise
import kotlin.test.*

class SelfUpdaterWebBrowserTest {
    @Test
    fun browserFetchUsesTheScriptSubpathBypassesCacheAndAbortsAfterCompletion() = CoroutineScope(Dispatchers.Default).promise {
        if (js("typeof window === 'undefined'") as Boolean) return@promise
        val script = document.createElement("script") as HTMLScriptElement
        script.type = "application/json"
        script.src = window.location.origin + "/Sportsouce/composeApp.js?v=1#module"
        document.head!!.appendChild(script)
        val original: dynamic = window.asDynamic().fetch
        var requested: String? = null
        var options: dynamic = null
        val location = window.location.href
        window.asDynamic().fetch = fun(url: dynamic, init: dynamic): Promise<dynamic> {
            requested = url as String
            options = init
            val response: dynamic = js("({ok:true})")
            response.text = { Promise.resolve("""{"schemaVersion":1,"sequence":12,"versionName":"1.6.1","buildNumber":10422,"changes":{"ru":"Изменения"}}""") }
            return Promise.resolve<dynamic>(response)
        }
        try {
            assertEquals(12L, fetchWebDeployment().sequence)
            assertTrue(checkNotNull(requested).startsWith(window.location.origin + "/Sportsouce/version.json?check="))
            assertEquals("no-store", options.cache as String)
            assertEquals(true, options.signal.aborted as Boolean)
            assertEquals(location, window.location.href, "Checking must never reload or navigate the open page")
        } finally {
            window.asDynamic().fetch = original
            script.parentNode?.removeChild(script)
        }
    }
    @Test
    fun browserFetchRejectsHttpFailureAndStillReleasesTheRequest() = CoroutineScope(Dispatchers.Default).promise {
        if (js("typeof window === 'undefined'") as Boolean) return@promise
        val script = document.createElement("script") as HTMLScriptElement
        script.type = "application/json"
        script.src = window.location.origin + "/composeApp.js"
        document.head!!.appendChild(script)
        val original: dynamic = window.asDynamic().fetch
        var options: dynamic = null
        window.asDynamic().fetch = fun(url: dynamic, init: dynamic): Promise<dynamic> {
            options = init
            return Promise.resolve<dynamic>(js("({ok:false})"))
        }
        try {
            assertFailsWith<IllegalStateException> { fetchWebDeployment() }
            assertEquals(true, options.signal.aborted as Boolean)
        } finally {
            window.asDynamic().fetch = original
            script.parentNode?.removeChild(script)
        }
    }
}
