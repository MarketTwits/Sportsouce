package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.models.ReleaseVersion

/** Only the exact APK belonging to the selected public release can be downloaded. */
data class ApkDownloadMetadata(
    val version: ReleaseVersion,
    val fileName: String,
    val url: String,
    val sizeBytes: Long,
    val sha256: String,
)

fun SelfUpdate.androidDownloadMetadata(): ApkDownloadMetadata? {
    val parsed = ReleaseVersion.fromTag(version) ?: return null
    val expectedName = "Sportsauce-${parsed.versionName}-${parsed.buildNumber}.apk"
    val expectedUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/$version/$expectedName"
    val size = sizeBytes?.takeIf { it > 0 } ?: return null
    val digest = sha256?.takeIf { it.length == 64 && it.all { char -> char in '0'..'9' || char in 'a'..'f' } } ?: return null
    if (name != expectedName || downloadUrl != expectedUrl) return null
    return ApkDownloadMetadata(parsed, expectedName, expectedUrl, size, digest)
}
