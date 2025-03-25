package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.start.components.StartAlbums
import com.markettwits.sportsouce.start.presentation.start.components.StartConditionPanel
import com.markettwits.sportsouce.start.presentation.start.components.StartDescription
import com.markettwits.sportsouce.start.presentation.start.components.StartExtraFieldsPanel
import com.markettwits.sportsouce.start.presentation.start.components.StartMembersPanel
import com.markettwits.sportsouce.start.presentation.start.components.StartMembersResultPanel
import com.markettwits.sportsouce.start.presentation.start.components.StartMembersStatistics
import com.markettwits.sportsouce.start.presentation.start.components.StartRegistrationPanel
import com.markettwits.sportsouce.start.presentation.start.components.StartResult
import com.markettwits.sportsouce.start.presentation.start.components.StartTitle
import com.markettwits.sportsouce.start.presentation.start.components.StartsRecommendationPanel
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StartScreenInnerContent(
    modifier: Modifier,
    data: StartItem,
    starts: List<StartsListItem>,
    onClickRegistration: () -> Unit,
    onClickMembers: (List<StartMembersUi>) -> Unit,
    onClickMembersResults: () -> Unit,
    onClickFullAlbum: () -> Unit,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
    onClickRecommendedStart: (Int) -> Unit,
    comments: @Composable (Modifier) -> Unit,
    donations: @Composable (Modifier) -> Unit
) {
    val innerModifier = Modifier.padding(10.dp)

    Column(modifier = modifier) {
        StartTitle(
            modifier = innerModifier,
            title = data.title,
            place = data.startPlace
        )
        StartExtraFieldsPanel(
            modifier = innerModifier,
            place = data.startPlace,
            organizers = data.organizers,
            startDate = data.startTime
        )
        StartRegistrationPanel(
            modifier = innerModifier,
            distance = data.distanceInfoNew,
            startStatus = data.startStatus,
            regLink = data.regLink,
            onClickRegistration = {
                onClickRegistration()
            },
        )
        StartDescription(modifier = innerModifier, description = data.description)
        StartAlbums(modifier = innerModifier, albums = data.startAlbum, onCLickFullAlbum = {
            onClickFullAlbum()
        })
        StartMembersStatistics(
            modifier = innerModifier,
            membersUi = data.membersUi
        )
        StartResult(
            modifier = innerModifier,
            results = data.result,
            title = "Результаты",
            onClickResult = {
                onClickUrl(it)
            }
        )
        StartResult(
            modifier = innerModifier,
            results = data.usefulLinks,
            title = "Полезные ссылки",
            onClickResult = {
                onClickUrl(it)
            }
        )
        StartConditionPanel(
            modifier = innerModifier,
            file = data.conditionFile,
            onClickFile = onClickUrl
        )
        StartMembersPanel(
            modifier = innerModifier,
            membersCount = data.membersUi.size
        ) {
            onClickMembers(data.membersUi)
        }
        StartMembersResultPanel(
            modifier = innerModifier,
            membersResultCount = data.membersResults.size,
            onClick = onClickMembersResults
        )
        StartsRecommendationPanel(
            modifier = innerModifier,
            items = starts,
            onItemClick = onClickRecommendedStart
        )
        donations(innerModifier)
    }
    comments(modifier)
}

@Composable
@Preview
private fun prev(){
    Box(Modifier.fillMaxSize()) {}
}
