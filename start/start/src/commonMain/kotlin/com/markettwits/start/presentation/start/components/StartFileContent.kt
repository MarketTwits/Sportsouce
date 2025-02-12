package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun StartFileContent(
    modifier: Modifier,
    fileName : String,
) {
    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.tertiary)
            .fillMaxWidth(),
    ) {
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterStart),
            imageVector = Icons.Default.FilePresent,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiary,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp),
            text = fileName,
            color = MaterialTheme.colorScheme.onTertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp
        )
    }
}