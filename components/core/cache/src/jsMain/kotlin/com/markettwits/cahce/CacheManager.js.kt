package com.markettwits.cahce

actual class CacheManagerImpl : CacheManager {

    actual override suspend fun getCacheSize(): Long {
        // Web platform doesn't support direct file system access
        // Could potentially use IndexedDB size calculation in the future
        return 0L
    }

    actual override suspend fun clearCache(): Boolean {
        // Web platform cache clearing would need different approach
        // For now, return true as cache clearing is not supported
        return true
    }

    actual override fun formatCacheSize(sizeInBytes: Long): String {
        return "Недоступно"
    }
}