package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.core_ui.items.base_screen.PullToRefreshScreen
import com.markettwits.core_ui.items.components.FullImageContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi

@Composable
fun StartScreenContent(
    data: StartItem,
    isLoading: Boolean,
    onClickRetry: () -> Unit,
    onClickBack: () -> Unit,
    onClickDistance: (DistanceItem, Boolean, String) -> Unit,
    onClickMembers: (List<StartMembersUi>) -> Unit,
    onClickImage: () -> Unit,
    onClickFullAlbum: () -> Unit,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
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
            val window = rememberScreenSizeInfo()
            if (!window.isPortrait()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val imageWith = window.wDP.value / 2.5f
                    Box(
                        modifier = Modifier
                            .width(imageWith.dp)
                            .fillMaxHeight()
                    ) {
                        FullImageContent(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                                .clickable { onClickImage() },
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
                            onClickDistance = onClickDistance,
                            onClickMembers = onClickMembers,
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
                        onClickDistance = onClickDistance,
                        onClickMembers = onClickMembers,
                        onClickUrl = onClickUrl,
                        onClickPhone = onClickPhone,
                        onClickFullAlbum = onClickFullAlbum,
                        donations = donations,
                        comments = comments
                    )
                }
            }
        }
        BackFloatingActionButton {
            onClickBack()
        }
    }
}
