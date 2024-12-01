package com.markettwits.start.register.presentation.order.presentation.components.promo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.WifiProtectedSetup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun PromoBox(modifier: Modifier = Modifier, onClick: () -> Unit) {
    OnBackgroundCard(
        onClick = {
            onClick()
        },
        modifier = modifier.padding(vertical = 10.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = modifier.padding(10.dp)) {
                Icon(
                    imageVector = Icons.Default.WifiProtectedSetup,
                    contentDescription = "Промокод",
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.padding(end = 5.dp))
                Text(
                    text = "Промокод",
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}