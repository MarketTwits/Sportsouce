package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.filter.domain.models.ShopFilterPrice

@Composable
fun ShopFilterPriceSlider(modifier: Modifier = Modifier, shopFilterPrice: ShopFilterPrice,
    onMinPriceChange: (String) -> Unit, onMaxPriceChange: (String) -> Unit) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Цена", style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth < 280.dp * LocalDensity.current.fontScale) Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PriceTextBox(value = shopFilterPrice.minPrice, isFrom = true, onValueChange = onMinPriceChange)
                PriceTextBox(value = shopFilterPrice.maxPrice, isFrom = false, onValueChange = onMaxPriceChange)
            } else Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PriceTextBox(Modifier.weight(1f), shopFilterPrice.minPrice, true, onMinPriceChange)
                PriceTextBox(Modifier.weight(1f), shopFilterPrice.maxPrice, false, onMaxPriceChange)
            }
        }
    }
}

@Composable
private fun PriceTextBox(modifier: Modifier = Modifier, value: ShopFilterPrice.Value, isFrom: Boolean, onValueChange: (String) -> Unit) {
    val text = if (value is ShopFilterPrice.Value.Price) value.cost.toString() else ""
    OutlinedTextFieldBase(modifier = modifier, label = if (isFrom) "От, ₽" else "До, ₽", value = text,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), onValueChange = { input ->
            val price = input.trim()
            if (price.isEmpty() || (price.all { it.isDigit() } && price.toLongOrNull()?.let { it < Int.MAX_VALUE } == true)) onValueChange(price)
        })
}

class NumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString) = TransformedText(text, OffsetMapping.Identity)
}
