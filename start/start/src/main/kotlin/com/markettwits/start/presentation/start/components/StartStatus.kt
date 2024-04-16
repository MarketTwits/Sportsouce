package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.common.startStatusBackground

@Composable
fun StartStatus(modifier: Modifier = Modifier, status: StartStatus, date: String) {
    Column(modifier = modifier) {
        val color = startStatusBackground(status.code)
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .border(width = 3.dp, color = color, shape = Shapes.medium)
                .clip(Shapes.medium)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                text = status.name,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = color,
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clip(Shapes.medium)
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                text = date,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}