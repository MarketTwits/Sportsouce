package com.markettwits.core_ui.items.image

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory

actual fun PlatformContext.asyncImageLoader(): ImageLoader =
    ImageLoader
        .Builder(this)
        .components {
            add(KtorNetworkFetcherFactory())
            add(ProxyInterceptor())
        }
        .build()
