package com.markettwits.sportsouce.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.core.theme.SportSauceTheme
import com.markettwits.core.theme.component.ThemeComponentBase
import com.markettwits.sportsauce.deeplink.api.DeepLinkParser
import com.markettwits.sportsauce.deeplink.model.Deeplink
import com.markettwits.sportsouce.root.RootComponentBase
import com.markettwits.sportsouce.root.RootContent
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext


class MainActivity : ComponentActivity() {

    private var rootComponent: RootComponentBase? = null
    private var themeComponent: ThemeComponentBase? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        lifecycleScope.launch {
            var parsedDeeplink: Deeplink.SportSauce? = null

            intent?.let { currentIntent ->
                try {
                    val deepLinkParser = GlobalContext.get().get<DeepLinkParser>()
                    val deeplink = deepLinkParser.fromIntent(this@MainActivity, currentIntent)
                    if (deeplink is Deeplink.SportSauce) {
                        parsedDeeplink = deeplink
                    }
                } catch (_: Exception) {
                    // Failed to parse deeplink, continue without it
                }
            }

            // Create or update components
            if (rootComponent == null || themeComponent == null) {
                setupComponents(parsedDeeplink)
            } else {
                // App is already initialized, handle deeplink navigation within existing component
                parsedDeeplink?.let { deeplink ->
                    rootComponent?.handleDeeplink(deeplink)
                }
            }
        }
    }

    private fun setupComponents(deeplink: Deeplink.SportSauce?) {
        val defaultComponentContext = defaultComponentContext()
        rootComponent = RootComponentBase(
            componentContext = defaultComponentContext
        )
        themeComponent = ThemeComponentBase(componentContext = defaultComponentContext)

        // Handle initial deeplink after components are created
        deeplink?.let { initialDeeplink ->
            rootComponent?.handleDeeplink(initialDeeplink)
        }

        setContent {
            SportSauceTheme(
                component = themeComponent!!
            ) { isDarkTheme ->
                SportSauceSystemBarColors(isDarkTheme)
                RootContent(component = rootComponent!!)
            }
        }
    }

    override fun onDestroy() {
        rootComponent = null
        themeComponent = null
        super.onDestroy()
    }
}
