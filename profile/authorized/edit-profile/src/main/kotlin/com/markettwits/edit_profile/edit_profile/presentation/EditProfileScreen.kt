package com.markettwits.edit_profile.edit_profile.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.components.top_bar.TopBarBase
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.edit_profile.edit_profile.presentation.components.UserDataPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiState
import com.markettwits.profile.presentation.component.edit_profile.presentation.MockEditProfileScreen
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.MyInfoPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.MySocialNetworkPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.SaveChangesButton
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.TabBar
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(component: EditProfileComponent) {
    val state by component.state.subscribeAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .background(Color.White)

        ) {
            if (!state.isLoading && state.data.isNotEmpty()) {
                TopBarBase(title = "Мой профиль") {
                    component.pop()
                }
                TabBar {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        when (it) {
                            0 -> MySocialNetworkPage(
                                user = state.data[0] as EditProfileUiPage.MySocialNetwork,
                                onUserChange = { updatedUser ->
                                    component.obtainTextFiled(updatedUser)
                                }
                            )

                            1 ->
                                UserDataPage(
                                    page = state.data[1] as EditProfileUiPage.UserData,
                                    onUserChange = { updatedUser ->
                                        component.obtainTextFiled(updatedUser)
                                    }
                                )

                            2 -> MyInfoPage(
                                page = state.data[2] as EditProfileUiPage.MyInfo,
                                onUserChange = { updatedUser ->
                                    component.obtainTextFiled(updatedUser)
                                }
                            )
                        }
                        SaveChangesButton(
                            state = state
                        ) {
                            component.saveChanges()
                        }
                    }
                }
            }
        }
        if (state is EditProfileUiState.Error) {
            FailedScreen(
                message = state.message,
                onClickBack = {
                    component.pop()
                },
                onClickRetry = {
                    component.launch()
                })
        }
        if (state is EditProfileUiState.Loading) {
            CircularProgressIndicator(
                modifier =
                Modifier
                    .align(Alignment.Center),
                color = SportSouceColor.SportSouceBlue
            )
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Snackbar(
                contentColor = Color.White,
                containerColor = if (state is EditProfileUiState.Error) SportSouceColor.SportSouceLightRed else SportSouceColor.SportSouceLighBlue,
                snackbarData = it
            )
        }
        LaunchedEffect(Unit) {
            component.events.collect {
                when (it) {
                    is EditProfileEvent.ShowError -> {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = it.message,
                                duration = SnackbarDuration.Long,
                                withDismissAction = true
                            )
                        }
                    }

                    is EditProfileEvent.ShowSuccess -> {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = it.message,
                                duration = SnackbarDuration.Long,
                                withDismissAction = true
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(MockEditProfileScreen())
}