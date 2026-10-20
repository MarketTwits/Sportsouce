package com.markettwits.sportsouce.start.register.presentation.registration.pay.components.price

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.divider.BaseDivider
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import kotlin.math.max

private data class TotalPriceUiModel(val currentPrice: Int, val previousPrice: Int?, val discountPercent: Int?)

@Composable
internal fun StartRegistrationPriceContent(
    modifier: Modifier = Modifier,
    priceResponse: StartRegistrationPriceResult,
    isLoading: Boolean,
    onClickPromo: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.small),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.large),
    ) {
        PromoBox(onClick = onClickPromo)
        val loadingModifier = if (isLoading) Modifier.shimmer() else Modifier
        when (priceResponse) {
            StartRegistrationPriceResult.Empty -> OrderPriceInfoEmpty(loadingModifier)
            StartRegistrationPriceResult.Free -> OrderPriceInfo(loadingModifier, 0, 0, 0, 0)
            is StartRegistrationPriceResult.Value -> OrderPriceInfo(
                modifier = loadingModifier,
                defaultPrice = priceResponse.priceWithoutDiscount,
                discountCount = (priceResponse.priceWithoutDiscount + priceResponse.additionalFieldsPrice) - priceResponse.totalPrice,
                optionsPrice = priceResponse.additionalFieldsPrice,
                finalPrice = priceResponse.totalPrice,
            )
        }
    }
}

@Composable
internal fun OrderPriceInfoEmpty(modifier: Modifier = Modifier) {
    OnBackgroundCard(modifier = modifier) {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            OrderHeading()
            PriceLine("Изначальная стоимость:") { PricePlaceholder() }
            PriceLine("Сумма скидки:") { PricePlaceholder() }
            PriceLine("Дополнительные опции:") { PricePlaceholder() }
            BaseDivider()
            PriceLine("Итого:", total = true) { PricePlaceholder() }
        }
    }
}

@Composable
internal fun OrderPriceInfo(
    modifier: Modifier = Modifier,
    defaultPrice: Int,
    discountCount: Int,
    optionsPrice: Int,
    finalPrice: Int,
) {
    val totalPriceWithoutDiscount = defaultPrice + optionsPrice
    val hasDiscount = discountCount > 0 && totalPriceWithoutDiscount > finalPrice
    val discountPercent = if (hasDiscount && totalPriceWithoutDiscount > 0) max(1, discountCount * 100 / totalPriceWithoutDiscount) else 0
    val total = TotalPriceUiModel(finalPrice, if (hasDiscount) totalPriceWithoutDiscount else null,
        if (hasDiscount) discountPercent else null)
    OnBackgroundCard(modifier = modifier) {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            OrderHeading()
            PriceLine("Изначальная стоимость:") { AnimatedPrice(defaultPrice) }
            AnimatedVisibility(visible = hasDiscount) {
                PriceLine("Сумма скидки:") { AnimatedPrice(discountCount, "− ") }
            }
            PriceLine("Дополнительные опции:") { AnimatedPrice(optionsPrice) }
            BaseDivider()
            PriceLine("Итого:", total = true) {
                AnimatedContent(
                    targetState = total,
                    transitionSpec = {
                        if (targetState.currentPrice > initialState.currentPrice)
                            slideInVertically { -it } togetherWith slideOutVertically { it }
                        else slideInVertically { it } togetherWith slideOutVertically { -it }
                    },
                    label = "start_registration_total_price",
                ) { value ->
                    Column(Modifier.width(IntrinsicSize.Max), horizontalAlignment = Alignment.End) {
                        if (value.previousPrice != null && value.discountPercent != null) {
                            Text("${value.previousPrice.formatPrice()} ₽", Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted,
                                textAlign = TextAlign.End, textDecoration = TextDecoration.LineThrough)
                            Text("−${value.discountPercent}%", Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.labelMedium, color = SauceTheme.colors.action,
                                textAlign = TextAlign.End)
                        }
                        Text("${value.currentPrice.formatPrice()} ₽", Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.action,
                            textAlign = TextAlign.End)
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderHeading() {
    Text("Ваш заказ", Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium,
        color = SauceTheme.colors.textPrimary)
}

/** Separate rows at large font sizes keep amounts legible instead of ellipsizing either column. */
@Composable
private fun PriceLine(title: String, total: Boolean = false, value: @Composable () -> Unit) {
    val style = if (total) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
    val color = if (total) SauceTheme.colors.textPrimary else SauceTheme.colors.textSecondary
    val fontScale = LocalDensity.current.fontScale
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        if (maxWidth < 240.dp * fontScale) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                Text(title, Modifier.fillMaxWidth(), style = style, color = color)
                Box(Modifier.align(Alignment.End)) { value() }
            }
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
                verticalAlignment = Alignment.CenterVertically) {
                Text(title, Modifier.weight(1f), style = style, color = color)
                value()
            }
        }
    }
}

@Composable
private fun AnimatedPrice(value: Int, prefix: String = "") {
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            if (targetState > initialState) slideInVertically { -it } togetherWith slideOutVertically { it }
            else slideInVertically { it } togetherWith slideOutVertically { -it }
        },
        label = "start_registration_price_row",
    ) { amount ->
        Box(Modifier.width(IntrinsicSize.Max)) {
            Text("$prefix${amount.formatPrice()} ₽", Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.End, color = SauceTheme.colors.textPrimary)
        }
    }
}

@Composable
private fun PricePlaceholder() {
    Box(Modifier.size(64.dp, 16.dp).background(SauceTheme.colors.surfaceSubtle, SauceShapes.badge))
}
