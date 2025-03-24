package com.markettwits.edit_profile.edit_social_network.presentation.screen

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.components.topbar.TopBarClipWithLabel
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.edit_profile.edit_social_network.presentation.component.EditProfileSocialNetworkComponent
import com.markettwits.edit_profile.edit_social_network.presentation.components.ProfileSocialNetworkContent
import com.markettwits.edit_profile.edit_social_network.presentation.components.ProfileSocialNetworkInfo
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore

@Composable
fun ProfileSocialNetworkScreen(component: EditProfileSocialNetworkComponent) {
    val state by component.state.collectAsState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val focusManager = LocalFocusManager.current
    var snackBarColor = remember {
        SportSouceColor.SportSouceLighBlue
    }
    Scaffold(topBar = {
        TopBarClipWithLabel(
            title = "Социальные сети",
            goBack = {
                component.obtainEvent(EditProfileSocialNetworkStore.Intent.GoBack)
            },
            onClickLabel = {
                component.obtainEvent(EditProfileSocialNetworkStore.Intent.OnCLickUpdate)
                focusManager.clearFocus()
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
        state.data?.let {
            Column(
                modifier = Modifier
                    .padding(top = padding.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)
            ) {
                ProfileSocialNetworkInfo(modifier = Modifier.align(Alignment.CenterHorizontally))
                ProfileSocialNetworkContent(
                    modifier = Modifier.padding(vertical = 10.dp),
                    user = it,
                    onUserChange = {
                        component.obtainEvent(EditProfileSocialNetworkStore.Intent.UpdateState(it))
                    }
                )
            }
        }
        if (state.isLoading) {
            LoadingFullScreen(
                modifier = Modifier.padding(top = padding.calculateTopPadding())
            )
        }
    }
    EventEffect(
        event = state.event,
        onConsumed = {
            component.obtainEvent(EditProfileSocialNetworkStore.Intent.OnConsumedEvent)
        },
    ) {
        snackBarColor =
            if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
        snackBarHostState.showLongMessageWithDismiss(message = it.message)
    }
}
