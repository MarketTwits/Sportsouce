package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun OrderPromocodeCard(
    modifier: Modifier,
    promocode: String
) {
    Column(modifier = modifier) {
        Text(
            text = "Промокод",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        OnBackgroundCard {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.padding(5.dp))
                Icon(
                    imageVector = Icons.Filled.Accessibility,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = promocode.ifEmpty { "Нет промокода" },
                    fontSize = 16.sp,
                    fontFamily = FontNunito.medium(),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

