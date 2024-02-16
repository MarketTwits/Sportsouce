package com.markettwits.profile.presentation.component.unauthorized.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.profile.R

@Composable
fun BoxScope.UnAuthorizedProfileContent(onClickAuth: () -> Unit) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.un_auth_animation))
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .align(Alignment.Center)
            .padding(40.dp)
    ) {
        LottieAnimation(
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.height(300.dp),
            composition = composition
        )
        Text(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Ещё не с нами ? ",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            color = MaterialTheme.colorScheme.tertiary
        )
        Button(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = Shapes.large,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            onClick = {
                onClickAuth()
            }) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Авторизация",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Войдите или создайте профиль, чтобы открыть\n" +
                    "для себя весь функционал приложения ! ",
            fontSize = 12.sp,
            fontFamily = FontNunito.medium,
            color = Color.Gray
        )
    }

}