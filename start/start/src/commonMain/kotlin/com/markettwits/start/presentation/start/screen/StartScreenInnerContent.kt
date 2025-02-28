package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.start.components.*
import com.markettwits.start.presentation.start.components.StartAlbums
import com.markettwits.start.presentation.start.components.StartConditionPanel
import com.markettwits.start.presentation.start.components.StartDescription
import com.markettwits.start.presentation.start.components.StartMembersPanel
import com.markettwits.start.presentation.start.components.StartMembersStatistics
import com.markettwits.start.presentation.start.components.StartResult
import com.markettwits.start.presentation.start.components.StartStatus
import com.markettwits.start.presentation.start.components.StartTitle

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
    val innerModifier = Modifier.padding(8.dp)

    Column(modifier = modifier) {
        StartTitle(
            modifier = innerModifier,
            title = data.title,
            place = data.startPlace
        )
        StartStatus(
            modifier = innerModifier,
            status = data.startStatus,
            membersCount = data.membersUi.count(),
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
        StartExtraFieldsPanel(
            modifier = innerModifier,
            place = data.startPlace,
            organizers = data.organizers,
            startDate = data.startTime
        )
//        StartTimeProgram(
//            modifier = innerModifier,
//            startTimes = data.startTimes
//        )
        StartAlbums(modifier = innerModifier, albums = data.startAlbum, onCLickFullAlbum = {
            onClickFullAlbum()
        })
//        StartDistances(
//            modifier = innerModifier,
//            distance = data.distanceInfoNew,
//            mapDistance = data.distanceMapNew,
//            startStatus = data.startStatus,
//            paymentDisabled = data.paymentDisabled,
//            paymentType = data.paymentType,
//            regLink = data.regLink,
//            onClick = {
//                onClickDistanceNew(it)
//            },
//            onClickUrl = onClickUrl
//        )
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
//        StartOrganizers(
//            modifier = innerModifier,
//            organizer = data.organizers,
//            onClickUrl = onClickUrl,
//            onClickPhone = onClickPhone
//        )
        donations(innerModifier)
    }
    comments(modifier)
}
