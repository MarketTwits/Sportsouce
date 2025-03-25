package com.markettwits.sportsouce.start.register.presentation.distances.screen

import androidx.compose.runtime.Composable
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesComponent
import com.markettwits.sportsouce.start.register.presentation.distances.components.StartDistancesContent

@Composable
fun StartDistancesScreen(
    component: StartDistancesComponent
) {
    val state = component.state

    StartDistancesContent(
        state = state,
        onClickGoBack = component::onClickGoBack,
        onClickSelectedDistance = component::onClickDistance
    )
}