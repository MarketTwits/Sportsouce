package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.core_ui.items.theme.SauceTheme
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
            withStyle(style = SpanStyle(color = SauceTheme.colors.textSecondary)) {
                append(field.title)
            }
            withStyle(style = SpanStyle(color = SauceTheme.colors.textSecondary)) {
                when (val price = field.price) {
                    is StartRegistrationFieldPrice.Empty -> {}
                    is StartRegistrationFieldPrice.Cost -> {
                        append(" ( + ${price.price.formatPrice()} ₽ )")
                    }
                }
            }
            withStyle(
                style = SpanStyle(color = SauceTheme.colors.danger.content)
            ) {
                if (!field.isOptional)
                    append(" *")
            }
        },
        style = MaterialTheme.typography.labelLarge,
        overflow = TextOverflow.Ellipsis
    )
}

