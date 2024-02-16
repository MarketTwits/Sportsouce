package com.markettwits.profile.presentation.component.unauthorized.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.profile.presentation.ProfileUiState
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfile

@Composable
fun NewUnAuthorizedProfileScreen(component: UnAuthorizedProfile) {
    val state by component.state.subscribeAsState()
    SportSouceTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.primary
                )
        ) {
            when (state) {
                is ProfileUiState.Base -> {}
                is ProfileUiState.Error -> {
                    UnAuthorizedProfileContent(onClickAuth = {
                        component.signIn()
                    })
                }

                is ProfileUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewUnAuthorizedProfileScreenPreview() {
    //  NewUnAuthorizedProfileScreen()
}