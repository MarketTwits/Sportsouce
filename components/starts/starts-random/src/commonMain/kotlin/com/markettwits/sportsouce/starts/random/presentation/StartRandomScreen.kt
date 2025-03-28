package com.markettwits.sportsouce.starts.random.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.sportsouce.starts.random.presentation.store.StartRandomStore

@Composable
fun StartRandomScreen(component: StartRandomComponent) {
    val state by component.value.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        if (state.isLoading) {
            LoadingFullScreen()
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickBack = {
                    component.obtainEvent(StartRandomStore.Intent.GoBack)
                },
                onClickRetry = {
                    component.obtainEvent(StartRandomStore.Intent.Retry)
                })
        }
    }
}