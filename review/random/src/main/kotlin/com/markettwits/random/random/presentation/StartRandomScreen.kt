package com.markettwits.random.random.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.random.random.presentation.store.StartRandomStore

@Composable
fun StartRandomScreen(component : StartRandomComponent) {
    val state by component.value.collectAsState()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = SportSouceColor.SportSouceBlue,
                strokeCap = StrokeCap.Round
            )
        }
        if (state.isError){
            FailedScreen(
                message = state.message,
                onClickHelp = { /*TODO*/ }) {
                component.obtainEvent(StartRandomStore.Intent.Retry)
            }
        }
    }
}