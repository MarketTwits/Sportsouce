package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.core_ui.items.base_screen.PullToRefreshScreen
import com.markettwits.core_ui.items.components.FullImageContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.start.components.StartAlbums
import com.markettwits.start.presentation.start.components.StartConditionPanel
import com.markettwits.start.presentation.start.components.StartDescription
import com.markettwits.start.presentation.start.components.StartDistances
import com.markettwits.start.presentation.start.components.StartMembersPanel
import com.markettwits.start.presentation.start.components.StartOrganizers
import com.markettwits.start.presentation.start.components.StartResult
import com.markettwits.start.presentation.start.components.StartStatus
import com.markettwits.start.presentation.start.components.StartTitle

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
            modifier = it
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                FullImageContent(
                    modifier = Modifier.clickable { onClickImage() },
                    imageUrl = data.image
                )
                val modifier = Modifier.padding(5.dp)
                Column(modifier = modifier) {
                    StartTitle(
                        modifier = modifier,
                        title = data.title,
                        place = data.startPlace
                    )
                    StartStatus(
                        modifier = modifier,
                        status = data.startStatus, date = data.startTime
                    )
                    StartDescription(modifier = modifier, description = data.description)
                    StartAlbums(modifier = modifier, albums = data.startAlbum, onCLickFullAlbum = {
                        onClickFullAlbum()
                    })
                    StartDistances(
                        modifier = modifier,
                        distance = data.distanceInfo,
                        startStatus = data.startStatus,
                        paymentDisabled = data.paymentDisabled,
                        paymentType = data.paymentType,
                        regLink = data.regLink,
                        onClick = { distance, paymentDisabled, paymentType ->
                            onClickDistance(distance, paymentDisabled, paymentType)
                        },
                        onClickUrl = onClickUrl
                    )
                    StartOrganizers(
                        modifier = modifier,
                        organizer = data.organizers,
                        onClickUrl = onClickUrl,
                        onClickPhone = onClickPhone
                    )
                    StartResult(
                        modifier = modifier,
                        results = data.result,
                        title = "Результаты",
                        onClickResult = {
                            onClickUrl(it)
                        }
                    )
                    StartResult(
                        modifier = modifier,
                        results = data.usefulLinks,
                        title = "Полезные ссылки",
                        onClickResult = {
                            onClickUrl(it)
                        }
                    )
                    StartConditionPanel(
                        modifier = modifier,
                        file = data.conditionFile,
                        onClickFile = onClickUrl
                    )
                    StartMembersPanel(
                        modifier = modifier,
                        membersCount = data.membersUi.size
                    ) {
                        onClickMembers(data.membersUi)
                    }
                    donations(modifier)
                }
                comments(modifier)
            }
            BackFloatingActionButton {
                onClickBack()
            }
        }
    }
}