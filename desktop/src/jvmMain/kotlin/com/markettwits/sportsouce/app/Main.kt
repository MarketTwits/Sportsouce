package com.markettwits.sportsouce.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.initKoin
import com.markettwits.root.RootComponentBase
import com.markettwits.root.RootContent
import com.markettwits.theme.theme.SportSauceTheme
import com.markettwits.theme.theme.component.ThemeComponentBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.awt.FlowLayout
import java.awt.Frame
import java.awt.Label
import java.io.File
import java.util.Locale


@OptIn(ExperimentalCoroutinesApi::class)
fun main() {

    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    Locale.setDefault(Locale("ru", "RU"))
    InStorageCacheDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath
    InStorageFileDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath

    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        java.awt.Dialog(Frame(), e.message ?: "Error").apply {
            layout = FlowLayout()
            val label = Label(e.message)
            add(label)
            val button = java.awt.Button("OK").apply {
                addActionListener { dispose() }
            }
            add(button)
            setSize(300, 300)
            isVisible = true
        }
    }

    val lifecycle = LifecycleRegistry()
    val context = DefaultComponentContext(lifecycle)
    initKoin {
        val root = runOnUiThread { RootComponentBase(context) }
        val theme = runOnUiThread { ThemeComponentBase(context) }
        application {
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
