package com.markettwits.cahce.domain.store

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath

inline fun <reified T : @Serializable Any> listStoreOfWrapper(
    path: String,
    fileName: String,
    default: List<T> = emptyList(),
    enableCache: Boolean = true,
    json: Json = Json { ignoreUnknownKeys = true; encodeDefaults = true },
): KStore<List<T>> {
    val fullPath = "$path/$fileName".toPath()
    return storeOf(fullPath, default, enableCache, json)
}
