package com.markettwits.edit_profile.edit_profile_info.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.components.top_bar.TopBarClipWithLabel
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.edit_profile.edit_profile_info.presentation.component.EditProfileInfoComponent
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore

@Composable
fun EditProfileInfoScreen(component: EditProfileInfoComponent) {
    val state by component.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarColor = remember {
        SportSouceColor.SportSouceLighBlue
    }
    Scaffold(topBar = {
        TopBarClipWithLabel(
            title = "Редактировать профиль",
            goBack = {
                component.obtainEvent(EditProfileInfoStore.Intent.GoBack)
            },
            onClickLabel = {
                component.obtainEvent(EditProfileInfoStore.Intent.OnClickUpdate)
            }
        )
    }, snackbarHost = {
        SnackbarHost(
            hostState = snackBarHostState,
        ) {
            Snackbar(
                contentColor = Color.White,
                containerColor = snackBarColor,
                dismissActionContentColor = Color.White,
                snackbarData = it
            )
        }
    }
    ) { padding ->
        state.userData?.let {
            EditProfileInfoFieldsContent(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(top = padding.calculateTopPadding()),
                user = it,
                teams = state.teams,
                cities = state.cities,
                onUserChange = { newValue ->
                    component.obtainEvent(EditProfileInfoStore.Intent.UpdateState(newValue))
                }
            )
        }
        if (state.isLoading) {
            LoadingFullScreen(modifier = Modifier.padding(top = padding.calculateTopPadding()))
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickHelp = { /*TODO*/ }
            ) {
                component.obtainEvent(EditProfileInfoStore.Intent.Retry)
            }
        }
    }
    EventEffect(
        event = state.event,
        onConsumed = {
            component.obtainEvent(EditProfileInfoStore.Intent.OnConsumedEvent)
        },
    ) {
        snackBarColor =
            if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
        snackBarHostState.showLongMessageWithDismiss(message = it.message)
    }
}