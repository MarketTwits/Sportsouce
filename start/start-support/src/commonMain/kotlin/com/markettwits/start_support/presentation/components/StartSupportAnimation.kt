package com.markettwits.start_support.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import sportsouce.start.start_support.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun StartSupportAnimation(modifier: Modifier = Modifier) {
    var file by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        file = Res.readBytes("files/wallet_animation.json").decodeToString()
    }
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.JsonString(file))
    Box(
        modifier = modifier
            .fillMaxWidth()
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            iterations = LottieConstants.IterateForever,
            composition = composition,
            speed = 0.6f
        )
    }
}