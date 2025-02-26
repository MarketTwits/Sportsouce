package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.common.startStatusBackground

@Composable
internal fun StartStatus(
    modifier: Modifier = Modifier,
    status: StartItem.StartStatus,
    date: String
) {
    Column(modifier = modifier) {
        val color = startStatusBackground(status.code)
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .border(width = 4.dp, color = color, shape = Shapes.medium)
                .clip(Shapes.medium)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                text = status.name,
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = color,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clip(Shapes.medium)
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                text = date,
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}