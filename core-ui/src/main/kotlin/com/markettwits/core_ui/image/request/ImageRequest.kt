package com.markettwits.core_ui.image.request

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest

@Composable
fun imageRequestCrossfade(model: Any?): ImageRequest =
    ImageRequest
        .Builder(LocalContext.current)
        .crossfade(true)
        .data(model)
        .build()
