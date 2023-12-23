package com.markettwits.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.profile.presentation.MockProfileScreenComponent
import com.markettwits.profile.presentation.ProfileScreen
import com.markettwits.profile.presentation.ProfileUiState
import com.markettwits.profile.presentation.component.authorized.AuthorizedProfileScreen
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfileScreen

@Composable
fun ProfileScreen(component: ProfileScreen) {
    val state by component.nameState.subscribeAsState()
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (state) {
            is ProfileUiState.Initial -> {}
            is ProfileUiState.Base -> AuthorizedProfileScreen((state as ProfileUiState.Base).user.name){
                component.exit()
            }
            is ProfileUiState.UnAuthorization -> UnAuthorizedProfileScreen(component = component)
        }

    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(MockProfileScreenComponent())
}