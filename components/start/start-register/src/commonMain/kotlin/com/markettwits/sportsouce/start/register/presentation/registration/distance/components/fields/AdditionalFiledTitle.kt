package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationAdditionalField
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationFieldPrice


@Composable
internal fun AdditionalFiledTitle(
    modifier: Modifier = Modifier,
    field: StartRegistrationAdditionalField
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.outline)) {
                append(field.title)
            }
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.outline)) {
                when (val price = field.price) {
                    is StartRegistrationFieldPrice.Empty -> {}
                    is StartRegistrationFieldPrice.Cost -> {
                        append(" ( + ${price.price.formatPrice()} ₽ )")
                    }
                }
            }
            withStyle(
                style = SpanStyle(color = MaterialTheme.colorScheme.error)
            ) {
                if (!field.isOptional)
                    append(" *")
            }
        },
        fontSize = 14.sp,
        fontFamily = FontNunito.semiBoldBold(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

