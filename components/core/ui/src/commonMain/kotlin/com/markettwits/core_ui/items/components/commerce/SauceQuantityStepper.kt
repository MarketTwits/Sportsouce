package com.markettwits.core_ui.items.components.commerce

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.*

@Composable
fun SauceQuantityStepper(count: Int, onDecrease: () -> Unit, onIncrease: () -> Unit,
    modifier: Modifier = Modifier, canIncrease: Boolean = true, canDecrease: Boolean = count > 0) {
    Surface(modifier, shape = SauceShapes.field, color = SauceTheme.colors.surfaceSubtle) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            IconButton(onDecrease, enabled = canDecrease, modifier = Modifier.size(SauceSizes.touchTarget)) {
                Icon(Icons.Default.Remove, "Уменьшить количество", tint = if (canDecrease) SauceTheme.colors.action else SauceTheme.colors.disabledContent)
            }
            Text(count.toString(), Modifier.widthIn(min = 24.dp).padding(horizontal = 4.dp)
                .semantics { contentDescription = "Количество: $count"; liveRegion = LiveRegionMode.Polite },
                style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            IconButton(onIncrease, enabled = canIncrease, modifier = Modifier.size(SauceSizes.touchTarget)) {
                Icon(Icons.Default.Add, "Увеличить количество", tint = if (canIncrease) SauceTheme.colors.action else SauceTheme.colors.disabledContent)
            }
        }
    }
}
