package com.markettwits.selfupdater.thirdparty.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ApkDownloadMetadataTest {
    private val release = SelfUpdate(
        version = "1.6.1.10422",
        name = "Sportsauce-1.6.1-10422.apk",
        downloadUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/Sportsauce-1.6.1-10422.apk",
        description = "",
        sizeBytes = 8892395,
        sha256 = "a".repeat(64),
    )

    @Test
    fun acceptsOnlyTheExactPublishedApk() {
        val metadata = assertNotNull(release.androidDownloadMetadata())
        assertEquals(10422L, metadata.version.buildNumber)
        assertEquals(release.name, metadata.fileName)
        assertEquals(release.sha256, metadata.sha256)
        listOf(
            release.copy(name = "../app.apk"),
            release.copy(name = "Sportsauce-1.6.1-10422.exe"),
            release.copy(version = "1.6.1.10422/../../other"),
            release.copy(downloadUrl = "http://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/${release.name}"),
            release.copy(downloadUrl = "https://example.com/${release.name}"),
            release.copy(downloadUrl = release.downloadUrl + "?redirect=other"),
            release.copy(sizeBytes = null),
            release.copy(sizeBytes = 0),
            release.copy(sha256 = null),
            release.copy(sha256 = "a".repeat(63)),
            release.copy(sha256 = "g".repeat(64)),
        ).forEach { assertNull(it.androidDownloadMetadata()) }
    }
}
