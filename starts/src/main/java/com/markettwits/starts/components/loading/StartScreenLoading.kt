package com.markettwits.starts.components.loading


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun StartScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = SportSouceColor.SportSouceBlue,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}