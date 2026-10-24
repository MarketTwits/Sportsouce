package com.markettwits.sportsouce.profile.results.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import app.cash.paging.*
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.results.domain.*
import com.markettwits.sportsouce.profile.results.presentation.actions.ResultsAction
import com.markettwits.sportsouce.profile.results.presentation.component.MyResultsComponent
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore.*
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore.State

@Composable
fun MyResultsScreen(component: MyResultsComponent) {
    val state by component.state.collectAsState()
    SauceScaffold(topBar = { TopBarWithClip(title = "Мои результаты", goBack = { component.obtainEvent(Intent.Back) }) }) { padding ->
        AdaptivePane(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
            MyResultsContent(state, component::obtainEvent, component::openAction)
        }
    }
    ResultsActionSlot(component)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MyResultsContent(state: State, event: (Intent) -> Unit, action: (ResultsAction) -> Unit) {
    val feed = state.feed.collectAsLazyPagingItems()
    val grid = rememberLazyGridState()
    LaunchedEffect(state.feedRefreshVersion) { if (state.feedRefreshVersion > 0) feed.refresh() }
    PullToRefreshScreen(isRefreshing = state.summary != null && (state.summaryLoading || (state.feedRefreshVersion > 0 && feed.loadState.refresh is LoadStateLoading)),
        onRefresh = { event(Intent.Refresh) }) {
        SauceLazyVerticalGrid(GridCells.Adaptive(310.dp), Modifier.fillMaxSize(), state = grid,
            contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item("intro", span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Ваша история стартов и личный прогресс.", style = MaterialTheme.typography.bodySmall,
                        color = SauceTheme.colors.textSecondary)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SauceButton("Найти", { action(ResultsAction.Find) },
                            Modifier.semantics { contentDescription = "Найти результат" }, variant = SauceButtonVariant.Outlined, fillWidth = false)
                        SauceButton("Добавить", { action(ResultsAction.Add) },
                            Modifier.semantics { contentDescription = "Добавить свой результат" }, variant = SauceButtonVariant.Soft, fillWidth = false)
                        SauceButton("Запросы", { action(ResultsAction.Requests) },
                            Modifier.semantics { contentDescription = "Совпадения и запросы" }, variant = SauceButtonVariant.Text, fillWidth = false)
                    }
                }
            }
            state.summaryError?.let { message -> item("summary-error", span = { GridItemSpan(maxLineSpan) }) { ResultsError(message) { event(Intent.RetrySummary) } } }
            if (state.summary == null && state.summaryLoading) item("summary-loading", span = { GridItemSpan(maxLineSpan) }) { ResultsSkeleton() }
            state.summary?.let { summary ->
                item("summary", span = { GridItemSpan(maxLineSpan) }) { SummaryStrip(summary) }
                if (summary.categories.any { it.best != null }) item("bests-title", span = { GridItemSpan(maxLineSpan) }) { SectionTitle("Личные рекорды") }
                items(summary.categories.filter { it.best != null }, key = { "best-${it.key}" }) { category -> BestResultCard(category) { event(Intent.OpenStart(it)) } }
                if (summary.categories.isNotEmpty()) item("progress", span = { GridItemSpan(maxLineSpan) }) { ResultsProgressCard(state, event) }
            }
            item("history-title", span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionTitle("История результатов", state.count?.toString())
                    if (!state.summary?.categories.isNullOrEmpty()) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChipBase(selected = state.feedCategory == null, onClick = { event(Intent.FeedCategory(null)) }, label = "Все")
                        state.summary?.categories?.forEach { category -> FilterChipBase(selected = state.feedCategory == category.key,
                            onClick = { event(Intent.FeedCategory(category.key)) }, label = category.label) }
                    }
                }
            }
            when {
                feed.loadState.refresh is LoadStateLoading && feed.itemCount == 0 -> items(3, key = { "feed-loading-$it" }) { ResultsSkeleton() }
                feed.loadState.refresh is LoadStateError -> item("feed-error", span = { GridItemSpan(maxLineSpan) }) {
                    ResultsError("Не удалось загрузить историю результатов", feed::retry)
                }
                feed.itemCount == 0 && feed.loadState.refresh is LoadStateNotLoading -> item("empty", span = { GridItemSpan(maxLineSpan) }) {
                    OnBackgroundCard { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Здесь будет ваша история", style = MaterialTheme.typography.titleMedium)
                        Text("После участия в старте результат появится автоматически. Результат с другого соревнования можно добавить на проверку.", color = SauceTheme.colors.textSecondary)
                    } }
                }
            }
            items(feed.itemCount, key = { index -> "result-${feed.peek(index)?.key ?: index}" }) { index ->
                feed[index]?.let { result -> MyResultCard(result) { id -> event(if (result.kind == "result") Intent.OpenResult(id) else Intent.OpenStart(id)) } }
            }
            when (feed.loadState.append) {
                is LoadStateLoading -> item("append", span = { GridItemSpan(maxLineSpan) }) { ResultsSkeleton() }
                is LoadStateError -> item("append-error", span = { GridItemSpan(maxLineSpan) }) { ResultsError("Не удалось загрузить следующие результаты", feed::retry) }
                else -> Unit
            }
        }
    }
}

@Composable
internal fun SectionTitle(title: String, subtitle: String? = null) {
    Row(Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 2.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
        subtitle?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted) }
    }
}

@Composable
internal fun ResultsError(message: String, retry: () -> Unit) {
    OnBackgroundCard { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(message, color = SauceTheme.colors.textSecondary, style = MaterialTheme.typography.bodyMedium)
        SauceButton("Повторить попытку", retry, variant = SauceButtonVariant.Outlined)
    } }
}

@Composable
internal fun ResultsSkeleton() {
    OnBackgroundCard { Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SauceSkeleton(Modifier.fillMaxWidth(.35f).height(16.dp))
        SauceSkeleton(Modifier.fillMaxWidth(.8f).height(24.dp))
        SauceSkeleton(Modifier.fillMaxWidth().height(48.dp))
    } }
}
