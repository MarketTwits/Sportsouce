package com.markettwits.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun StartScreen(component : StartScreen) {
    val text = component.start.subscribeAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = text.value)
    }
}