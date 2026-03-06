package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun BoxScope.InBoxImageContent() {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(10.dp)
                .size(40.dp),
            imageVector = Icons.Default.Image,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Изображение",
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}