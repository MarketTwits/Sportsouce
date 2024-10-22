package com.markettwits.core.errors.impl.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun SauceThrowableRetryButton(
    modifier: Modifier = Modifier,
    onClick : () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(
            text = "Повторить",
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp
        )
    }
}