package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.screens.FullImageContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.core_ui.items.window.screenWidthDp
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.starts_common.domain.StartsListItem

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun StartScreenContent(
    data: StartItem,
    starts: List<StartsListItem>,
    isLoading: Boolean,
    onClickRetry: () -> Unit,
    onClickBack: () -> Unit,
    onClickRegistration: () -> Unit,
    onClickMembers: (List<StartMembersUi>) -> Unit,
    onClickMembersResults: () -> Unit,
    onClickImage: () -> Unit,
    onClickFullAlbum: () -> Unit,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
    onClickRecommendedStart: (Int) -> Unit,
    comments: @Composable (Modifier) -> Unit,
    donations: @Composable (Modifier) -> Unit
) {
    PullToRefreshScreen(isRefreshing = isLoading, onRefresh = {
        onClickRetry()
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            val windowSize = calculateWindowSizeClass()
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
                            imageUrl = data.image,
                            isPortrait = false
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        StartScreenInnerContent(
                            modifier = Modifier,
                            data = data,
                            starts = starts,
                            onClickRegistration = onClickRegistration,
                            onClickMembers = onClickMembers,
                            onClickRecommendedStart = onClickRecommendedStart,
                            onClickMembersResults = onClickMembersResults,
                            onClickUrl = onClickUrl,
                            onClickPhone = onClickPhone,
                            onClickFullAlbum = onClickFullAlbum,
                            donations = donations,
                            comments = comments
                        )
                    }
                }
            } else {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    FullImageContent(
                        modifier = Modifier.clickable { onClickImage() },
                        imageUrl = data.image
                    )
                    StartScreenInnerContent(
                        modifier = it,
                        data = data,
                        starts = starts,
                        onClickRegistration = onClickRegistration,
                        onClickMembers = onClickMembers,
                        onClickMembersResults = onClickMembersResults,
                        onClickRecommendedStart = onClickRecommendedStart,
                        onClickUrl = onClickUrl,
                        onClickPhone = onClickPhone,
                        onClickFullAlbum = onClickFullAlbum,
                        donations = donations,
                        comments = comments
                    )
                }
            }
            BackFloatingActionButton {
                onClickBack()
            }
        }
    }
}
