package com.markettwits.shop.filter.presentation.components.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_extensions.formatPrice
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.textField.defaultOutlineTextFiledColors
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.filter.domain.models.ShopFilterPrice

@Composable
fun ShopFilterPriceSlider(
    modifier: Modifier = Modifier,
    shopFilterPrice: ShopFilterPrice,
    onMinPriceChange: (String) -> Unit,
    onMaxPriceChange: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = "Цена",
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            lineHeight = 18.sp,
            softWrap = true,
            textAlign = TextAlign.Start,
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.tertiary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PriceTextBox(
                modifier = Modifier.weight(1f),
                value = shopFilterPrice.minPrice,
                isFrom = true,
                onValueChange = onMinPriceChange)
            PriceTextBox(
                modifier = Modifier.weight(1f),
                value = shopFilterPrice.maxPrice,
                isFrom = false,
                onValueChange = onMaxPriceChange)
        }
    }
}

@Composable
private fun PriceTextBox(
    modifier: Modifier = Modifier,
    value: ShopFilterPrice.Value,
    isFrom: Boolean,
    onValueChange: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .clip(Shapes.large)
            .fillMaxWidth()
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        val text =
            when (value) {
                is ShopFilterPrice.Value.Price -> value.cost.toString()
                is ShopFilterPrice.Value.Empty -> ""
            }
        TextField(
            modifier = Modifier.align(Alignment.Center),
            value = text,
            visualTransformation = NumberVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle().copy(
                color = MaterialTheme.colorScheme.tertiary,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start,
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
            ),
            maxLines = 1,
            minLines = 1,
            singleLine = true,
            leadingIcon = {
                Text(
                    text = if (isFrom) "От" else "До",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    lineHeight = 18.sp,
                    softWrap = true,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            trailingIcon = {
                Text(
                    text = "₽",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    lineHeight = 18.sp,
                    softWrap = true,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            colors = defaultOutlineTextFiledColors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            onValueChange = { price ->
                if (price.trim().all { it.isDigit()})
                    if (price.isEmpty())
                        onValueChange(price.trim())
                    else
                        if (price.toLong() < Int.MAX_VALUE)
                            onValueChange(price.trim())
            }
        )
    }
}

class NumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text,
            OffsetMapping.Identity
        )
    }
}