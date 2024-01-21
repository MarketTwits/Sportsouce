package com.markettwits.start.presentation.common


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = SportSouceColor.SportSouceBlue,
            strokeCap = StrokeCap.Round,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}