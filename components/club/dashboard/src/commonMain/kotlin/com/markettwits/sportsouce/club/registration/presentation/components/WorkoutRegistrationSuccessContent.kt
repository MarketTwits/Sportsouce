package com.markettwits.sportsouce.club.registration.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import sportsouce.components.club.dashboard.generated.resources.Res

@Composable
internal fun WorkoutRegistrationSuccessContent(
    modifier: Modifier = Modifier,
    onClickNext: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WorkoutRegistrationSuccessContentImage()
            Spacer(modifier = Modifier.padding(14.dp))
            Text(
                text = "Вы подали заявку на участие в клубе",
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold(),
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(14.dp))
            Text(
                text = "Ожидайте звонка, мы вам в скором времени перезвоним",
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.regular(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(14.dp))
            ButtonContentBase(
                title = "Продолжить",
                containerColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                onClick = onClickNext
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun WorkoutRegistrationSuccessContentImage(modifier: Modifier = Modifier) {
    var file by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        file = Res.readBytes("files/blue_success_animation.json").decodeToString()
    }
    val composition by rememberLottieComposition { LottieCompositionSpec.JsonString(file) }
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.height(300.dp),
            painter = rememberLottiePainter(
                composition = composition,
                iterations = Compottie.IterateForever,
            ),
            contentDescription = null,
        )
    }
}