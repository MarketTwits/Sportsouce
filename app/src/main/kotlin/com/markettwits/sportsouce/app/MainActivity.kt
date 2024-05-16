package com.markettwits.sportsouce.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.root.RootComponentBase
import com.markettwits.root.RootContent
import com.markettwits.theme.theme.SportSauceTheme
import com.markettwits.theme.theme.component.ThemeComponentBase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
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
            decomposeComponentContext(defaultComponentContext)
            val context: ComponentContext by inject()
            val root = RootComponentBase(componentContext = context)
            val theme = ThemeComponentBase(componentContext = context)
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
