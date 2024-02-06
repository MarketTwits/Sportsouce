package com.markettwits.start.presentation.promo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun RegistrationPromoButton(
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
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(2.dp)
                    .size(20.dp),
                color = Color.White,
                strokeCap = StrokeCap.Round
            )
        } else {
            Text(
                modifier = Modifier.padding(2.dp),
                text = "Применить",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}