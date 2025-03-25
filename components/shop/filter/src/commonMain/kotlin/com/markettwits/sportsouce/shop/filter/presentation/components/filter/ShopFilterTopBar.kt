package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun ShopFilterTopBar(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    onClickReset: () -> Unit,
) {
    Box(
        modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 5.dp, end = 8.dp)
            .padding(vertical = 2.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            modifier = modifier.align(Alignment.CenterStart),
            onClick = onClickBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 30.dp),
            text = "Фильтр",
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 18.sp
        )
        IconButton(
            modifier = modifier.align(Alignment.CenterEnd),
            onClick = onClickReset
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}