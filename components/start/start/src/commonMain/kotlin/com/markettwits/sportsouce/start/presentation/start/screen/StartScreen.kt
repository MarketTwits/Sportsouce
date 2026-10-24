package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.sportsouce.start.presentation.comments.components.StartCommentsCompactPanel
import com.markettwits.sportsouce.start.presentation.start.component.toCommentData
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenComponent
import com.markettwits.sportsouce.start.presentation.start.components.StartSupport
import com.markettwits.sportsouce.start.presentation.start.components.StartDetailsBody
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponent

@Composable
fun StartScreen(
    startComponent: StartScreenComponent,
    startSupportComponent: StartSupportComponent,
) {
    val state by startComponent.start.collectAsState()
    var fullImage by rememberSaveable(state.startItem?.id) {
        mutableStateOf(false)
    }
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarSuccess by remember { mutableStateOf(false) }
    val snackBarColors = if (snackBarSuccess) SauceTheme.colors.success else SauceTheme.colors.danger

    val isShowRegButton = state.startItem?.startStatus?.code == 3
    Scaffold(
        containerColor = SauceTheme.colors.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    modifier = Modifier.padding(bottom = if (isShowRegButton) 60.dp else 10.dp),
                    contentColor = snackBarColors.content,
                    containerColor = snackBarColors.container,
                    snackbarData = it
                )
            }
        },
    ) {
        Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
            StartDetailsBody(
                hasData = state.startItem != null,
                isLoading = state.isLoading,
                error = state.error,
                onClickRetry = { startComponent.obtainEvent(StartScreenStore.Intent.OnClickRetry) },
                onClickBack = { startComponent.obtainEvent(StartScreenStore.Intent.OnClickBack) },
            ) {
                state.startItem?.let { data ->
                    StartScreenContent(
                        data = data,
                        favoriteState = state.favoriteState,
                        recommendedStarts = state.startsRecommended,
                        seriesStarts = state.startsSeries,
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
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickFullAlbum(it))
                        },
                        donations = { modifier ->
                            StartSupport(
                                modifier = modifier,
                                component = startSupportComponent,
                                sponsors = data.sponsors,
                                onClickSponsorUrl = { url ->
                                    startComponent.obtainEvent(StartScreenStore.Intent.OnClickUrl(url))
                                },
                                eventContent = {
                                    startComponent.obtainEvent(
                                        StartScreenStore.Intent.TriggerEvent(
                                            it.message,
                                            it.success
                                        )
                                    )
                                }
                            )
                        },
                        comments = { modifier ->
                            StartCommentsCompactPanel(
                                modifier = modifier,
                                comments = data.commentsRemote,
                                maxComments = 5,
                                onClickViewAll = {
                                    startComponent.obtainEvent(
                                        StartScreenStore.Intent.OpenStartCommentsScreen(CommentMode.Base)
                                    )
                                },
                                onClickReply = { replier, commentId ->
                                    data.commentsRemote.rows.find { it.id == commentId }?.let { comment ->
                                        startComponent.obtainEvent(
                                            StartScreenStore.Intent.OpenStartCommentsScreen(
                                                CommentMode.Reply(replier, commentId, comment.toCommentData())
                                            )
                                        )
                                    }
                                },
                                onClickTextField = {
                                    startComponent.obtainEvent(
                                        StartScreenStore.Intent.OpenStartCommentsScreen(CommentMode.Base)
                                    )
                                }
                            )
                        },
                        onClickMembersResults = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickMembersResult)
                        },
                        onClickRecommendedStart = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickStartRecommended(it))
                        },
                        onClickRelatedStarts = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickRelatedStarts)
                        },
                        onClickShare = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickShare)
                        },
                        onClickAddToFavourites = {
                            startComponent.obtainEvent(StartScreenStore.Intent.OnClickFavorite)
                        }
                    )
                    if (fullImage) {
                        FullImageScreen(image = data.image) { fullImage = !fullImage }
                    }
                }
            }
        }
        EventEffect(
            event = state.event,
            onConsumed = {
                startComponent.obtainEvent(StartScreenStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarSuccess = it.success
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }
}