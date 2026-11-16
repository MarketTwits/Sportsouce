package com.markettwits.sportsouce.unauthorized.domain

interface GuestWelcomeRepository {
    /** Only local storage is read here; opening the profile never waits for the network. */
    suspend fun cachedContent(): GuestWelcomeContent
    /** Saves validated updates. A screen showing local content can read them after this returns. */
    suspend fun refresh()
}

object LocalGuestWelcomeRepository : GuestWelcomeRepository {
    override suspend fun cachedContent() = GuestWelcomeContent.Local
    override suspend fun refresh() = Unit
}
