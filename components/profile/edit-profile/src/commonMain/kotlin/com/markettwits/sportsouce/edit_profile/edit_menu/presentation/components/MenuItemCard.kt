package com.markettwits.sportsouce.edit_profile.edit_menu.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
fun EditProfileMenuItemCard(
    item: MenuItem,
    onCLick: (String) -> Unit
) {
    when (item) {
        is MenuItem.Base -> {
            Row(
                modifier = Modifier
                    .clickable { onCLick(item.title) }
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = item.title,
                    color = if (item.isDangerZone)
                        SportSouceColor.SportSouceLightRed
                    else
                        MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.medium(),
                )
            }
        }

        is MenuItem.Header -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.bold(),
                )
            }
        }
    }
}