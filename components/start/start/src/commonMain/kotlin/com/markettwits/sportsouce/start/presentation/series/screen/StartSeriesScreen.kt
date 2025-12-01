package com.markettwits.sportsouce.start.presentation.series.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.sportsouce.start.presentation.series.component.StartSeriesComponent
import com.markettwits.sportsouce.start.presentation.series.components.StartSeriesContent
import com.markettwits.sportsouce.start.presentation.series.store.StartSeriesStore

@Composable
fun StartSeriesScreen(component: StartSeriesComponent) {
    val state by component.state.collectAsState()

    StartSeriesContent(
        items = state.items,
        currentStartId = state.currentStartId,
        onBack = {
            component.obtainEvent(StartSeriesStore.Intent.OnClickBack)
        },
        onClickStart = { start ->
            component.obtainEvent(StartSeriesStore.Intent.OnClickStart(start.id))
        }
    )
}
