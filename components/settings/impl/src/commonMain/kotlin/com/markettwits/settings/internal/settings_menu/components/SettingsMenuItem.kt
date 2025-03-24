package com.markettwits.settings.internal.settings_menu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun SettingsMenuItem(modifier: Modifier = Modifier, item: SettingsMenuElement) {
    when (item) {
        is SettingsMenuElement.Base -> SettingsMenuElementBase(
            modifier = modifier, item = item
        )

        is SettingsMenuElement.WithDescription -> SettingsMenuElementWithDescription(
            modifier = modifier,
            item = item
        )
    }
}

@Composable
private fun SettingsMenuElementBase(
    modifier: Modifier = Modifier,
    item: SettingsMenuElement.Base
) {
    Row(
        modifier = modifier
            .padding(14.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.semiBoldBold(),
        )
    }
}

@Composable
private fun SettingsMenuElementWithDescription(
    modifier: Modifier = Modifier,
    item: SettingsMenuElement.WithDescription
) {
    Row(
        modifier = modifier
            .padding(14.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            Text(
                text = item.title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.semiBoldBold(),
            )
            Text(
                text = item.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
            )
        }
    }
}