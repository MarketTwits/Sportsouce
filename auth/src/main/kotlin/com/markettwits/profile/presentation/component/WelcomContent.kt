package com.markettwits.profile.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.markettwits.auth.R
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun WelcomeContent() {
    Image(
        painter = painterResource(id = R.drawable.im_welcom_image),
        contentDescription = "welcom image"
    )
    Text(
        text = "Добро пожаловать на SportSouce !",
        fontFamily = FontNunito.bold,
        fontSize = 24.sp,
        color = SportSouceColor.SportSouceBlue
    )
}