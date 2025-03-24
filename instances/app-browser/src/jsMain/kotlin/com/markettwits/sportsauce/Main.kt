package com.markettwits.sportsauce

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.markettwits.initKoin
import com.markettwits.root.RootComponentBase
import com.markettwits.root.RootContent
import com.markettwits.theme.theme.SportSauceTheme
import com.markettwits.theme.theme.component.ThemeComponentBase
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