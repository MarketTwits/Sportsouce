package com.markettwits.sportsouce.unauthorized.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.theme.SauceTheme
import io.github.alexzhirkevich.compottie.*
import sportsouce.components.profile.unauthorized.generated.resources.Res

@Composable
internal fun BoxScope.UnAuthorizedProfileContent(onClickAuth: () -> Unit) {
    var file by remember { mutableStateOf("") }
    LaunchedEffect(Unit) { file = Res.readBytes("files/un_auth_animation.json").decodeToString() }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val illustrationHeight = (maxHeight * 0.3f).coerceIn(100.dp, 200.dp)
        Column(Modifier.align(Alignment.Center).widthIn(max = 440.dp).fillMaxWidth()
            .verticalScroll(rememberScrollState()).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (file.isBlank()) SauceSkeleton(Modifier.fillMaxWidth().height(illustrationHeight))
            else {
                val composition by rememberLottieComposition { LottieCompositionSpec.JsonString(file) }
                Image(painter = rememberLottiePainter(composition, iterations = Compottie.IterateForever),
                    contentDescription = null, modifier = Modifier.fillMaxWidth().height(illustrationHeight))
            }
            Text("Ваш спортивный профиль", style = MaterialTheme.typography.titleMedium,
                color = SauceTheme.colors.textPrimary, textAlign = TextAlign.Center)
            Text("Войдите или создайте аккаунт, чтобы видеть свои регистрации, участников и сохранённые старты.",
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary, textAlign = TextAlign.Center)
            SauceButton("Войти", onClickAuth)
        }
    }
}
