package com.markettwits.core_ui.components.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun TopBarWithClip(modifier: Modifier = Modifier, title: String, goBack: () -> Unit) {
    Box(
        modifier
            .shadow(4.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 5.dp, end = 8.dp)
            .padding(vertical = 2.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            modifier = modifier
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
                .align(Alignment.Center)
                .padding(start = 30.dp),
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold,
            fontSize = 18.sp
        )
    }
}