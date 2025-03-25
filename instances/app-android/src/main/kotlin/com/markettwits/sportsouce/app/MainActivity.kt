package com.markettwits.sportsouce.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.core.theme.SportSauceTheme
import com.markettwits.core.theme.component.ThemeComponentBase
import com.markettwits.initKoin
import com.markettwits.sportsouce.root.RootComponentBase
import com.markettwits.sportsouce.root.RootContent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        initKoin {
            androidContext(applicationContext)
            val defaultComponentContext = defaultComponentContext()
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
        stopKoin()
        super.onDestroy()
    }
}
