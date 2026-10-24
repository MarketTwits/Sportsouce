package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.screens.FullImageContent
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.start.component.StartFavoriteState
import com.markettwits.sportsouce.start.presentation.start.components.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartScreenContent(
    data: StartItem,
    favoriteState: StartFavoriteState,
    recommendedStarts: List<StartsListItem>,
    seriesStarts: List<StartsListItem>,
    archiveStarts: List<StartsListItem> = emptyList(),
    archiveLoading: Boolean = false,
    archiveError: Boolean = false,
    onClickArchive: () -> Unit = {},
    onRetryArchive: () -> Unit = {},
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
    onClickAddToFavourites: () -> Unit,
    comments: @Composable (Modifier) -> Unit,
    donations: @Composable (Modifier) -> Unit,
) {
    key(data.id) {
        PullToRefreshScreen(isRefreshing = isLoading, onRefresh = onClickRetry) {
            BoxWithConstraints(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                val wide = maxWidth >= 840.dp && maxHeight >= 600.dp
                val scrollState = rememberScrollState()
                val density = LocalDensity.current
                var registrationHeight by remember { mutableIntStateOf(0) }
                Row(Modifier.fillMaxSize()) {
                    if (wide) {
                        Box(Modifier.weight(0.4f).fillMaxHeight()
                            .semantics { contentDescription = "Открыть афишу старта" }
                            .clickable(role = Role.Button, onClick = onClickImage)) {
                            FullImageContent(
                                modifier = Modifier.fillMaxSize(),
                                backgroundColor = SauceTheme.colors.background,
                                imageUrl = data.image,
                                isPortrait = false,
                            )
                        }
                    }
                    Box(Modifier.weight(if (wide) 0.6f else 1f).fillMaxHeight()) {
                        Column(
                            Modifier.fillMaxSize().verticalScroll(scrollState)
                                .then(if (data.startStatus.code != 3) Modifier.navigationBarsPadding() else Modifier)
                        ) {
                            if (!wide) {
                                Box(Modifier.semantics { contentDescription = "Открыть афишу старта" }
                                    .clickable(role = Role.Button, onClick = onClickImage)) {
                                    FullImageContent(
                                        backgroundColor = SauceTheme.colors.background,
                                        imageUrl = data.image,
                                    )
                                }
                            }
                            StartScreenInnerContent(
                                modifier = Modifier,
                                data = data,
                                error = error,
                                recommendedStarts = recommendedStarts,
                                seriesStarts = seriesStarts,
                                archiveStarts = archiveStarts,
                                archiveLoading = archiveLoading,
                                archiveError = archiveError,
                                onClickArchive = onClickArchive,
                                onRetryArchive = onRetryArchive,
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
                                comments = comments,
                            )
                            if (data.startStatus.code == 3) {
                                Spacer(Modifier.height(with(density) { registrationHeight.toDp() }))
                            }
                        }
                        if (data.startStatus.code == 3) {
                            StartRegistrationButton(
                                modifier = Modifier.align(Alignment.BottomCenter)
                                    .onSizeChanged { registrationHeight = it.height }
                                    .navigationBarsPadding()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                startStatus = data.startStatus,
                                isLoading = isLoading,
                                isPartialData = isPartialData,
                                hasError = error != null,
                                onClickRegistration = onClickRegistration,
                            )
                        }
                    }
                }
                BackFloatingActionButton(Modifier.align(Alignment.TopStart), onClickBack)
                Row(Modifier.align(Alignment.TopEnd)) {
                    StartShareActionButton(onClick = onClickShare)
                    StartFavoriteActionButton(
                        isFavorite = favoriteState.isFavorite,
                        isLoading = favoriteState.isLoading(),
                        onClick = onClickAddToFavourites,
                    )
                }
            }
        }
    }
}
