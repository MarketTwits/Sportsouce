package com.markettwits.sportsouce.auth.flow.internal.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes

@Composable
internal fun ConsumeRowContent(
    modifier: Modifier = Modifier,
    title: String,
    onClickConsume: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(Shapes.medium)
            .fillMaxWidth()
            .clickable(onClick = onClickConsume),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(10.dp),
            text = title,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.medium(),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}