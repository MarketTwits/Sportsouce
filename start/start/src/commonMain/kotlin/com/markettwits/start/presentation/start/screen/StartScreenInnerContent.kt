package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.start.components.StartAlbums
import com.markettwits.start.presentation.start.components.StartConditionPanel
import com.markettwits.start.presentation.start.components.StartDescription
import com.markettwits.start.presentation.start.components.StartDistances
import com.markettwits.start.presentation.start.components.StartDistancesNew
import com.markettwits.start.presentation.start.components.StartMembersPanel
import com.markettwits.start.presentation.start.components.StartMembersStatistics
import com.markettwits.start.presentation.start.components.StartOrganizers
import com.markettwits.start.presentation.start.components.StartResult
import com.markettwits.start.presentation.start.components.StartStatus
import com.markettwits.start.presentation.start.components.StartTimeProgram
import com.markettwits.start.presentation.start.components.StartTitle
import com.markettwits.start_cloud.model.start.fields.DistinctDistance
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun StartScreenInnerContent(
    modifier: Modifier,
    data: StartItem,
    onClickDistance: (DistanceItem, Boolean, String) -> Unit,
    onClickDistanceNew: (DistinctDistance) -> Unit,
    onClickMembers: (List<StartMembersUi>) -> Unit,
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
            status = data.startStatus, date = data.startTime
        )
        StartDescription(modifier = innerModifier, description = data.description)
        StartTimeProgram(
            modifier = innerModifier,
            startTimes = data.startTimes
        )
        StartAlbums(modifier = innerModifier, albums = data.startAlbum, onCLickFullAlbum = {
            onClickFullAlbum()
        })
        //New distance
        StartDistancesNew(
            modifier = innerModifier,
            distance = data.distanceInfoNew,
            mapDistance = data.distanceMapNew,
            startStatus = data.startStatus,
            paymentDisabled = data.paymentDisabled,
            paymentType = data.paymentType,
            regLink = data.regLink,
            onClick = {
                onClickDistanceNew(it)
            },
            onClickUrl = onClickUrl
        )
        //Old distances
        StartDistances(
            modifier = innerModifier,
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
        StartMembersStatistics(
            modifier = innerModifier,
            membersUi = data.membersUi.toImmutableList()
        )
        StartOrganizers(
            modifier = innerModifier,
            organizer = data.organizers,
            onClickUrl = onClickUrl,
            onClickPhone = onClickPhone
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
        donations(innerModifier)
    }
    comments(modifier)
}
