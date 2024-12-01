package com.markettwits.start.register.presentation.registration.presentation.components.pay.price

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.start.register.presentation.order.presentation.components.promo.PromoBox
import com.markettwits.start.register.presentation.registration.domain.extension.formatPrice
import com.markettwits.start_cloud.model.register.price.StartRegisterPriceResponse

@Composable
internal fun StartRegistrationPriceContent(
    modifier: Modifier = Modifier,
    priceResponse: StartRegisterPriceResponse,
    isLoading: Boolean,
    onClickPromo: () -> Unit,
) {
    Column(
        modifier = modifier.padding(10.dp),
    ) {
        PromoBox(onClick = onClickPromo)
        Spacer(modifier = Modifier.height(10.dp))
        OrderPriceInfo(
            modifier = if (isLoading) Modifier.shimmer() else Modifier,
            defaultPrice = priceResponse.priceWithoutDiscount,
            discountCount = priceResponse.priceWithoutDiscount - priceResponse.totalPrice,
            optionsPrice = priceResponse.additionalFieldsPrice,
            finalPrice = priceResponse.totalPrice
        )
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
