package com.markettwits.core_ui.items.components.commerce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.SauceTheme

/** Product photography keeps its original light studio background in either theme. */
@Composable
fun SauceProductImage(url: String?, modifier: Modifier = Modifier) {
    Box(modifier.background(SauceTheme.colors.surfaceSubtle), contentAlignment = Alignment.Center) {
        if (url.isNullOrBlank()) ProductImageFallback()
        else SubcomposeAsyncImage(model = imageRequestCrossfade(url), contentDescription = null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit,
            loading = { SauceSkeleton(Modifier.fillMaxSize()) },
            success = { SubcomposeAsyncImageContent(Modifier.fillMaxSize().background(Color.White)) },
            error = { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { ProductImageFallback() } })
    }
}

@Composable
private fun ProductImageFallback() {
    Icon(Icons.Default.Photo, null, Modifier.size(32.dp), tint = SauceTheme.colors.textMuted)
}
