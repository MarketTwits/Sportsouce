package com.markettwits.registrations.registrations.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun BoxScope.RegistrationsPayButton(
    modifier: Modifier = Modifier,
    count: Int,
    cost: Int,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier
            .padding(25.dp)
            .align(Alignment.BottomCenter)
            .fillMaxWidth(),
        shape = Shapes.large,
        colors = ButtonDefaults.elevatedButtonColors(containerColor = SportSouceColor.SportSouceBlue),
        onClick = {
            onClick()
        }) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = modifier,
                text = "К оплате",
                color = Color.White,
                fontFamily = FontNunito.bold,
                fontSize = 16.sp
            )
            Text(
                modifier = modifier,
                text = "$count шт, $cost ₽",
                color = Color.White,
                fontFamily = FontNunito.medium,
                fontSize = 12.sp
            )
        }

    }
}