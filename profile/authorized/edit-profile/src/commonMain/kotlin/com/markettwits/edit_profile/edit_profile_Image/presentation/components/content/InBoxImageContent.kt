package com.markettwits.edit_profile.edit_profile_Image.presentation.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            tint = Color.LightGray
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Изображение",
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp,
            color = Color.LightGray
        )
    }
}