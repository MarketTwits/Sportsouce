package com.markettwits.sportsouce.profile.registrations.detail.components.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Start
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
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
fun OrderDialogStartStatus(modifier: Modifier = Modifier, startStatus: StartItem.StartStatus) {
    OnBackgroundCard(modifier = modifier) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Start,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Column {
                Text(
                    text = "Статус старта",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.medium(),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = startStatus.name,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}