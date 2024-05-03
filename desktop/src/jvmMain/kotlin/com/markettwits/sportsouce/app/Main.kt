package com.markettwits.sportsouce.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.root.RootComponentBase
import com.markettwits.root.RootContent
import com.markettwits.theme.theme.SportSauceTheme
import com.markettwits.theme.theme.component.ThemeComponentBase
import java.io.File
import java.util.Locale


fun main() {
    Locale.setDefault(Locale("ru", "RU"))
    InStorageCacheDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath
    InStorageFileDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath

    val lifecycle = LifecycleRegistry()
    val root =
        runOnUiThread { RootComponentBase(DefaultComponentContext(lifecycle)) }
    val theme = runOnUiThread {
        ThemeComponentBase(DefaultComponentContext(lifecycle))
    }

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
