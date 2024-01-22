package com.markettwits.start.presentation.registration.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor


@Composable
fun StartRegistrationButtonPanel(modifier: Modifier = Modifier, onClickSave : () -> Unit, onClickPay : () -> Unit) {
    Row(modifier = modifier.padding(10.dp)) {
        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceLighBlue),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp
            ),
            onClick = { onClickSave() }
        ) {
            Text(
                text = "Сохранить",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Button(
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp
            ),
            colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceLighBlue),
            onClick = { onClickPay() }
        ) {
            Text(
                text = "Оплатить",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}
@Composable
fun StartRegistrationButtonPanelWithoutPayment(modifier: Modifier = Modifier, onClickSave : () -> Unit) {
    Row(modifier = modifier.padding(10.dp)) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceLighBlue),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp
            ),
            onClick = { onClickSave() }
        ) {
            Text(
                text = "Сохранить",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}