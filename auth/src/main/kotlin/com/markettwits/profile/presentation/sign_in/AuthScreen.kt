package com.markettwits.profile.presentation.sign_in

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.profile.presentation.sign_in.components.SignInContent

@Composable
fun AuthScreen(component: SignInScreen) {
    val state by component.state.subscribeAsState()
    val fieldState by component.fieldState.subscribeAsState()
    SportSouceTheme {
        SignInContent(state = state, fieldState = fieldState, component = component)
    }
}


@Preview()
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthScreenPreview() {
    SportSouceTheme {
        AuthScreen(MockSignInScreenComponent())
    }
}

