package com.markettwits.selfupdater.googleplay.api

internal enum class PlayAvailability { Available, InProgress, NotAvailable, Unknown }

internal data class PlayUpdateSnapshot<T>(
    val handle: T,
    val availability: PlayAvailability,
    val immediateAllowed: Boolean,
    val downloaded: Boolean,
)

internal interface PlayUpdateGateway<T> {
    suspend fun getUpdateInfo(): PlayUpdateSnapshot<T>
    fun startImmediateUpdate(handle: T): Boolean
}

internal enum class PlayCheckResult { Started, ReadyToComplete, NoUpdate, Unavailable }

/** Each call obtains a fresh Play update token. The chosen flow and its permission always match. */
internal class GooglePlayUpdateController<T>(private val gateway: PlayUpdateGateway<T>) {
    suspend fun check(): PlayCheckResult {
        val info = gateway.getUpdateInfo()
        if (info.downloaded) return PlayCheckResult.ReadyToComplete
        return when (info.availability) {
            PlayAvailability.Available -> {
                if (!info.immediateAllowed) PlayCheckResult.Unavailable
                else if (gateway.startImmediateUpdate(info.handle)) PlayCheckResult.Started
                else PlayCheckResult.Unavailable
            }
            PlayAvailability.InProgress ->
                if (gateway.startImmediateUpdate(info.handle)) PlayCheckResult.Started
                else PlayCheckResult.Unavailable
            PlayAvailability.NotAvailable -> PlayCheckResult.NoUpdate
            PlayAvailability.Unknown -> PlayCheckResult.Unavailable
        }
    }

    /** Resume only an existing Play flow; foregrounding must not start a new offer. */
    suspend fun resume(): PlayCheckResult {
        val info = gateway.getUpdateInfo()
        return when {
            info.downloaded -> PlayCheckResult.ReadyToComplete
            info.availability == PlayAvailability.InProgress ->
                if (gateway.startImmediateUpdate(info.handle)) PlayCheckResult.Started else PlayCheckResult.Unavailable
            else -> PlayCheckResult.NoUpdate
        }
    }
}
