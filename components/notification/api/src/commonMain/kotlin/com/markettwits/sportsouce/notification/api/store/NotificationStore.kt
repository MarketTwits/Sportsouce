package com.markettwits.sportsouce.notification.api.store

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.notification.api.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface NotificationStore : Store<NotificationStore.Intent, NotificationStore.State, NotificationStore.Label> {
    data class State(
        val feed: Flow<PagingData<InboxNotification>> = emptyFlow(),
        val unreadCount: Int = 0,
        val message: String? = null,
        val filter: NotificationFilter = NotificationFilter.All,
        val pending: Set<Int> = emptySet(),
        val readingAll: Boolean = false,
        val refreshing: Boolean = false,
        val refreshVersion: Int = 0,
        val pushPermission: PushPermissionState = PushPermissionState.Hidden,
    )
    sealed interface Intent {
        data object MessageShown : Intent
        data object Back : Intent
        data object SignIn : Intent
        data object Refresh : Intent
        data object Resume : Intent
        data object InboxChanged : Intent
        data object RefreshFinished : Intent
        data object ReadAll : Intent
        data object RequestPushPermission : Intent
        data object EnablePushOnDevice : Intent
        data object OpenPushSettings : Intent
        data class Filter(val filter: NotificationFilter) : Intent
        data class Read(val item: InboxNotification, val openStart: Boolean = false) : Intent
    }
    sealed interface Message {
        data class Count(val count: Int) : Message
        data class Error(val message: String?) : Message
        data class Feed(val flow: Flow<PagingData<InboxNotification>>) : Message
        data class Filter(val filter: NotificationFilter) : Message
        data class Pending(val ids: Set<Int>) : Message
        data class ReadingAll(val value: Boolean) : Message
        data class Refresh(val visible: Boolean) : Message
        data class Permission(val value: PushPermissionState) : Message
        data object RefreshFinished : Message
    }
    sealed interface Label {
        data object Back : Label
        data object SignIn : Label
        data class OpenStart(val id: Int) : Label
    }
}
