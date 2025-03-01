package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.start.components.*

@Composable
internal fun StartScreenInnerContent(
    modifier: Modifier,
    data: StartItem,
    onClickRegistration: () -> Unit,
    onClickMembers: (List<StartMembersUi>) -> Unit,
    onClickMembersResults: () -> Unit,
    onClickFullAlbum: () -> Unit,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
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
            onClickUrl = onClickUrl
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
        donations(innerModifier)
    }
    comments(modifier)
}
