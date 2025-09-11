package com.markettwits.cahce

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.log10
import kotlin.math.pow

actual class CacheManagerImpl : CacheManager {

    override suspend fun getCacheSize(): Long = withContext(Dispatchers.IO) {
        try {
            val cacheDir = File(InStorageCacheDirectory.path)
            if (cacheDir.exists() && cacheDir.isDirectory) {
                calculateDirectorySize(cacheDir)
            } else {
                0L
            }
        } catch (e: Exception) {
            0L
        }
    }

    override suspend fun clearCache(): Boolean = withContext(Dispatchers.IO) {
        try {
            val cacheDir = File(InStorageCacheDirectory.path)
            if (cacheDir.exists() && cacheDir.isDirectory) {
                deleteDirectoryContents(cacheDir)
            } else {
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun formatCacheSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0) return "0 B"

        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(sizeInBytes.toDouble()) / log10(1024.0)).toInt()
        val size = sizeInBytes / 1024.0.pow(digitGroups.toDouble())

        return "%.1f %s".format(size, units[digitGroups])
    }

    private fun calculateDirectorySize(directory: File): Long {
        var size = 0L
        try {
            directory.listFiles()?.forEach { file ->
                size += if (file.isDirectory) {
                    calculateDirectorySize(file)
                } else {
                    file.length()
                }
            }
        } catch (e: Exception) {
            // Ignore files that can't be accessed
        }
        return size
    }

    private fun deleteDirectoryContents(directory: File): Boolean {
        return try {
            directory.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    deleteDirectoryContents(file)
                    file.delete()
                } else {
                    file.delete()
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}