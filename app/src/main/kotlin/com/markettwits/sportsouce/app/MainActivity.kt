package com.markettwits.sportsouce.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.root.root.RootComponentBase
import com.markettwits.root.root.RootContent


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        InStorageCacheDirectory.path = cacheDir.path
        InStorageFileDirectory.path = filesDir.path
        val root = RootComponentBase(componentContext = defaultComponentContext())
        setContent {
            SportSouceTheme {
                RootContent(component = root)
            }
        }
    }
}