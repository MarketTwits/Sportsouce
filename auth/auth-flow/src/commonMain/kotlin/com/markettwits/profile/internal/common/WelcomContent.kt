package com.markettwits.profile.internal.common

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import sportsouce.auth.auth_flow.generated.resources.Res
import sportsouce.auth.auth_flow.generated.resources.im_welcom_image

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun WelcomeContent(content: String = "Добро пожаловать на SportSouce !") {
    Image(
        painter = painterResource(Res.drawable.im_welcom_image),
        contentDescription = "welcom image"
    )
    Text(
        text = content,
        fontFamily = FontNunito.bold(),
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.tertiary
    )
}