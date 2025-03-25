package com.markettwits.sportsouce.unauthorized.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
            .background(MaterialTheme.colorScheme.primary)
    ) {
        when (state) {
            is UnAuthorizedProfileUiState.Base -> {}
            is UnAuthorizedProfileUiState.Error -> {
                UnAuthorizedProfileContent(onClickAuth = {
                    component.signIn()
                })
            }
            is UnAuthorizedProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
