package com.markettwits.core_ui.items.components.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun TopBarClipWithLabel(
    modifier: Modifier = Modifier,
    title: String,
    goBack: () -> Unit,
    onClickLabel: () -> Unit
) {
    Box(
        modifier
            .shadow(4.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 5.dp, end = 8.dp)
            .padding(vertical = 2.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart),
            onClick = { goBack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 18.sp
        )
        IconButton(
            modifier = modifier.align(Alignment.CenterEnd),
            onClick = { onClickLabel() }
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "check",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}