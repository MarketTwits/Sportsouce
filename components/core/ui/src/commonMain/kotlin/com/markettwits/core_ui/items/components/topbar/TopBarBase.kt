package com.markettwits.core_ui.items.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun TopBarBase(
    modifier: Modifier = Modifier,
    title: String,
    isStatusBarHandle: Boolean = true,
    goBack: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(sauceTopBarContainerColor())
            .then(if (isStatusBarHandle) Modifier.windowInsetsPadding(WindowInsets.statusBars) else Modifier)
            .padding(start = 5.dp, end = 8.dp)
            .heightIn(min = 56.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart),
            onClick = { goBack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Назад",
                tint = SauceTheme.colors.textPrimary,
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 48.dp)
                .fillMaxWidth(),
            text = title,
            color = SauceTheme.colors.textPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
