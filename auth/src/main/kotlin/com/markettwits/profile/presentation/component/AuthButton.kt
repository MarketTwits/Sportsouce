package com.markettwits.profile.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = SportSouceColor.SportSouceBlue,
            disabledContainerColor = SportSouceColor.SportSouceBlue.copy(alpha = 0.3f),
        ),
        enabled = enabled,
        shape = Shapes.medium,
        onClick = { onClick() }
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(
                text = title,
                fontFamily = FontNunito.bold,
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}