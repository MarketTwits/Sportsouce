package com.markettwits.start.presentation.order.presentation.components.extra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun StartRegistrationTopBar(goBack: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(Color.White)
            .padding(start = 5.dp, end = 8.dp)
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
                .clickable {
                    goBack()
                },
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "back",
            tint = SportSouceColor.SportSouceBlue,
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 30.dp),
            text = "Регистрация",
            color = SportSouceColor.SportSouceBlue,
            fontFamily = FontNunito.bold,
            fontSize = 18.sp
        )
    }
}