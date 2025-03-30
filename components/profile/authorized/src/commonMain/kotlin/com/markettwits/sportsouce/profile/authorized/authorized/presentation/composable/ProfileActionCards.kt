package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Light
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito


@Composable
internal fun ProfileActionCards(
    modifier: Modifier = Modifier,
    startsCount: Int,
    onClickStarts: () -> Unit,
    onClickOrders: () -> Unit
) {

    val startsDescription = if (startsCount != 0) "$startsCount стартов" else "Нет регистраций"

    Row(modifier = modifier) {
        ProfileActionCard(
            icon = Icons.Default.Light,
            title = "Мои Старты",
            description = startsDescription,
            onClick = onClickStarts
        )
        Spacer(Modifier.width(8.dp))
        ProfileActionCard(
            icon = Icons.Default.ShoppingCart,
            title = "Мои заказы",
            description = "Посмотреть",
            onClick = onClickOrders
        )
    }
}

@Composable
private fun ProfileActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(Modifier.height(4.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    overflow = TextOverflow.Clip,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.regular(),
                    overflow = TextOverflow.Clip,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(Modifier.width(12.dp))
        }
    }
}