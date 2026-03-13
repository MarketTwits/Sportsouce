package com.markettwits.sportsouce.start.register.presentation.registration.pay.components.price

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import kotlin.math.max

private data class TotalPriceUiModel(
    val currentPrice: Int,
    val previousPrice: Int?,
    val discountPercent: Int?,
)

@Composable
internal fun StartRegistrationPriceContent(
    modifier: Modifier = Modifier,
    priceResponse: StartRegistrationPriceResult,
    isLoading: Boolean,
    onClickPromo: () -> Unit,
) {
    Column(
        modifier = modifier.padding(10.dp),
    ) {
        PromoBox(onClick = onClickPromo)

        Spacer(modifier = Modifier.height(10.dp))

        when (priceResponse) {
            StartRegistrationPriceResult.Empty -> {
                OrderPriceInfoEmpty(
                    modifier = if (isLoading) Modifier.shimmer() else Modifier
                )
            }

            StartRegistrationPriceResult.Free -> {
                OrderPriceInfo(
                    modifier = if (isLoading) Modifier.shimmer() else Modifier,
                    defaultPrice = 0,
                    discountCount = 0,
                    optionsPrice = 0,
                    finalPrice = 0
                )
            }

            is StartRegistrationPriceResult.Value -> {
                OrderPriceInfo(
                    modifier = if (isLoading) Modifier.shimmer() else Modifier,
                    defaultPrice = priceResponse.priceWithoutDiscount,
                    discountCount = (priceResponse.priceWithoutDiscount + priceResponse.additionalFieldsPrice) - priceResponse.totalPrice,
                    optionsPrice = priceResponse.additionalFieldsPrice,
                    finalPrice = priceResponse.totalPrice
                )
            }
        }
    }
}

@Composable
internal fun OrderPriceInfoEmpty(modifier: Modifier = Modifier) {
    OnBackgroundCard(modifier = modifier) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Ваш заказ",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            DiscountRowEmpty(title = "Изначальная стоимость :")
            DiscountRowEmpty(title = "Сумма скидки :")
            DiscountRowEmpty(title = "Дополнительные опции :")
            TotalPriceRowEmpty()
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
    val discountPercent = if (hasDiscount && totalPriceWithoutDiscount > 0) {
        max(1, discountCount * 100 / totalPriceWithoutDiscount)
    } else {
        0
    }

    OnBackgroundCard(modifier = modifier) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Ваш заказ",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            DiscountRow(title = "Изначальная стоимость :", value = defaultPrice)
            DiscountRow(
                title = "Сумма скидки :",
                value = discountCount,
                isVisible = hasDiscount,
                valuePrefix = "- "
            )
            DiscountRow(title = "Дополнительные опции :", value = optionsPrice)
            Spacer(modifier = Modifier.height(8.dp))
            TotalPriceRow(
                title = "Итого:",
                currentPrice = finalPrice,
                previousPrice = if (hasDiscount) totalPriceWithoutDiscount else null,
                discountPercent = if (hasDiscount) discountPercent else null
            )
        }
    }
}

@Composable
private fun TotalPriceRowEmpty(modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = true) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = "Итого:",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 4.dp, bottom = 4.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    .clip(Shapes.medium)
            )
        }
    }
}

@Composable
private fun TotalPriceRow(
    modifier: Modifier = Modifier,
    title: String,
    currentPrice: Int,
    previousPrice: Int?,
    discountPercent: Int?,
) {
    val totalPriceUiModel = TotalPriceUiModel(
        currentPrice = currentPrice,
        previousPrice = previousPrice,
        discountPercent = discountPercent,
    )

    AnimatedVisibility(visible = true) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = title,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                modifier = Modifier.wrapContentWidth(),
                horizontalAlignment = Alignment.End
            ) {
                AnimatedContent(
                    targetState = totalPriceUiModel,
                    transitionSpec = {
                        if (targetState.currentPrice > initialState.currentPrice) {
                            slideInVertically { -it } togetherWith slideOutVertically { it }
                        } else {
                            slideInVertically { it } togetherWith slideOutVertically { -it }
                        }
                    },
                    label = "start_registration_total_price"
                ) { value ->
                    Column(horizontalAlignment = Alignment.End) {
                        if (value.previousPrice != null && value.discountPercent != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${value.previousPrice.formatPrice()} ₽",
                                    color = MaterialTheme.colorScheme.outline,
                                    textAlign = TextAlign.End,
                                    maxLines = 1,
                                    textDecoration = TextDecoration.LineThrough,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    fontFamily = FontNunito.regular(),
                                )
                                Text(
                                    text = "-${value.discountPercent}%",
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.End,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    lineHeight = 12.sp,
                                    fontFamily = FontNunito.medium(),
                                )
                            }
                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                    append("${value.currentPrice.formatPrice()} ₽")
                                }
                            },
                            fontSize = 20.sp,
                            fontFamily = FontNunito.bold(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscountRowEmpty(modifier: Modifier = Modifier, title: String) {
    AnimatedVisibility(visible = true) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    .clip(Shapes.medium)
            )
        }
    }
}

@Composable
private fun DiscountRow(
    modifier: Modifier = Modifier,
    title: String,
    value: Int,
    isVisible: Boolean = true,
    valuePrefix: String = "",
) {
    AnimatedVisibility(visible = isVisible) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
            AnimatedContent(
                targetState = value,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    } else {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    }
                },
                label = "start_registration_price_row"
            ) { animatedValue ->
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = " $valuePrefix${animatedValue.formatPrice()} ₽",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
