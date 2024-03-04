package com.markettwits.profile.internal.sign_in.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito

@Composable
internal fun ForgotPasswordText(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Забыли пароль ?",
            fontFamily = FontNunito.regular,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}