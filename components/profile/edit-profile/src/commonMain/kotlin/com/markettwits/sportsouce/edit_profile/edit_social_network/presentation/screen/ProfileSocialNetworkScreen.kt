package com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.edit_profile.edit_profile_change_password.presentation.component.SaveChangesButton
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.component.EditProfileSocialNetworkComponent
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.components.ProfileSocialNetworkContent
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.components.ProfileSocialNetworkInfo
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore

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
    Scaffold(
        topBar = {
            TopBarWithClip(
                title = "Социальные сети",
                goBack = {
                    component.obtainEvent(EditProfileSocialNetworkStore.Intent.GoBack)
                },
            )
        },
        snackbarHost = {
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
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (state.isLoading) return@Scaffold
            SaveChangesButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                loading = state.isLoading,
                onClick = {
                    component.obtainEvent(EditProfileSocialNetworkStore.Intent.OnCLickUpdate)
                    focusManager.clearFocus()
                }
            )
        }
    ) { padding ->
        AdaptivePane {
            state.data?.let {
                Column(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding())
                        .padding(bottom = padding.calculateBottomPadding())
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

                    // Add bottom spacing to prevent keyboard overlap
                    Spacer(modifier = Modifier.height(150.dp))
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
}
