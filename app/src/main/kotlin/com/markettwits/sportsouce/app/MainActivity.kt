package com.markettwits.sportsouce.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.root.root.RootComponentBase
import com.markettwits.root.root.RootContent
import com.markettwits.theme.theme.SportSauceTheme
import com.markettwits.theme.theme.component.ThemeComponentBase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        InStorageCacheDirectory.path = cacheDir.path
        InStorageFileDirectory.path = filesDir.path
        val defaultComponentContext = defaultComponentContext()
        initKoin {
            androidContext(applicationContext)
            val root = RootComponentBase(componentContext = defaultComponentContext)
            val theme = ThemeComponentBase(componentContext = defaultComponentContext)
            setContent {
                SportSauceTheme(
                    component = theme
                ) { isDarkTheme ->
                    SportSauceSystemBarColors(isDarkTheme)
                    RootContent(component = root)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}
