package com.markettwits.sportsouce.profile.upcoming

import com.markettwits.sportsouce.starts.cloud.SportSauceNetworkStartsApi
import com.markettwits.sportsouce.starts.cloud.model.StartStatusEntity

/** The registration response can contain a shortened start record. */
internal class UpcomingStartCatalog(private val api: SportSauceNetworkStartsApi) {
    suspend fun load(startIds: Set<Int>): Map<Int, UpcomingStartDetails> = buildMap {
        val remaining = startIds.toMutableSet()
        var offset = 0
        while (remaining.isNotEmpty()) {
            val page = api.fetchStarts(
                limit = 50,
                offset = offset,
                isGroup = false,
                statuses = StartStatusEntity.entries.filter { it != StartStatusEntity.ENDED },
            )
            for (start in page.rows) {
                if (remaining.remove(start.id)) {
                    val cover = start.posterLinkFile?.fullPath?.takeIf(String::isNotBlank)
                        ?: start.afishaLinkFile?.fullPath?.takeIf(String::isNotBlank)
                    put(start.id, UpcomingStartDetails(start.end_date, cover))
                }
            }
            offset += page.rows.size
            if (page.rows.isEmpty() || offset >= page.count) break
        }
    }
}

internal data class UpcomingStartDetails(val endDate: String? = null, val image: String? = null)
