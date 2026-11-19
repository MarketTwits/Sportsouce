package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationAdditionalField
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationFieldPrice


@Composable
internal fun AdditionalFiledTitle(
    modifier: Modifier = Modifier,
    field: StartRegistrationAdditionalField
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
        Text(
            text = buildAnnotatedString {
                append(field.labelWithPrice())
                if (!field.isOptional) {
                    withStyle(SpanStyle(color = SauceTheme.colors.danger.content)) { append(" *") }
                }
            },
            style = MaterialTheme.typography.labelLarge,
            color = SauceTheme.colors.textSecondary,
            overflow = TextOverflow.Ellipsis,
        )
        field.description?.takeIf { it.isNotBlank() }?.let { description ->
            Text(description, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
        }
    }
}

internal fun StartRegistrationAdditionalField.labelWithPrice(): String = buildString {
    append(title)
    if (price is StartRegistrationFieldPrice.Cost) append(" (+${price.price.formatPrice()} ₽)")
}

