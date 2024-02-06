package com.markettwits.start.presentation.member.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun MemberContinueButton(modifier: Modifier = Modifier, onClickContinue: () -> Unit) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceLighBlue),
        onClick = { onClickContinue() }
    ) {
        Text(
            text = "Продолжить",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}