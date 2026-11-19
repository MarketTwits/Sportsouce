package com.markettwits.selfupdater.models

/** Version names are for display; the monotonically increasing build number orders releases. */
data class ReleaseVersion(
    val versionName: String,
    val buildNumber: Long,
) {
    companion object {
        /** GitHub tags use `<major>.<minor>.<patch>.<build>`, optionally prefixed with `v`. */
        fun fromTag(tag: String): ReleaseVersion? {
            val parts = tag.removePrefix("v").split('.')
            if (parts.size != 4 || parts.any { it.isEmpty() || it.any { char -> char !in '0'..'9' } }) {
                return null
            }
            val versionParts = parts.take(3).map { it.toIntOrNull() ?: return null }
            val build = parts[3].toLongOrNull()?.takeIf { it > 0 } ?: return null
            return ReleaseVersion(versionParts.joinToString("."), build)
        }
    }
}
