package com.markettwits.core.errors.impl.supcomponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun SauceThrowableRetryButtonLarge(
    modifier: Modifier = Modifier,
    onClickRetry : () -> Unit
) {
    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        onClick = onClickRetry
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            text = "ПОВТОРИТЬ",
            color = MaterialTheme.colorScheme.onTertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp
        )
    }
}