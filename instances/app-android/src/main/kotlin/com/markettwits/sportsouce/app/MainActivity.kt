package com.markettwits.sportsouce.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
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

        // Configure window to handle display cutouts properly
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        installSplashScreen()
        val initialDeeplink = runBlocking {
            parseDeeplinkOrNull(intent)
        }

        val defaultComponentContext = defaultComponentContext()
        rootComponent = RootComponentBase(defaultComponentContext)
        themeComponent = ThemeComponentBase(defaultComponentContext)

        setContent {
            SportSauceTheme(
                component = themeComponent
            ) { isDarkTheme ->
                val currentColorScheme = MaterialTheme.colorScheme
                SideEffect {
                    sportSauceSystemBarColors(
                        isDarkTheme = isDarkTheme,
                        colorScheme = currentColorScheme
                    )
                }
                RootContent(component = rootComponent)
            }
        }

        initialDeeplink?.let { deeplink ->
            rootComponent.handleDeeplink(deeplink)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

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
