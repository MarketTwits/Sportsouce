package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
internal fun ProfileClubCards(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.large),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                modifier = Modifier.padding(6.dp),
                text = "Клуб",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                SubscriptionCard(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Default.Subscriptions,
                    title = "Подписки",
                    color = SportSouceColor.SportSouceLighBlue,
                    description = "Pass",
                    price = "от 0₽ старт",
                    onClick = onClick
                )
                Spacer(Modifier.width(12.dp))
                SubscriptionCard(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Default.MiscellaneousServices,
                    title = "Сервис",
                    color = SportSouceColor.SportSouceRegistryOpenGreen,
                    description = "Pass",
                    price = "от 0₽ подготовка",
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun SubscriptionCard(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    color: Color,
    title: String,
    description: String,
    price: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = imageVector,
                tint = color,
                contentDescription = ""
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                overflow = TextOverflow.Clip,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = price,
                fontSize = 14.sp,
                fontFamily = FontNunito.regular(),
                overflow = TextOverflow.Clip,
                color = MaterialTheme.colorScheme.outline
            )
        }
        SubscriptionCardBadge(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.End),
            text = description,
            color = color
        )
    }
}

@Composable
private fun SubscriptionCardBadge(
    modifier: Modifier = Modifier,
    color: Color,
    text: String,
) {
    Box(
        modifier = modifier
            .clip(Shapes.small)
            .background(color),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .padding(4.dp),
            text = text,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp,
            color = Color.White
        )
    }
}