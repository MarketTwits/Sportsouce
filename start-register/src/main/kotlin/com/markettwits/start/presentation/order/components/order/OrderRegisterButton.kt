package com.markettwits.start.presentation.order.components.order

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun OrderRegisterButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        enabled = isEnabled,
        colors = ButtonDefaults.textButtonColors(
            containerColor = SportSouceColor.SportSouceLighBlue,
            disabledContainerColor = SportSouceColor.SportSouceBlue.copy(alpha = 0.3f)
        ),
        onClick = { onClick() }
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, strokeCap = StrokeCap.Round)
        }
        Text(
            text = "Зарегестрироваться",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}