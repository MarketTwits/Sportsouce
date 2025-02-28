package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Groups
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
import com.markettwits.start_cloud.model.start.fields.Organizer

@Composable
internal fun StartExtraFieldsPanel(
    modifier: Modifier = Modifier,
    place: String,
    organizers: List<Organizer>,
    startDate: String
) {
    Column(modifier = modifier) {
        if (place.isNotEmpty()) {
            StartExtraFiledRow(
                icon = Icons.Outlined.Place,
                value = place,
            )
        }
        if (organizers.isNotEmpty()) {
            StartExtraFiledRow(
                icon = Icons.Outlined.Groups,
                value = "Организаторы ${organizers.joinToString(", ") { it.name }}"

            )
        }
        if (startDate.isNotEmpty()) {
            StartExtraFiledRow(
                icon = Icons.Outlined.CalendarToday,
                value = startDate,
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
        modifier = modifier.padding(vertical = 4.dp),
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