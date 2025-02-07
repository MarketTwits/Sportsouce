package com.markettwits.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.Typography
import com.markettwits.theme.theme.component.ThemeComponent
import com.markettwits.theme.theme.components.isDark
import com.markettwits.theme.theme.components.systemColorPallet

@Composable
fun SportSauceTheme(
    component: ThemeComponent,
    content: @Composable (Boolean) -> Unit
) {
    val theme = component.getAppTheme().collectAsState()
    val isDark = isDark(theme = theme.value)
    val palette = systemColorPallet(theme = theme.value, systemIsDark = isSystemInDarkTheme())

    SportSauceTheme(
        isDark = isDark,
        colorScheme = palette,
        content = { content(isDark) }
    )
}


@Composable
private fun SportSauceTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {

    setSingletonImageLoaderFactory { context ->
        context.asyncImageLoader()
    }

    MaterialTheme(
        shapes = Shapes(medium = RoundedCornerShape(size = 10.dp)),
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        CompositionLocalProvider(LocalDarkOrLightTheme provides isDark, content = content)
    }
}

private fun PlatformContext.asyncImageLoader() =
    coil3.ImageLoader
        .Builder(this)
        .components { add(KtorNetworkFetcherFactory()) }
        .crossfade(true)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(this, 0.25)
                .strongReferencesEnabled(true)
                .build()
        }
        .logger(DebugLogger())
        .build()

