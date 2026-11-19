package com.markettwits.sportsouce.notification.impl.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowScreen
import com.markettwits.sportsouce.notification.api.NotificationComponent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.cash.paging.*
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.snackbar.*
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.sportsouce.starts.common.presentation.startStatusTone
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.notification.impl.domain.*
import com.markettwits.sportsouce.notification.api.model.*
import com.markettwits.sportsouce.notification.api.store.NotificationStore.*
import com.markettwits.sportsouce.notification.api.store.NotificationStore.State
import kotlinx.datetime.*
import kotlin.time.Instant

@Composable
fun NotificationScreen(component: NotificationComponent) {
    val auth by component.authSlot.subscribeAsState()
    val state by component.state.collectAsState()
    if (auth.child != null) RootAuthFlowScreen(auth.child!!.instance)
    else NotificationInboxScreen(state, component::obtainEvent)
}

@Composable
internal fun NotificationInboxScreen(state: State, event: (Intent) -> Unit) {
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.message) {
        state.message?.let { snackbar.showSnackbar(it, withDismissAction = true); event(Intent.MessageShown) }
    }
    SauceScaffold(
        topBar = { TopBarWithClip(title = "Уведомления", goBack = { event(Intent.Back) }) },
        snackbarHost = { SauceSnackbarHost(snackbar, tone = SauceSnackbarTone.Error) },
    ) { padding ->
        AdaptivePane(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
            NotificationContent(state, state.unreadCount, event)
        }
    }
}

@Composable
internal fun NotificationContent(state: State, unread: Int, event: (Intent) -> Unit) {
    val feed = state.feed.collectAsLazyPagingItems()
    val grid = rememberLazyGridState()
    LaunchedEffect(state.refreshVersion) {
        if (state.refreshVersion > 0) feed.refresh()
    }
    LaunchedEffect(state.filter) { grid.scrollToItem(0) }
    val error = (feed.loadState.refresh as? LoadStateError)?.error
    val guest = error is NotificationAuthRequired
    PullToRefreshScreen(isRefreshing = state.refreshing && feed.itemCount > 0,
        onRefresh = { event(Intent.Refresh) }) {
        SauceLazyVerticalGrid(
            columns = GridCells.Adaptive(340.dp), modifier = Modifier.fillMaxSize(), state = grid,
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item("intro", span = { GridItemSpan(maxLineSpan) }) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(if (unread > 0) "Новых уведомлений: $unread" else "Ваши уведомления",
                            style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                        Text("О ваших стартах, регистрациях и профиле", style = MaterialTheme.typography.bodySmall,
                            color = SauceTheme.colors.textSecondary)
                    }
                    if ((unread > 0 || feed.itemSnapshotList.items.any { !it.isRead }) && !guest) IconButton(onClick = { event(Intent.ReadAll) },
                        enabled = !state.readingAll && state.pending.isEmpty(), modifier = Modifier.clip(SauceShapes.button)) {
                        if (state.readingAll) CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                        else Icon(Icons.Outlined.DoneAll, "Прочитать все", tint = SauceTheme.colors.action)
                    }
                }
            }
            if (!guest) item("filters", span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(NotificationFilter.entries, key = { it.name }) { filter ->
                        FilterChipBase(selected = state.filter == filter, onClick = { event(Intent.Filter(filter)) },
                            label = if (filter == NotificationFilter.Unread && unread > 0) "${filter.label} · $unread" else filter.label)
                    }
                }
            }
            when {
                guest -> item("sign-in", span = { GridItemSpan(maxLineSpan) }) {
                    InboxMessage("Войдите в аккаунт", "Уведомления о ваших стартах и регистрациях появятся после входа.", "Войти", { event(Intent.SignIn) })
                }
                error != null -> item("error", span = { GridItemSpan(maxLineSpan) }) {
                    InboxMessage("Не удалось загрузить уведомления", "Проверьте подключение и попробуйте ещё раз.", "Повторить", feed::retry)
                }
                feed.itemCount == 0 && feed.loadState.refresh is LoadStateLoading -> items(4, key = { "skeleton-$it" }) { NotificationSkeleton() }
                feed.itemCount == 0 && feed.loadState.refresh is LoadStateNotLoading && feed.loadState.append !is LoadStateLoading -> item("empty", span = { GridItemSpan(maxLineSpan) }) {
                    InboxMessage(if (state.filter == NotificationFilter.Unread) "Всё прочитано" else "Пока нет уведомлений",
                        if (state.filter == NotificationFilter.All) "Сообщения о стартах, регистрациях и обновлениях появятся здесь."
                        else "В этом разделе пока нет уведомлений.")
                }
            }
            if (!guest) items(feed.itemCount, key = { index -> "notification-${feed.peek(index)?.id ?: index}" }) { index ->
                feed[index]?.let { item -> NotificationCard(item, item.id in state.pending || state.readingAll, event) }
            }
            when (feed.loadState.append) {
                is LoadStateLoading -> item("append", span = { GridItemSpan(maxLineSpan) }) { NotificationSkeleton() }
                is LoadStateError -> item("append-error", span = { GridItemSpan(maxLineSpan) }) {
                    InboxMessage("Не удалось загрузить следующие уведомления", "Уже загруженные сообщения доступны.", "Повторить", feed::retry)
                }
                else -> Unit
            }
        }
    }
}

@Composable
internal fun NotificationCard(item: InboxNotification, pending: Boolean, event: (Intent) -> Unit) {
    val colors = SauceTheme.colors
    val content = item.notification
    val statusColors = when (content.startStatusCode()?.let(::startStatusTone)) {
        SauceStatusTone.Neutral -> colors.neutral
        SauceStatusTone.Success -> colors.success
        SauceStatusTone.Warning -> colors.warning
        SauceStatusTone.Danger -> colors.danger
        null -> null
    }
    OnBackgroundCard(Modifier.padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = if (item.isRead) colors.surface else colors.actionSoft.copy(alpha = .45f), contentColor = colors.textPrimary)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(shape = SauceShapes.badge,
                    color = statusColors?.container ?: if (item.isRead) colors.surfaceSubtle else colors.actionSoft,
                    contentColor = statusColors?.content ?: if (item.isRead) colors.textMuted else colors.action) {
                    Icon(if (content.templateKind == "start_completed") Icons.Outlined.EmojiEvents else Icons.Outlined.Notifications,
                        null, Modifier.padding(8.dp).size(18.dp))
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(content.kindLabel, style = MaterialTheme.typography.labelMedium, color = statusColors?.content ?: colors.textSecondary)
                    Text(notificationDate(item.createdAt), style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                }
                if (!item.isRead) Box(Modifier.size(7.dp).background(colors.action, CircleShape))
            }
            Text(content.title, style = MaterialTheme.typography.titleSmall,
                fontWeight = if (item.isRead) FontWeight.Medium else FontWeight.SemiBold)
            content.startName?.let { Text(it, style = MaterialTheme.typography.labelLarge, color = colors.action) }
            if (content.text.isNotBlank()) Text(content.text, style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
            if (content.startId != null || !item.isRead) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End), verticalAlignment = Alignment.CenterVertically) {
                if (content.startId != null) SauceButton("Открыть старт", { event(Intent.Read(item, openStart = true)) },
                    modifier = Modifier.weight(1f, fill = false), variant = SauceButtonVariant.Soft, fillWidth = false, enabled = !pending, loading = pending)
                if (!item.isRead) IconButton(onClick = { event(Intent.Read(item)) }, enabled = !pending,
                    modifier = Modifier.clip(SauceShapes.button)) {
                    if (pending && content.startId == null) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                    else Icon(Icons.Outlined.Done, "Отметить прочитанным", tint = colors.action)
                }
            }
        }
    }
}

@Composable
private fun InboxMessage(title: String, description: String, action: String? = null, onAction: () -> Unit = {}) {
    OnBackgroundCard(Modifier.padding(horizontal = 16.dp)) {
        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Outlined.NotificationsNone, null, Modifier.size(32.dp), tint = SauceTheme.colors.action)
            Text(title, style = MaterialTheme.typography.titleMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            if (action != null) SauceButton(action, onAction, variant = SauceButtonVariant.Soft, fillWidth = false)
        }
    }
}

@Composable
private fun NotificationSkeleton() {
    OnBackgroundCard(Modifier.padding(horizontal = 16.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SauceSkeleton(Modifier.size(34.dp))
            SauceSkeleton(Modifier.fillMaxWidth(.75f).height(18.dp))
            SauceSkeleton(Modifier.fillMaxWidth().height(44.dp))
            SauceSkeleton(Modifier.align(Alignment.End).width(120.dp).height(40.dp))
        }
    }
}

internal fun notificationDate(value: String): String = try {
    val date = Instant.parse(value).toLocalDateTime(TimeZone.currentSystemDefault())
    "${date.dayOfMonth.toString().padStart(2, '0')}.${date.monthNumber.toString().padStart(2, '0')}.${date.year} · ${date.hour.toString().padStart(2, '0')}:${date.minute.toString().padStart(2, '0')}"
} catch (_: IllegalArgumentException) { "" }

private fun NotificationContent.startStatusCode(): Int? = when (templateKind) {
    "start_completed" -> 6
    "start_reminder", "registration_closed" -> 4
    "registration_open" -> 3
    "start_started" -> 5
    "start_postponed" -> 1
    else -> null
}
