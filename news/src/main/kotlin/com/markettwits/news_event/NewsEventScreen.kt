package com.markettwits.news_event

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color

@Composable
fun NewsEventScreen(component: NewsEventComponent) {
    val state = component.state.collectAsState()
    Text(text = state.value.news.title, color = Color.Black)
}