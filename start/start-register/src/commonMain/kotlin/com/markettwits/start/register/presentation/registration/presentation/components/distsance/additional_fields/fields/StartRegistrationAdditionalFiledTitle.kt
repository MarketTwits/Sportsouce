package com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields.fields

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.presentation.registration.domain.extension.formatPrice
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationAdditionalField
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationPrice


@Composable
internal fun StartRegistrationAdditionalFiledTitle(
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
                    is StartRegistrationPrice.Empty -> {}
                    is StartRegistrationPrice.Cost -> {
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

