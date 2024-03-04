package com.markettwits.profile.internal.common

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.markettwits.auth_flow.R
import com.markettwits.core_ui.theme.FontNunito

@Composable
internal fun WelcomeContent(content: String = "Добро пожаловать на SportSouce !") {
    Image(
        painter = painterResource(id = R.drawable.im_welcom_image),
        contentDescription = "welcom image"
    )
    Text(
        text = content,
        fontFamily = FontNunito.bold,
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.tertiary
    )
}