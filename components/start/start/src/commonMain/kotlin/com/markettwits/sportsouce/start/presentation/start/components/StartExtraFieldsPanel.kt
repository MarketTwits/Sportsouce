package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer

@Composable
internal fun StartExtraFieldsPanel(modifier: Modifier = Modifier, place: String, organizers: List<Organizer>,
    startDate: String, isPartialData: Boolean = false) {
    if (isPartialData) return
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        if (startDate.isNotBlank()) StartExtraFieldRow(Icons.Outlined.DateRange, startDate, prominent = true)
        if (place.isNotBlank()) StartExtraFieldRow(Icons.Outlined.Place, place)
        if (organizers.isNotEmpty()) StartExtraFieldRow(Icons.Outlined.PersonOutline,
            "Организаторы: ${organizers.joinToString { it.name }}")
    }
}

@Composable
private fun StartExtraFieldRow(icon: ImageVector, value: String, prominent: Boolean = false) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
        Icon(icon, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textMuted)
        Text(value, Modifier.weight(1f), style = if (prominent) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyMedium,
            color = if (prominent) SauceTheme.colors.textPrimary else SauceTheme.colors.textSecondary)
    }
}
