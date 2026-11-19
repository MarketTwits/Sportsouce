package com.markettwits.sportsouce.profile.authorized.receipts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.cloud.model.receipts.PaidReceipt

@Composable
fun ReceiptsScreen(component: ReceiptsComponent) {
    val state by component.state.collectAsState()
    SauceScaffold(topBar = { TopBarWithClip(title = "Оплаченные квитанции", goBack = component.back) }) { padding ->
        AdaptivePane(Modifier.fillMaxSize().padding(padding)) {
            ReceiptsContent(state, component::reload, component.open)
        }
    }
}

@Composable
internal fun ReceiptsContent(state: ReceiptLoadState<List<PaidReceipt>>, reload: () -> Unit, open: (PaidReceipt) -> Unit) {
    PullToRefreshScreen(isRefreshing = state.loading && state.data != null, onRefresh = reload) {
        SauceLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item(key = "intro") {
                Text("Квитанции по завершённым регистрациям на старты.", style = MaterialTheme.typography.bodyMedium,
                    color = SauceTheme.colors.textSecondary)
            }
            state.error?.let { message -> item(key = "error") { ReceiptError(message, reload, state.loading) } }
            when {
                state.data == null && state.loading -> item(key = "loading") { ReceiptSkeletons() }
                state.data?.isEmpty() == true -> item(key = "empty") {
                    OnBackgroundCard {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Квитанций пока нет", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                            Text("Здесь появятся квитанции по завершённым регистрациям.",
                                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                        }
                    }
                }
                else -> items(state.data.orEmpty(), key = { "receipt-${it.id}" }) { receipt -> ReceiptCard(receipt) { open(receipt) } }
            }
        }
    }
}

@Composable
internal fun ReceiptCard(receipt: PaidReceipt, open: () -> Unit) {
    OnBackgroundCard(onClick = open) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(receipt.startDate.orEmpty(), Modifier.weight(1f), style = MaterialTheme.typography.bodySmall,
                    color = SauceTheme.colors.textMuted)
                Text(receiptAmount(receipt.amount), style = MaterialTheme.typography.titleMedium,
                    color = SauceTheme.colors.textPrimary)
            }
            Text(receipt.startName?.takeIf(String::isNotBlank) ?: "Старт", style = MaterialTheme.typography.titleSmall,
                color = SauceTheme.colors.textPrimary, maxLines = 2, overflow = TextOverflow.Ellipsis)
            val participants = receipt.participants.filter(String::isNotBlank).joinToString(", ")
            if (participants.isNotEmpty()) Text(participants, style = MaterialTheme.typography.bodySmall,
                color = SauceTheme.colors.textSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
            HorizontalDivider(color = SauceTheme.colors.border)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("№ ${receipt.orderNumber?.takeIf(String::isNotBlank) ?: receipt.id}", Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall, color = SauceTheme.colors.textMuted,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Открыть", style = MaterialTheme.typography.labelMedium, color = SauceTheme.colors.action)
                Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, Modifier.size(14.dp), tint = SauceTheme.colors.action)
            }
        }
    }
}

@Composable
fun ReceiptDocumentScreen(component: ReceiptDocumentComponent) {
    val state by component.state.collectAsState()
    SauceScaffold(topBar = { TopBarWithClip(title = "Квитанция", goBack = component.back) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.data != null -> ReceiptDocument(state.data!!, Modifier.fillMaxSize())
                state.loading -> Box(Modifier.padding(16.dp)) { ReceiptSkeletons(document = true) }
                state.error != null -> Box(Modifier.padding(16.dp)) {
                    ReceiptError(state.error!!, component::reload, state.loading, title = "Не удалось загрузить квитанцию")
                }
            }
        }
    }
}

@Composable
private fun ReceiptError(message: String, reload: () -> Unit, loading: Boolean, title: String = "Не удалось загрузить квитанции") {
    OnBackgroundCard {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            Text(message, Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            SauceButton("Повторить попытку", reload, variant = SauceButtonVariant.Outlined, enabled = !loading)
        }
    }
}

@Composable
internal fun ReceiptSkeletons(document: Boolean = false) {
    Column(Modifier.fillMaxWidth().semantics {
        contentDescription = if (document) "Загрузка квитанции" else "Загрузка квитанций"
        progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
    }, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(if (document) 1 else 4) {
            OnBackgroundCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SauceSkeleton(Modifier.fillMaxWidth(0.4f).height(16.dp))
                    SauceSkeleton(Modifier.fillMaxWidth(0.85f).height(20.dp))
                    SauceSkeleton(Modifier.fillMaxWidth().height(if (document) 300.dp else 32.dp))
                    SauceSkeleton(Modifier.fillMaxWidth(0.6f).height(16.dp))
                }
            }
        }
    }
}
