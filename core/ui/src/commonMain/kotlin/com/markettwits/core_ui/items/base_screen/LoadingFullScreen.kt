package com.markettwits.core_ui.items.base_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton

@Composable
fun LoadingFullScreen(
    modifier: Modifier = Modifier,
    onClickBack: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
    ) {
        if (onClickBack != null) {
            BackFloatingActionButton {
                onClickBack()
            }
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.tertiary,
                strokeCap = StrokeCap.Round
            )
        }
    }
}