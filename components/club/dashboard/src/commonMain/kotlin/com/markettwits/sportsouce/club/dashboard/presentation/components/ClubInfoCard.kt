package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun ClubInfoCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    colors: List<Color>,
    onClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontNunito.extraBold()
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 8.dp),
                    lineHeight = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            AnimatedIconBackground(
                colors = colors,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}