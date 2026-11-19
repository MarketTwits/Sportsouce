package com.markettwits.sportsouce.unauthorized.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.markettwits.core_ui.items.theme.DarkSauceColors
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.preloadCachedAlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent

/** No bundled bitmap: cached CDN photos reveal over a lightweight design-system backdrop. */
@Composable
internal fun GuestProfileBackground(
    content: GuestWelcomeContent,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = SingletonImageLoader.get(LocalPlatformContext.current),
) {
    Box(modifier.drawWithCache {
        val backdrop = Brush.radialGradient(
            colors = listOf(DarkSauceColors.actionSoft, DarkSauceColors.background),
            center = Offset(size.width * .75f, size.height * .3f),
            radius = size.maxDimension,
        )
        onDrawBehind { drawRect(backdrop) }
    }) {
        val url = content.imageUrl
        if (!url.isNullOrBlank()) {
            val context = LocalPlatformContext.current
            val cached by produceState<Boolean?>(null, url, imageLoader, context) {
                value = preloadCachedAlbumImage(url, AlbumImageSize.Cover, imageLoader, context)
            }
            // Finish the cache-only read first: a saved original must not start another proxy request.
            if (cached != null) AlbumImage(
                url = url,
                size = AlbumImageSize.Cover,
                modifier = Modifier.fillMaxSize(),
                alignment = BiasAlignment(content.imageAlignmentX, content.imageAlignmentY),
                // Race the independent CDN only on a cold visit; decoding stays bounded to 960 px.
                raceOriginal = cached == false,
                loading = {},
                error = { _, _ -> },
                imageLoader = imageLoader,
            )
        }
    }
}
