package com.markettwits.sportsouce.start.register.presentation.registration.pay.components.price

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult

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
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Итого",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
            DiscountRow(title = "Сумма скидки :", value = discountCount)
            DiscountRow(title = "Дополнительные опции :", value = optionsPrice)
            Text(
                modifier = Modifier.padding(4.dp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                        append("Итого: ")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimary)) {
                        append("${finalPrice.formatPrice()} ₽")
                    }
                },
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DiscountRowEmpty(modifier: Modifier = Modifier, title: String) {
    AnimatedVisibility(visible = true) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
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
private fun DiscountRow(modifier: Modifier = Modifier, title: String, value: Int) {
    AnimatedVisibility(visible = true) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = " ${value.formatPrice()} ₽",
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
