package com.markettwits.sportsouce.unauthorized.data

import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random
import kotlin.time.Clock

internal class GuestWelcomeRepositoryBase(
    private val url: String,
    private val cache: GuestWelcomeCache,
    private val remote: GuestWelcomeRemoteSource,
    private val now: () -> Long = { Clock.System.now().toEpochMilliseconds() },
    private val choose: (Int) -> Int = { Random.nextInt(it) },
) : GuestWelcomeRepository {
    private val refreshLock = Mutex()
    private var nextAttemptAt = 0L

    override suspend fun cachedContent(): GuestWelcomeContent {
        if (!isPublicHttpsUrl(url)) return GuestWelcomeContent.Local
        return recover {
            val entry = cache.read()?.takeIf { it.source == url } ?: return@recover GuestWelcomeContent.Local
            val contents = entry.config.contents()
            contents.getOrNull(if (contents.isEmpty()) -1 else choose(contents.size)) ?: GuestWelcomeContent.Local
        } ?: GuestWelcomeContent.Local
    }

    override suspend fun refresh() {
        if (!isPublicHttpsUrl(url)) return
        refreshLock.withLock {
            val time = now()
            if (time < nextAttemptAt) return@withLock
            val previous = recover {
                cache.read()?.takeIf { it.source == url }?.also { it.config.contents() }
            }
            if (previous != null && time >= previous.fetchedAt && time - previous.fetchedAt < 6 * 60 * 60 * 1000L) return@withLock
            // Avoid retry storms when offline, but preserve the last working content indefinitely.
            nextAttemptAt = time + 5 * 60 * 1000L
            recover {
                val entry = when (val result = remote.revalidate(url, previous?.etag, previous?.lastModified)) {
                    is GuestWelcomeRemoteResult.Updated -> {
                        result.config.contents()
                        GuestWelcomeCacheEntry(url, time, result.config, result.etag, result.lastModified)
                    }
                    is GuestWelcomeRemoteResult.NotModified -> {
                        requireNotNull(previous).copy(fetchedAt = time,
                            etag = result.etag ?: previous.etag,
                            lastModified = result.lastModified ?: previous.lastModified)
                    }
                }
                cache.write(entry)
                nextAttemptAt = time + 6 * 60 * 60 * 1000L
            }
        }
    }

    private suspend fun <T> recover(block: suspend () -> T): T? = try { block() }
    catch (cancelled: CancellationException) { throw cancelled }
    catch (_: Exception) { null }
}
