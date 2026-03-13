package com.markettwits.sportsouce.start.register.presentation.registration.distance.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.start.register.presentation.registration.distance.component.StartDistanceComponent
import com.markettwits.sportsouce.start.register.presentation.registration.pay.components.price.ButtonContent


@Composable
internal fun StartStage(
    modifier: Modifier = Modifier,
    component: StartDistanceComponent,
) {

    val state by component.state.collectAsState()

    Column(
        modifier = modifier
    ) {
        StartRegistrationDistanceContent(
            distance = state.distance,
            invalidStageIds = state.invalidStageIds,
            validationAttemptTick = state.validationAttemptTick,
            onClickStartStatement = {
                component.onClickStartMember(it)
            },
            onChangeStatementField = {
                component.onChangeStatementAnswer(it)
            },
            onChangeDistanceField = {
                component.onChangeDistanceAnswer(it)
            },
        )
        state.ButtonContent(
            onClickGoBack = {
                component.onClickGoBack()
            },
            onClickGoNext = {
                component.onClickGoNext()
            }
        )
    }
}
