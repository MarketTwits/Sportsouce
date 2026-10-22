package com.markettwits.sportsouce.unauthorized.domain

interface GuestWelcomeRepository {
    /** Only local storage is read here; opening the profile never waits for the network. */
    suspend fun cachedContent(): GuestWelcomeContent
    /** Validated updates are saved for the next welcome visit, never published into an active one. */
    suspend fun refresh()
}

object LocalGuestWelcomeRepository : GuestWelcomeRepository {
    override suspend fun cachedContent() = GuestWelcomeContent.Local
    override suspend fun refresh() = Unit
}
