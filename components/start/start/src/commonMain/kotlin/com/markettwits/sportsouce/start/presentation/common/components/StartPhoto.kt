package com.markettwits.sportsouce.start.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.*

/** The same photo, loading and unavailable surfaces in the preview, selector and gallery. */
@Composable
internal fun StartPhoto(imageUrl: String, modifier: Modifier = Modifier) {
    if (imageUrl.isBlank()) {
        StartPhotoPlaceholder(modifier)
        return
    }
    SubcomposeAsyncImage(model = imageRequestCrossfade(imageUrl), contentDescription = null,
        contentScale = ContentScale.Crop, modifier = modifier.background(SauceTheme.colors.surfaceSubtle),
        loading = { Box(Modifier.fillMaxSize().shimmer().background(SauceTheme.colors.surfaceSubtle)) },
        error = { StartPhotoPlaceholder(Modifier.fillMaxSize()) },
        success = { SubcomposeAsyncImageContent(Modifier.fillMaxSize()) })
}

@Composable
private fun StartPhotoPlaceholder(modifier: Modifier) {
    Box(modifier.background(SauceTheme.colors.surfaceSubtle), contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.medium), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            Icon(Icons.Outlined.ImageNotSupported, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textMuted)
            Text("Фото недоступно", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall,
                color = SauceTheme.colors.textMuted, textAlign = TextAlign.Center)
        }
    }
}
