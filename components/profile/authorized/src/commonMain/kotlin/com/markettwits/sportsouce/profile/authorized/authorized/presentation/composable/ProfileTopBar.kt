package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ProfileTopBar(modifier: Modifier = Modifier, goSettings: () -> Unit) {
    Box(
        modifier
            .shadow(4.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 5.dp, end = 8.dp)
            .padding(vertical = 5.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            onClick = { goSettings() }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 30.dp),
            text = "Мой профиль",
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 18.sp
        )
    }
}