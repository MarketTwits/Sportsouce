package com.markettwits.sportsouce.app.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.core.theme.SportSauceTheme
import com.markettwits.core.theme.component.ThemeComponentBase
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.initKoin
import com.markettwits.sportsouce.root.RootComponentBase
import com.markettwits.sportsouce.root.RootContent
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.util.Locale


fun main() {

    Locale.setDefault(Locale("ru", "RUS"))

    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    InStorageCacheDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath
    InStorageFileDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath

    val lifecycle = LifecycleRegistry()
    val context = DefaultComponentContext(lifecycle)
    initKoin {
        val root = runOnUiThread { RootComponentBase(context) }
        val theme = runOnUiThread { ThemeComponentBase(context) }
        application {
            val windowState = rememberWindowState()
            LifecycleController(lifecycle, windowState)
            Window(
                title = "Спорт Союз",
                icon = DefaultImages.SportSauceLightLogo(),
                onCloseRequest = { exitApplication() }
            ) {
                SportSauceTheme(theme) {
                    RootContent(root)
                }
            }
        }
    }
}
