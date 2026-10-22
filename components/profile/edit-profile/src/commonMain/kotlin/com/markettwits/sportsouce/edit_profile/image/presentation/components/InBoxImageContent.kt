package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun BoxScope.InBoxImageContent() {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = Icons.Default.Image,
            contentDescription = null,
            tint = SauceTheme.colors.textMuted
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Выбрать фото",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = SauceTheme.colors.textSecondary
        )
    }
}
