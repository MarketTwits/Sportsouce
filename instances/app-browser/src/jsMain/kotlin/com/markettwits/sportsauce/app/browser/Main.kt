package com.markettwits.sportsauce.app.browser

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.markettwits.core.theme.SportSauceTheme
import com.markettwits.core.theme.component.ThemeComponentBase
import com.markettwits.initKoin
import com.markettwits.sportsouce.root.RootComponentBase
import com.markettwits.sportsouce.root.RootContent
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val componentContext = DefaultComponentContext(lifecycle = lifecycle)
    InitPlatformLocaleForWeb()
    InitStorageForWeb()
    initKoin {
        val root = RootComponentBase(componentContext)
        val theme = ThemeComponentBase(componentContext)
        lifecycle.attachToDocument()
        onWasmReady {
            ComposeViewport(document.body ?: return@onWasmReady) {
                SportSauceTheme(theme) {
                    RootContent(root)
                }
            }
        }
    }
}