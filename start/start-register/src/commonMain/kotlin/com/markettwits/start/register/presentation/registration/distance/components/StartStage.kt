package com.markettwits.start.register.presentation.registration.distance.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.start.register.presentation.member.screen.MemberScreen
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore
import com.markettwits.start.register.presentation.registration.distance.component.StartDistanceComponent
import com.markettwits.start.register.presentation.registration.pay.components.price.ButtonContent


@Composable
internal fun StartStage(
    modifier: Modifier = Modifier,
    component: StartDistanceComponent,
) {

    val state by component.state.collectAsState()

    val child by component.childSlot.subscribeAsState()

    child.child?.instance?.also {
        when (it) {
            is StartDistanceComponent.Child.StartRegistrationMember ->
                Dialog(onDismissRequest = {
                    it.component.obtainEvent(RegistrationMemberStore.Intent.Pop)
                }) {
                    MemberScreen(
                        modifier = Modifier
                            .clip(Shapes.medium)
                            .fillMaxSize(),
                        component = it.component
                    )
                }
        }
    }

    Column(
        modifier = modifier
    ) {
        StartRegistrationDistanceContent(
            distance = state.distance,
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