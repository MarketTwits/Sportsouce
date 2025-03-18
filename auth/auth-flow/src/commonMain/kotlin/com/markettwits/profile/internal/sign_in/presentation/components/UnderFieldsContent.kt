package com.markettwits.profile.internal.sign_in.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.profile.internal.common.AuthButton

@Composable
fun UnderFieldsContent(
    modifier: Modifier = Modifier,
    onClickRegistry: () -> Unit,
    onClickAuth: () -> Unit,
    isButtonEnabled: Boolean,
    isButtonLoading: Boolean,

    ) {
    Row(
        modifier = modifier.padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable(onClickRegistry)
                .padding(horizontal = 20.dp, vertical = 4.dp),
            text = "Забыли пароль ?",
            fontFamily = FontNunito.medium(),
            fontSize = 14.sp,
            color = Color.Gray,

            )
        AuthButton(
            modifier = modifier.weight(1f),
            "Войти",
            enabled = isButtonEnabled,
            loading = isButtonLoading,
            onClick = onClickAuth
        )
    }
}