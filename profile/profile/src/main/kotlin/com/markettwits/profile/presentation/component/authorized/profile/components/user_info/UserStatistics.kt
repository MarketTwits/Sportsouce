package com.markettwits.profile.presentation.component.authorized.profile.components.user_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun UserStatistics(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserStatisticCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                title = "Участники",
                value = "1"
            )
            UserStatisticCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                title = "Всего регистраций",
                value = "4"
            )
        }
    }
}

@Composable
private fun UserStatisticCard(modifier: Modifier = Modifier, title: String, value: String) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer)
    ) {
        Text(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Start),
            text = title,
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.Start),
            text = value,
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
