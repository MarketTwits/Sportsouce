package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer

@Composable
internal fun StartExtraFieldsPanel(modifier: Modifier = Modifier, place: String, organizers: List<Organizer>,
    startDate: String, isPartialData: Boolean = false) {
    if (isPartialData || (startDate.isBlank() && place.isBlank() && organizers.isEmpty())) return
    Column(
        modifier = modifier.fillMaxWidth().clip(SauceShapes.card),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall),
    ) {
        if (startDate.isNotBlank()) StartExtraFieldRow(Icons.Outlined.DateRange, "Дата старта", startDate)
        if (place.isNotBlank()) StartExtraFieldRow(Icons.Outlined.Place, "Место проведения", place)
        if (organizers.isNotEmpty()) StartExtraFieldRow(
            Icons.Outlined.PersonOutline,
            if (organizers.size == 1) "Организатор" else "Организаторы",
            organizers.joinToString { it.name },
        )
    }
}

@Composable
private fun StartExtraFieldRow(icon: ImageVector, label: String, value: String) {
    val colors = SauceTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().background(colors.surfaceSubtle)
            .padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
    ) {
        Box(
            modifier = Modifier.size(36.dp).background(
                color = colors.actionSoft,
                shape = SauceShapes.badge,
            ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, Modifier.size(20.dp), tint = colors.action)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, Modifier.fillMaxWidth(), style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
            Text(value, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary)
        }
    }
}
