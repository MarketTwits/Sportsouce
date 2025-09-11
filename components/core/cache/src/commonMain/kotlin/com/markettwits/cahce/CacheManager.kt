package com.markettwits.cahce

interface CacheManager {
    suspend fun getCacheSize(): Long
    suspend fun clearCache(): Boolean
    fun formatCacheSize(sizeInBytes: Long): String
}

expect class CacheManagerImpl() : CacheManager {
    override suspend fun getCacheSize(): Long
    override suspend fun clearCache(): Boolean
    override fun formatCacheSize(sizeInBytes: Long): String
}