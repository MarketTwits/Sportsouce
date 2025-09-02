package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.start.presentation.comments.component.StartCommentsComponent
import com.markettwits.sportsouce.start.presentation.comments.components.StartCommentsContent
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenComponent
import com.markettwits.sportsouce.start.presentation.start.components.StartSupport
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponent

@Composable
fun StartScreen(
    startComponent: StartScreenComponent,
    startCommentsComponent: StartCommentsComponent,
    startSupportComponent: StartSupportComponent,
) {
    val state by startComponent.start.collectAsState()
    var fullImage by rememberSaveable {
        mutableStateOf(false)
    }
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLightRed)
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = snackBarColor,
                    snackbarData = it
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .imePadding()
        ) {
            AnimatedVisibility(
                visible = state.startItem != null,
                enter = fadeIn(animationSpec = tween(durationMillis = 400)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                state.startItem?.let { data ->
                    StartScreenContent(
                        data = data,
                        starts = state.startsRecommended,
                        error = state.error,
                        isLoading = state.isLoading,
                        isPartialData = state.isPartialData,
                        onClickRetry = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickRetry)
                        },
                        onClickBack = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickBack)
                        },
                        onClickRegistration = {
                            startComponent.obtainEvent(
                                StartScreenStore.Intent.OnClickRegistration
                            )
                        },
                        onClickMembers = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickMembers(it))
                        },
                        onClickUrl = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickUrl(it))
                        },
                        onClickPhone = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickPhone(it))
                        },
                        onClickImage = {
                            fullImage = !fullImage
                        },
                        onClickFullAlbum = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickFullAlbum)
                        },
                        donations = { modifier ->
                            StartSupport(modifier = modifier, component = startSupportComponent) {
                                startComponent.obtainEvent(
                                    StartScreenStore.Intent.TriggerEvent(
                                        it.message,
                                        it.success
                                    )
                                )
                            }
                        },
                        comments = { modifier ->
                            StartCommentsContent(
                                modifier = modifier,
                                component = startCommentsComponent
                            ) {
                                startComponent.obtainEvent(
                                    StartScreenStore.Intent.TriggerEvent(
                                        it.message,
                                        it.success
                                    )
                                )
                            }
                        },
                        onClickMembersResults = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickMembersResult)
                        },
                        onClickRecommendedStart = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickStartRecommended(it))
                        },
                        onClickShare = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickShare)
                        }
                    )
                    if (fullImage) {
                        FullImageScreen(image = data.image) { fullImage = !fullImage }
                    }
                }
            }
            AnimatedVisibility(
                visible = state.isLoading && state.startItem == null,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 400))
            ) {
                LoadingFullScreen(
                    onClickBack = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickBack)
                    }
                )
            }
            state.error?.mapToSauceError()?.SauceErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                onClickRetry = {
                    startComponent.obtainEvent(StartScreenStore.Intent.OnClickRetry)
                },
                onClickGoBack = {
                    startComponent.obtainEvent(StartScreenStore.Intent.OnClickBack)
                }
            )
        }
        EventEffect(
            event = state.event,
            onConsumed = {
                startComponent.obtainEvent(StartScreenStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarColor =
                if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }
}