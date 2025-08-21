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
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext


class MainActivity : ComponentActivity() {

    private lateinit var rootComponent: RootComponentBase
    private lateinit var themeComponent: ThemeComponentBase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val initialDeeplink = runBlocking {
            parseDeeplinkOrNull(intent)
        }

        val defaultComponentContext = defaultComponentContext()
        rootComponent = RootComponentBase(
            componentContext = defaultComponentContext
        )
        themeComponent = ThemeComponentBase(componentContext = defaultComponentContext)

        // Set content once during onCreate
        setContent {
            SportSauceTheme(
                component = themeComponent
            ) { isDarkTheme ->
                SportSauceSystemBarColors(isDarkTheme)
                RootContent(component = rootComponent)
            }
        }

        // Handle initial deeplink after components are fully initialized
        initialDeeplink?.let { deeplink ->
            rootComponent.handleDeeplink(deeplink)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // Handle new intent deeplinks without recreating components
        lifecycleScope.launch {
            parseDeeplinkOrNull(intent)?.let { deeplink ->
                rootComponent.handleDeeplink(deeplink)
            }
        }
    }

    private suspend fun parseDeeplinkOrNull(intent: Intent): Deeplink.SportSauce? {
        return try {
            val deepLinkParser = GlobalContext.get().get<DeepLinkParser>()
            val deeplink = deepLinkParser.fromIntent(this, intent)
            deeplink as? Deeplink.SportSauce
        } catch (_: Exception) {
            null
        }
    }
}
