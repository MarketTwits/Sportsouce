package com.markettwits.core_ui.items.components.commerce

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

/** Displays server-formatted prices without changing their value or calculating discounts. */
@Composable
fun SaucePrice(current: String, previous: String? = null, discount: Int? = null,
    modifier: Modifier = Modifier, prominent: Boolean = false) {
    FlowRow(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp), itemVerticalAlignment = Alignment.CenterVertically) {
        Text("$current ₽", style = if (prominent) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
            color = SauceTheme.colors.textPrimary)
        if (!previous.isNullOrBlank()) {
            Text("$previous ₽", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted,
                textDecoration = TextDecoration.LineThrough)
            if (discount != null && discount > 0) Text("−$discount%", style = MaterialTheme.typography.labelMedium,
                color = SauceTheme.colors.action)
        }
    }
}
