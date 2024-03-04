package com.markettwits.profile.internal.sign_up.presentation.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.profile.internal.sign_up.presentation.component.SignUpComponent
import com.markettwits.profile.internal.sign_up.presentation.content.components.SignUpScreenContent
import com.markettwits.profile.internal.sign_up.presentation.store.SignUpStore

@Composable
internal fun SignUpScreen(component: SignUpComponent) {
    val state by component.state.collectAsState()
    SportSouceTheme {
        SignUpScreenContent(
            state = state,
            onValueChanged = {
                component.obtainEvent(SignUpStore.Intent.ChangeValue(it))
            },
            onClickSignUp = {
                component.obtainEvent(SignUpStore.Intent.OnClickRegister)
            },
            onConsumedEvent = {
                component.obtainEvent(SignUpStore.Intent.OnConsumedEvent)
            }
        )
    }
}