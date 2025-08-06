package com.markettwits.sportsouce.unauthorized.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import sportsouce.components.profile.unauthorized.generated.resources.Res


@Composable
internal fun BoxScope.UnAuthorizedProfileContent(onClickAuth: () -> Unit) {
    var file by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        file = Res.readBytes(
            "files/un_auth_animation.json"
        ).decodeToString()
    }
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.JsonString(file))
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .align(Alignment.Center)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(300.dp),
            composition = composition
        )
        Text(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Ещё не с нами ? ",
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.tertiary
        )
        Button(
            modifier = Modifier
                .width(250.dp)
                .padding(10.dp),
            shape = Shapes.large,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            onClick = {
                onClickAuth()
            }) {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                text = "Авторизация",
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = "Войдите или создайте профиль, чтобы открыть\n" +
                    "для себя весь функционал приложения ! ",
            fontSize = 12.sp,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.outline
        )
    }
}