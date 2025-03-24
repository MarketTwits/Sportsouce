package com.markettwits.theme.theme.components

import coil3.PlatformContext
import coil3.intercept.Interceptor
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.ImageResult
import coil3.util.DebugLogger

internal fun PlatformContext.asyncImageLoader() =
    coil3.ImageLoader
        .Builder(this)
        .components {
            add(KtorNetworkFetcherFactory())
            add(ProxyInterceptor())
        }
        .logger(DebugLogger())
        .build()


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