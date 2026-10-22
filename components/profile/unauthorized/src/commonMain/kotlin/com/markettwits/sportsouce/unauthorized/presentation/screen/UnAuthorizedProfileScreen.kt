package com.markettwits.sportsouce.unauthorized.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.markettwits.core_ui.items.components.progress.CircularProgressIndicatorBase
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.unauthorized.presentation.component.UnAuthorizedProfileComponent
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileContent
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileUiState

@Composable
fun UnAuthorizedProfileScreen(component: UnAuthorizedProfileComponent) {
    val state by component.state.subscribeAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SauceTheme.colors.background)
    ) {
        when (state) {
            is UnAuthorizedProfileUiState.Base -> {}
            is UnAuthorizedProfileUiState.Error -> {
                val playIntro = remember(component) { component.consumeWelcomeIntro() }
                UnAuthorizedProfileContent(playIntro = playIntro, content = (state as UnAuthorizedProfileUiState.Error).welcome,
                    onClickAuth = component::signIn)
            }
            is UnAuthorizedProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicatorBase(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
