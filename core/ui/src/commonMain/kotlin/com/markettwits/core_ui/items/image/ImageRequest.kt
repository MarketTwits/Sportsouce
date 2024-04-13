package com.markettwits.core_ui.items.image

import androidx.compose.runtime.Composable
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun imageRequestCrossfade(model: Any?): ImageRequest =
    ImageRequest
        .Builder(LocalPlatformContext.current)
        .crossfade(true)
        .data(model)
        .build()
