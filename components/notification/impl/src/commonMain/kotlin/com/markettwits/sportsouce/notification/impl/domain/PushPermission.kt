package com.markettwits.sportsouce.notification.impl.domain

import com.markettwits.sportsouce.notification.api.model.PushPermissionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal interface PushPermission {
    val state: Flow<PushPermissionState>
    fun request()
    fun openSettings()
    fun enable() = Unit

    data object Unavailable : PushPermission {
        override val state = emptyFlow<PushPermissionState>()
        override fun request() = Unit
        override fun openSettings() = Unit
    }
}
