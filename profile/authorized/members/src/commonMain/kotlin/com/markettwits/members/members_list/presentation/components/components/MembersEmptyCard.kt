package com.markettwits.members.members_list.presentation.components.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun MembersEmptyCard(modifier: Modifier = Modifier) {
    OnBackgroundCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(4.dp),
                text = "У вас ещё нет участников",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold(),
                lineHeight = 14.sp,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(4.dp),
                text = socialNetworkLabel,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.medium(),
                lineHeight = 14.sp,
                fontSize = 12.sp
            )
        }

    }
}

private const val socialNetworkLabel =
    "Добавьте участников что бы легче регистрироваться на командные старты  "