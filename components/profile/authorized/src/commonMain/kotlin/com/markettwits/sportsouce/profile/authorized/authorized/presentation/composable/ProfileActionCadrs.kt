package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes

@Composable
internal fun ProfileActionCadrs(
    modifier: Modifier = Modifier,
    onClickMembers: () -> Unit,
    onClickHelp: () -> Unit,
    onClickSettings: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = Shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            ProfileActionCard(
                onClick = onClickMembers,
                text = "Участники"
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 10.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
            )
            ProfileActionCard(
                onClick = onClickSettings,
                actionIcon = Icons.Default.Settings,
                text = "Настройки"
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 10.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
            )
            ProfileActionCard(
                onClick = onClickHelp,
                actionIcon = Icons.AutoMirrored.Filled.Message,
                text = "Помощь"
            )
        }
    }
}

@Composable
private fun ProfileActionCard(
    modifier: Modifier = Modifier,
    text: String,
    actionIcon: ImageVector = Icons.Default.ChevronRight,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = FontNunito.bold(),
                fontSize = 18.sp
            )
            Box(
                modifier = Modifier
                    .clip(Shapes.medium)
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}