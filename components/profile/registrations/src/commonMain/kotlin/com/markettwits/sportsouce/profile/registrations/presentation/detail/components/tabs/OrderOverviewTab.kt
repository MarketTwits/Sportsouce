package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOptions
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.RegistrationsCardImageCard
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore.StartPriceResult
import com.markettwits.sportsouce.profile.registrations.presentation.management.InlineRegistrationError
import com.markettwits.sportsouce.profile.registrations.presentation.management.canManageSlot
import com.markettwits.sportsouce.profile.registrations.presentation.management.hasOptionsToPay
import com.markettwits.sportsouce.profile.registrations.presentation.management.inlinePaymentAvailable
import com.markettwits.sportsouce.profile.registrations.presentation.management.isRegistrationPast

@Composable
internal fun OrderOverviewTab(
    order: StartOrderInfo,
    price: StartPriceResult,
    options: RegistrationOptions?,
    onOpenStart: (() -> Unit)?,
    onPay: () -> Unit,
    onUpdatePrice: () -> Unit,
    onHelp: () -> Unit,
    onChangeDistance: () -> Unit,
    onManageOptions: (() -> Unit)?,
    onRefund: (() -> Unit)?,
    optionsError: String? = null,
) {
    val past = order.isRegistrationPast()
    val showOptionsPayment = onManageOptions != null && order.hasOptionsToPay(options)
    OrderDetailPage {
        RegistrationStartSummary(order, onOpenStart)
        RegistrationStatusBanner(
            order,
            archived = past,
            actions = if (order.inlinePaymentAvailable()) ({
                RegistrationPaymentActions(price, onManageOptions != null, onPay, onUpdatePrice)
            }) else null,
        )
        if (showOptionsPayment && onManageOptions != null) RegistrationOptionsDueBanner(order, options, onManageOptions)
        RegistrationOverviewActions(
            options = options,
            optionsError = optionsError.takeUnless { showOptionsPayment },
            onChangeDistance = onChangeDistance.takeIf { order.canManageSlot() },
            onManageOptions = onManageOptions.takeUnless { past || order.isRefunded || showOptionsPayment },
            onRefund = onRefund,
            onHelp = onHelp,
        )
    }
}

@Composable
private fun RegistrationStartSummary(order: StartOrderInfo, onOpenStart: (() -> Unit)?) {
    val colors = SauceTheme.colors
    val distances = order.members.map { it.distanceName.trim() }.filter(String::isNotEmpty).groupingBy { it }.eachCount()
    OrderSectionCard {
        Column(Modifier.fillMaxWidth()) {
            BoxWithConstraints(
                Modifier.fillMaxWidth().padding(SauceSpacing.large),
            ) {
                val showPoster = order.image.isNotBlank() && maxWidth >= 250.dp * LocalDensity.current.fontScale
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SauceSpacing.large),
                ) {
                    if (showPoster) RegistrationsCardImageCard(
                        image = order.image,
                        modifier = Modifier.size(width = 64.dp, height = 80.dp),
                    )
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                        Text(
                            text = order.name.ifBlank { order.startTitle }.ifBlank { "Старт" },
                            modifier = Modifier.semantics { heading() },
                            style = MaterialTheme.typography.titleLarge,
                            color = colors.textPrimary,
                        )
                        if (order.dateStartPreview.isNotBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                            ) {
                                Icon(Icons.Outlined.CalendarToday, null, Modifier.size(16.dp), tint = colors.textMuted)
                                Text(order.dateStartPreview, style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                            }
                        }
                    }
                }
            }

            if (distances.isNotEmpty()) {
                HorizontalDivider(Modifier.padding(horizontal = SauceSpacing.large), color = colors.border)
                BoxWithConstraints(Modifier.fillMaxWidth().padding(SauceSpacing.large)) {
                    val inlineCount = maxWidth >= 250.dp * LocalDensity.current.fontScale
                    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                        Text(
                            if (distances.size == 1) "Дистанция" else "Дистанции",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.textMuted,
                        )
                        distances.forEach { (distance, count) ->
                            val countLabel = participantCount(count).takeIf { order.members.size > 1 }
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                            ) {
                                Icon(Icons.Outlined.ConfirmationNumber, null, Modifier.size(18.dp), tint = colors.action)
                                Column(Modifier.weight(1f)) {
                                    Text(distance, style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
                                    if (!inlineCount && countLabel != null) {
                                        Text(countLabel, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                                    }
                                }
                                if (inlineCount && countLabel != null) {
                                    Text(countLabel, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                                }
                            }
                        }
                    }
                }
            }
            if (onOpenStart != null) {
                HorizontalDivider(Modifier.padding(horizontal = SauceSpacing.large), color = colors.border)
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val showIcon = maxWidth >= 300.dp * LocalDensity.current.fontScale
                    SauceActionRow("Открыть страницу старта", onOpenStart, icon = Icons.Outlined.Flag.takeIf { showIcon },
                        contentPadding = PaddingValues(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium))
                }
            }
        }
    }
}

private fun participantCount(count: Int): String {
    val noun = when {
        count % 100 in 11..14 -> "участников"
        count % 10 == 1 -> "участник"
        count % 10 in 2..4 -> "участника"
        else -> "участников"
    }
    return "$count $noun"
}

@Composable
private fun RegistrationOverviewActions(
    options: RegistrationOptions?,
    optionsError: String?,
    onChangeDistance: (() -> Unit)?,
    onManageOptions: (() -> Unit)?,
    onRefund: (() -> Unit)?,
    onHelp: () -> Unit,
) {
    val colors = SauceTheme.colors
    val optionSubtitle = when {
        options?.paidOptions?.any { it.pendingChangeRequest?.status == "awaiting_payment" } == true -> "Ожидается доплата"
        options?.paidOptions?.any { it.pendingChangeRequest?.status == "pending" } == true -> "Изменение на проверке"
        options?.options?.any { it.status == "available" } == true -> "Можно добавить опции"
        else -> null
    }
    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        OrderSectionHeading("Действия с регистрацией")
        if (optionsError != null && onManageOptions != null) {
            InlineRegistrationError(
                optionsError, title = "Не удалось загрузить опции",
            )
        }
        OrderSectionCard {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val showIcons = maxWidth >= 300.dp * LocalDensity.current.fontScale
                val padding = PaddingValues(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium)
                val titleStyle = if (showIcons) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium
                Column(Modifier.fillMaxWidth()) {
                    if (onChangeDistance != null) {
                        SauceActionRow("Изменить дистанцию", onChangeDistance, icon = Icons.Outlined.SwapHoriz.takeIf { showIcons },
                            contentPadding = padding, titleStyle = titleStyle)
                        HorizontalDivider(Modifier.padding(horizontal = SauceSpacing.large), color = colors.border)
                    }
                    if (onManageOptions != null) {
                        SauceActionRow(if (showIcons) "Дополнительные опции" else "Опции", onManageOptions, subtitle = optionSubtitle,
                            icon = Icons.Outlined.ShoppingBag.takeIf { showIcons }, contentPadding = padding, titleStyle = titleStyle)
                        HorizontalDivider(Modifier.padding(horizontal = SauceSpacing.large), color = colors.border)
                    }
                    if (onRefund != null) {
                        SauceActionRow("Вернуть слот", onRefund, icon = Icons.Outlined.Timer.takeIf { showIcons },
                            contentPadding = padding, titleStyle = titleStyle)
                        HorizontalDivider(Modifier.padding(horizontal = SauceSpacing.large), color = colors.border)
                    }
                    SauceActionRow("Помощь с регистрацией", onHelp, icon = Icons.Outlined.HelpOutline.takeIf { showIcons },
                        contentPadding = padding, titleStyle = titleStyle)
                }
            }
        }
    }
}
