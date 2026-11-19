package com.markettwits

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class OriginalImageShareTest {
    private val first = "https://cdn.sportsauce.ru/live/photo-1.jpg"
    private val second = "https://cdn.sportsauce.ru/live/photo-2.png"

    @Test fun downloadsEveryOriginalBeforeOpeningTheShareSheet() = runTest {
        val events = mutableListOf<String>()
        val progress = mutableListOf<OriginalImageShareProgress>()
        val sharer = OriginalImageSharerBase { object : OriginalShareBatch {
            override suspend fun download(url: String, index: Int, onBytes: (Long) -> Unit): OriginalShareFile {
                events += url; onBytes(1234L + index)
                return OriginalShareFile(index.toString(), if (index == 0) "image/jpeg" else "image/png")
            }
            override suspend fun launch(files: List<OriginalShareFile>) {
                assertEquals(listOf(first, second), events)
                assertEquals(listOf("0", "1"), files.map { it.path })
                events += "share"
            }
            override fun discard() { events += "discard" }
        } }
        sharer.share(listOf("https://img.sportsauce.ru/insecure/rs:fit:48:48/q:40/plain/$first", second), progress::add)
        assertEquals(listOf(first, second, "share"), events)
        assertEquals(OriginalImageShareProgress(2, 2, 2469), progress.last())
    }

    @Test fun incompleteOrCancelledBatchesAreDiscardedAndNeverShared() = runTest {
        for (cancel in listOf(false, true)) {
            var shared = false; var discarded = false
            val sharer = OriginalImageSharerBase { object : OriginalShareBatch {
                override suspend fun download(url: String, index: Int, onBytes: (Long) -> Unit): OriginalShareFile {
                    if (index == 1) {
                        if (cancel) throw CancellationException("cancel") else error("truncated download")
                    }
                    return OriginalShareFile("original.jpg", "image/jpeg")
                }
                override suspend fun launch(files: List<OriginalShareFile>) { shared = true }
                override fun discard() { discarded = true }
            } }
            assertFails { sharer.share(listOf(first, second)) {} }
            assertFalse(shared); assertTrue(discarded)
        }
    }

    @Test fun proxyOptionsAndSpecialCharactersNeverLeakIntoOriginalDownload() {
        assertEquals(first, originalShareUrl(first))
        val original = "$first?token=a%20b&name=user@example.org"
        assertEquals(original, originalShareUrl("https://img.sportsauce.ru/insecure/rs:fit:480:480/plain/https://cdn.sportsauce.ru/live/photo-1.jpg%3Ftoken%3Da%2520b%26name%3Duser%40example.org@webp"))
        assertFails { originalShareUrl("file:///cache/preview.jpg") }
        assertFails { originalShareUrl("https://img.sportsauce.ru/insecure/encoded-image") }
    }

    @Test fun savingResolvesOriginalsAndPublishesOnlyTheCompleteBatchWithoutOpeningSharing() = runTest {
        val events = mutableListOf<String>()
        val progress = mutableListOf<OriginalImageShareProgress>()
        val downloader = OriginalImageDownloaderBase { object : OriginalShareBatch {
            override suspend fun download(url: String, index: Int, onBytes: (Long) -> Unit): OriginalShareFile {
                events += url; onBytes(500)
                return OriginalShareFile(index.toString(), "image/jpeg")
            }
            override suspend fun launch(files: List<OriginalShareFile>) = error("Must not open the share sheet")
            override suspend fun save(files: List<OriginalShareFile>): String {
                assertEquals(listOf(first, second), events)
                assertEquals(listOf("0", "1"), files.map { it.path })
                events += "save"
                return "Галерея · Sportsauce"
            }
            override fun discard() { events += "cleanup" }
        } }
        val result = downloader.download(listOf("https://img.sportsauce.ru/insecure/rs:fit:48:48/plain/$first", second, first), progress::add)
        assertEquals(OriginalImageDownloadResult(2, "Галерея · Sportsauce"), result)
        assertEquals(listOf(first, second, "save", "cleanup"), events)
        assertEquals(OriginalImageShareProgress(2, 2, 1000), progress.last())
    }

    @Test fun saveFailuresAndCancellationCleanTheBatchAndNeverReportSuccess() = runTest {
        for (failure in listOf("download", "cancel", "save")) {
            var discarded = false; var saved = false
            val downloader = OriginalImageDownloaderBase { object : OriginalShareBatch {
                override suspend fun download(url: String, index: Int, onBytes: (Long) -> Unit): OriginalShareFile {
                    if (index == 1) when (failure) {
                        "download" -> error("offline")
                        "cancel" -> throw CancellationException()
                    }
                    return OriginalShareFile(index.toString(), "image/jpeg")
                }
                override suspend fun launch(files: List<OriginalShareFile>) = error("unused")
                override suspend fun save(files: List<OriginalShareFile>): String { saved = true; error("disk full") }
                override fun discard() { discarded = true }
            } }
            assertFails { downloader.download(listOf(first, second)) {} }
            assertEquals(failure == "save", saved)
            assertTrue(discarded)
        }
    }
}
