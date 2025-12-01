package com.markettwits.core_ui.items.image

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.intercept.Interceptor
import coil3.request.ImageResult

expect fun PlatformContext.asyncImageLoader(): ImageLoader


internal class ProxyInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val data = request.data

        if (data is String && (data.startsWith("http://") || data.startsWith("https://"))) {
            val proxiedUrl = "https://wsrv.nl/?url=$data"
            val newRequest = chain.request.newBuilder()
                .data(proxiedUrl)
                .build()

            return chain.withRequest(newRequest).proceed()
        }
        return chain.proceed()
    }
}