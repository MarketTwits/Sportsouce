package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer

@Composable
internal fun StartExtraFieldsPanel(
    modifier: Modifier = Modifier,
    place: String,
    organizers: List<Organizer>,
    startDate: String
) {
    Column(modifier = modifier) {
        if (startDate.isNotEmpty()) {
            StartExtraFiledRow(
                icon = Icons.Outlined.DateRange,
                value = startDate,
            )
        }
        if (place.isNotEmpty()) {
            StartExtraFiledRow(
                icon = Icons.Outlined.Place,
                value = place,
            )
        }
        if (organizers.isNotEmpty()) {
            StartExtraFiledRow(
                icon = Icons.Outlined.PersonOutline,
                value = "Организаторы ${organizers.joinToString(", ") { it.name }}"

            )
        }
    }
}

@Composable
private fun StartExtraFiledRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
) {
    Row(
        modifier = modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "icon",
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontNunito.regular(),
            fontSize = 14.sp
        )
    }
}