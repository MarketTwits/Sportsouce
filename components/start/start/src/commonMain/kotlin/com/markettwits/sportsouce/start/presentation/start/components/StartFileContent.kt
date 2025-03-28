package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun StartFileContent(
    modifier: Modifier,
    fileName: String,
    onClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                modifier = Modifier
                    .weight(0.1f)
                    .padding(6.dp),
                imageVector = Icons.Default.FilePresent,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
            )
            Text(
                modifier = Modifier
                    .weight(0.9f)
                    .padding(6.dp),
                text = fileName,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.medium(),
                fontSize = 14.sp
            )
        }
    }
}