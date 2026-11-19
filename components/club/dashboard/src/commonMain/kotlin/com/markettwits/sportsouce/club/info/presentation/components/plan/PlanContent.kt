package com.markettwits.sportsouce.club.info.presentation.components.plan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import androidx.compose.foundation.lazy.items
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.*
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionColor
import com.markettwits.sportsouce.club.info.domain.models.ClubFeature

@Composable
internal fun ClubPlanContent(onClickSubscribe: () -> Unit, steps: List<ClubFeature> = emptyList()) {
    val fallback = listOf("Записаться и прийти на бесплатную пробную тренировку.",
        "После тренировки поможем выбрать абонемент и расписание.", "Выберем цель и дальнейший план.",
        "Тренируемся, общаемся в команде и достигаем результата.")
    val descriptions = if (steps.isEmpty()) fallback else steps.map { it.description }
    SauceLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(descriptions.size) { index ->
            OnBackgroundCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ClubAccentLabel("Шаг ${index + 1}", SubscriptionColor.ORANGE)
                    HtmlText(text = descriptions[index], style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textPrimary, showImages = false)
                }
            }
        }
        item { SauceButton("Записаться на тренировку", onClickSubscribe, modifier = Modifier.fillMaxWidth()) }
    }
}
