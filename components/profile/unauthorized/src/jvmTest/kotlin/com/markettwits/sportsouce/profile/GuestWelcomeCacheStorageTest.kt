package com.markettwits.sportsouce.profile

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.sportsouce.unauthorized.data.*
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.*

class GuestWelcomeCacheStorageTest {
    @Test fun existingCachedJsonMigratesToPersistentStorageAndSurvivesReopening() = runBlocking {
        val root = Files.createTempDirectory("guest-config-cache").toFile()
        val oldCache = InStorageCacheDirectory.path
        val oldFiles = InStorageFileDirectory.path
        try {
            val temporary = root.resolve("cache").apply { mkdirs() }
            val permanent = root.resolve("files").apply { mkdirs() }
            InStorageCacheDirectory.path = temporary.path
            InStorageFileDirectory.path = permanent.path
            val config = parseGuestWelcomeConfig(java.io.File("../../../.github/content/guest-profile.json").readText())
            val entry = GuestWelcomeCacheEntry("https://example.test/config.json", 10, config, "etag-v1")
            val legacy = temporary.resolve("sportsouce.guest-welcome-v1")
            legacy.writeText(GuestWelcomeJson.encodeToString(entry))
            assertEquals(entry, GuestWelcomeCacheBase().read())
            assertTrue(permanent.resolve("sportsouce.guest-welcome-v1").exists())
            legacy.delete()
            val reopened = GuestWelcomeCacheBase()
            assertEquals(entry, reopened.read(), "Reopening must read persistent storage without the legacy file")
            val updated = entry.copy(fetchedAt = 20, etag = "etag-v2")
            reopened.write(updated)
            assertEquals(updated, GuestWelcomeCacheBase().read())
        } finally {
            InStorageCacheDirectory.path = oldCache
            InStorageFileDirectory.path = oldFiles
            root.deleteRecursively()
        }
    }
}
