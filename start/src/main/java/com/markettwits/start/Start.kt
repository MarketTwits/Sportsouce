package com.markettwits.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun StartScreen(component: StartScreen) {
    val text = component.start.subscribeAsState()

    val startData = text.value.start_data
    Box(modifier = Modifier.fillMaxSize()) {
        if (startData != null) {
            Text(text = startData.name, color = SportSouceColor.SportSouceBlue)
        }
    }
}
