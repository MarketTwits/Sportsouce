package com.markettwits.sportsouce.club.subscription.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.commerce.SauceQuantityStepper
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.extensions.formatRubles
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.*
import com.markettwits.sportsouce.club.dashboard.domain.Subscription
import com.markettwits.sportsouce.club.subscription.component.SubscriptionPricingComponent
import com.markettwits.sportsouce.club.subscription.store.SubscriptionPricingStore

@Composable
fun SubscriptionPricingScreen(component: SubscriptionPricingComponent) {
    val state by component.state.collectAsState()
    SauceScaffold(topBar = { TopBarWithClip(title = "Абонементы и цены", goBack = { component.obtainEvent(SubscriptionPricingStore.Intent.OnClickBack) }) }) { padding ->
        AdaptivePane(Modifier.fillMaxSize().padding(padding)) { SubscriptionPricingContent(state, component::obtainEvent) }
    }
}

@Composable
internal fun SubscriptionPricingContent(state: SubscriptionPricingStore.State, event: (SubscriptionPricingStore.Intent) -> Unit) {
    var selectedName by rememberSaveable { mutableStateOf<String?>(null) }
    val group = state.subscriptions.firstOrNull { it.groupName == selectedName } ?: state.subscriptions.firstOrNull()
    SauceLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item(key = "intro") { Text("Выберите направление и формат занятий. После заявки с вами свяжутся и расскажут про оплату.",
            style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary) }
        if (state.subscriptions.isNotEmpty()) item(key = "tabs") {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                state.subscriptions.forEach { type -> FilterChipBase(selected = type.groupName == group?.groupName, label = type.groupName,
                    onClick = { selectedName = type.groupName }, selectedColors = type.subscription.firstOrNull()?.color?.clubColors()?.selectionColors) }
            }
        }
        if (state.error != null) item(key = "error") { ClubNotice("Не удалось загрузить абонементы", "Попробуйте обновить данные.", { event(SubscriptionPricingStore.Intent.RetryRequest) }) }
        if (state.isLoading && state.subscriptions.isEmpty()) item(key = "loading") { ClubLoading("Загрузка абонементов") }
        else if (group == null && state.error == null) item(key = "empty") { ClubNotice("Абонементов пока нет", "Доступные форматы можно уточнить при записи на тренировку.") }
        items(group?.subscription.orEmpty(), key = { "subscription-${it.id}" }) { subscription ->
            SubscriptionCard(subscription, state.priceCalculations[subscription.id.toString()], state.isLoading, event)
        }
    }
}

@Composable
internal fun SubscriptionCard(subscription: Subscription, calculation: SubscriptionPricingStore.PriceCalculationState?, refreshing: Boolean,
    event: (SubscriptionPricingStore.Intent) -> Unit) {
    val count = calculation?.monthCount ?: 1
    val busy = refreshing || calculation?.isLoading == true
    val price = calculation?.workoutPrice?.totalPrice ?: subscription.price
    OnBackgroundCard {
        ClubAccentStripe(subscription.color)
        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(subscription.name, style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
            if (subscription.description.isNotBlank()) HtmlText(text = subscription.description, style = MaterialTheme.typography.bodyMedium,
                color = SauceTheme.colors.textSecondary, showImages = false)
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(4.dp), itemVerticalAlignment = Alignment.CenterVertically) {
                    Text("${price.formatRubles()} ₽", style = MaterialTheme.typography.headlineSmall, color = SauceTheme.colors.textPrimary)
                    calculation?.workoutPrice?.priceWithoutDiscounts?.takeIf { it > price }?.let { previous ->
                        Text("${previous.formatRubles()} ₽", style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.LineThrough, color = SauceTheme.colors.textMuted)
                    }
                }
                if (subscription.priceDependsOnCount) {
                    Text("Количество", style = MaterialTheme.typography.labelLarge, color = SauceTheme.colors.textMuted)
                    SauceQuantityStepper(count, onDecrease = { event(SubscriptionPricingStore.Intent.UpdateMonthCount(subscription.id, count - 1)) },
                        onIncrease = { event(SubscriptionPricingStore.Intent.UpdateMonthCount(subscription.id, count + 1)) },
                        canDecrease = count > 1 && !busy, canIncrease = count < subscription.maxAmount && !busy)
                }
            }
            if (calculation?.error != null) {
                Text("Не удалось пересчитать стоимость.", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.danger.content)
                SauceButton("Повторить расчёт", { event(SubscriptionPricingStore.Intent.UpdateMonthCount(subscription.id, count)) },
                    variant = SauceButtonVariant.Text, enabled = !busy)
            }
            SauceButton("Записаться", { event(SubscriptionPricingStore.Intent.OnClickSubscribe(subscription.id, subscription.name)) },
                modifier = Modifier.fillMaxWidth(), variant = SauceButtonVariant.Outlined, enabled = !busy && calculation?.error == null,
                loading = calculation?.isLoading == true)
        }
    }
}
