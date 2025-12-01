package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.screens.FullImageContent
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.core_ui.items.window.screenWidthDp
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.start.components.StartRegistrationButton
import com.markettwits.sportsouce.start.presentation.start.components.StartShareActionButton
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun StartScreenContent(
    data: StartItem,
    backgroundColor: Color,
    recommendedStarts: List<StartsListItem>,
    seriesStarts: List<StartsListItem>,
    error: Throwable? = null,
    isLoading: Boolean,
    isPartialData: Boolean = false,
    onClickRetry: () -> Unit,
    onClickBack: () -> Unit,
    onClickRegistration: () -> Unit,
    onClickMembers: (List<StartMembersUi>) -> Unit,
    onClickMembersResults: () -> Unit,
    onClickImage: () -> Unit,
    onClickFullAlbum: (StartItem.Album) -> Unit,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
    onClickShare: () -> Unit,
    onClickRelatedStarts: () -> Unit,
    onClickRecommendedStart: (StartsListItem) -> Unit,
    comments: @Composable (Modifier) -> Unit,
    donations: @Composable (Modifier) -> Unit,
) {
    val windowSize = calculateWindowSizeClass()

    PullToRefreshScreen(isRefreshing = isLoading, onRefresh = {
        onClickRetry()
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (windowSize.isLarge) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val imageWith = windowSize.screenWidthDp.value / 2.5f
                    Box(
                        modifier = Modifier
                            .width(imageWith.dp)
                            .fillMaxHeight()
                    ) {
                        FullImageContent(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                                .noRippleClickable { onClickImage() },
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            imageUrl = data.image,
                            isPortrait = false
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            StartScreenInnerContent(
                                modifier = Modifier,
                                data = data,
                                error = error,
                                recommendedStarts = recommendedStarts,
                                seriesStarts = seriesStarts,
                                isPartialData = isPartialData,
                                onClickMembers = onClickMembers,
                                onClickRecommendedStart = onClickRecommendedStart,
                                onClickMembersResults = onClickMembersResults,
                                onClickUrl = onClickUrl,
                                onClickPhone = onClickPhone,
                                onClickFullAlbum = onClickFullAlbum,
                                onClickRetry = onClickRetry,
                                onClickRelatedStarts = onClickRelatedStarts,
                                donations = donations,
                                comments = comments
                            )
                        }
                        StartRegistrationButton(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(10.dp),
                            startStatus = data.startStatus,
                            isLoading = isLoading,
                            hasError = error != null,
                            onClickRegistration = onClickRegistration
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        FullImageContent(
                            modifier = Modifier.clickable { onClickImage() },
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            imageUrl = data.image
                        )
                        StartScreenInnerContent(
                            modifier = Modifier,
                            data = data,
                            error = error,
                            recommendedStarts = recommendedStarts,
                            seriesStarts = seriesStarts,
                            isPartialData = isPartialData,
                            onClickMembers = onClickMembers,
                            onClickMembersResults = onClickMembersResults,
                            onClickRecommendedStart = onClickRecommendedStart,
                            onClickUrl = onClickUrl,
                            onClickPhone = onClickPhone,
                            onClickRetry = onClickRetry,
                            onClickFullAlbum = onClickFullAlbum,
                            donations = donations,
                            onClickRelatedStarts = onClickRelatedStarts,
                            comments = comments
                        )
                    }
                    StartRegistrationButton(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(10.dp),
                        startStatus = data.startStatus,
                        isLoading = isLoading,
                        hasError = error != null,
                        onClickRegistration = onClickRegistration
                    )
                }
            }
            BackFloatingActionButton(
                modifier = Modifier.align(Alignment.TopStart),
            ) {
                onClickBack()
            }
            StartShareActionButton(
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                onClickShare()
            }
        }
    }
}
