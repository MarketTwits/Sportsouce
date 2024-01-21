package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.core_ui.components.BackFloatingActionButton
import com.markettwits.core_ui.components.FullImageContent
import com.markettwits.core_ui.refresh.PullToRefreshScreen
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.component.StartConditionPanel
import com.markettwits.start.presentation.start.component.StartDescription
import com.markettwits.start.presentation.start.component.StartDistances
import com.markettwits.start.presentation.start.component.StartMembersPanel
import com.markettwits.start.presentation.start.component.StartOrganizers
import com.markettwits.start.presentation.start.component.StartResult
import com.markettwits.start.presentation.start.component.StartStatus
import com.markettwits.start.presentation.start.component.StartTitle

@Composable
fun StartScreenContent(
    data: StartItem,
    isLoading : Boolean,
    onClickRetry : () -> Unit,
    onClickBack : () -> Unit,
    onClickDistance : (DistanceInfo) -> Unit,
    onClickMembers : ( List<StartMembersUi>) -> Unit,
    comments : @Composable (Modifier) -> Unit
) {
    PullToRefreshScreen(isRefreshing = isLoading, onRefresh = {
        onClickRetry()
    }) {
        Box(
            modifier = it
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                FullImageContent(imageUrl = data.image)
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
                    StartDistances(
                        modifier = modifier,
                        distance = data.distanceInfo,
                        startStatus = data.startStatus,
                        onClick = {
                            onClickDistance(it)
                        }
                    )
                    StartOrganizers(modifier = modifier, organizer = data.organizers)
                    StartResult(
                        modifier = modifier,
                        results = data.result,
                        title = "Результаты"
                    )
                    StartResult(
                        modifier = modifier,
                        results = data.usefulLinks,
                        title = "Полезные ссылки"
                    )
                    StartConditionPanel(modifier = modifier, file = data.conditionFile)
                    StartMembersPanel(
                        modifier = modifier,
                        membersCount = data.membersUi.size
                    ) {
                        onClickMembers(data.membersUi)
                    }
                }
                comments(modifier)
            }
            BackFloatingActionButton {
                onClickBack()
            }
        }
    }
}