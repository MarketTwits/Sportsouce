package com.markettwits

import InitPlatformLocaleForWeb
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.root.RootComponentBase
import com.markettwits.root.RootContent
import com.markettwits.theme.theme.SportSauceTheme
import com.markettwits.theme.theme.component.ThemeComponentBase
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady
import web.dom.DocumentVisibilityState
import web.events.EventType

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val context = DefaultComponentContext(lifecycle = lifecycle)
    InitPlatformLocaleForWeb()
    initKoin {
        val root = RootComponentBase(context)
        val theme = ThemeComponentBase(context)
        lifecycle.attachToDocument()

        onWasmReady {
            InStorageCacheDirectory.path = "data/com.markettwits.sportsauce"
            InStorageFileDirectory.path = "cache/com.markettwits.sportsauce"
            ComposeViewport(document.body ?: return@onWasmReady) {
                SportSauceTheme(theme) {
                    RootContent(root)
                }
            }
        }
    }
}

private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (web.dom.document.visibilityState == DocumentVisibilityState.visible) {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    web.dom.document.addEventListener(
        type = EventType("visibilitychange"),
        callback = { onVisibilityChanged() })
}