package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.composable.SauceErrorSimpleContent
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.start.components.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.delay

@Composable
internal fun StartScreenInnerContent(
    modifier: Modifier,
    data: StartItem,
    error: Throwable? = null,
    recommendedStarts: List<StartsListItem>,
    seriesStarts: List<StartsListItem>,
    isPartialData: Boolean = false,
    onClickMembers: (List<StartMembersUi>) -> Unit,
    onClickMembersResults: () -> Unit,
    onClickFullAlbum: (StartItem.Album) -> Unit,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
    onClickRetry: () -> Unit,
    onClickRelatedStarts: () -> Unit,
    onClickRecommendedStart: (StartsListItem) -> Unit,
    comments: @Composable (Modifier) -> Unit,
    donations: @Composable (Modifier) -> Unit,
) {
    val innerModifier = Modifier.padding(10.dp)

    // Animation states for staggered appearance
    var showConditionGrid by rememberSaveable { mutableStateOf(false) }
    var showDistanceInfo by rememberSaveable { mutableStateOf(false) }
    var showAlbums by rememberSaveable { mutableStateOf(false) }
    var showAllFiles by rememberSaveable { mutableStateOf(false) }
    var showOrganizers by rememberSaveable { mutableStateOf(false) }
    var showStartSeries by rememberSaveable { mutableStateOf(false) }

    var showConditionPanel by rememberSaveable { mutableStateOf(false) }
    var showReviewPanel by rememberSaveable { mutableStateOf(false) }
    var showMembersPanel by rememberSaveable { mutableStateOf(false) }

    var hasAnimated by rememberSaveable { mutableStateOf(false) }

    var previousPartialState by rememberSaveable { mutableStateOf(isPartialData) }

    // Staggered animation timing - trigger when transitioning from partial to full data
    LaunchedEffect(isPartialData, data.id) {
        // Only trigger animations if we haven't animated before and under specific conditions
        if (!hasAnimated) {
            if (previousPartialState && !isPartialData) {
                // Wait for StartExtraFieldsPanel to complete its internal animations (~550ms)
                delay(600) // Allow time for StartExtraFieldsPanel animations to complete
                showConditionGrid = true
                delay(120)
                showReviewPanel = true
                delay(120)
                showDistanceInfo = true
                delay(120)
                showAlbums = true
                delay(120)
                showAllFiles = true
                delay(120)
                showOrganizers = true
                delay(120)
                showStartSeries = true
                delay(120)
                delay(120)
                showAllFiles = true
                delay(120)
                showMembersPanel = true
                hasAnimated = true
            } else if (!previousPartialState && !isPartialData) {
                // If not partial data from the start (direct load), show with staggered timing
                delay(600) // Account for StartExtraFieldsPanel timing even on direct load
                showConditionGrid = true
                delay(120)
                showReviewPanel = true
                delay(120)
                showDistanceInfo = true
                delay(120)
                showAlbums = true
                delay(120)
                showAllFiles = true
                delay(120)
                showOrganizers = true
                delay(120)
                showStartSeries = true
                delay(120)
                delay(1200)
                showAllFiles = true
                delay(120)
                showMembersPanel = true
                hasAnimated = true
            }
        }
        previousPartialState = isPartialData
    }

    Column(modifier = modifier) {
        StartTopCard {
            StartTitle(
                modifier = innerModifier,
                title = data.title,
            )
            StartStatus(
                organizers = data.organizers,
                kindOfSports = data.kindOfSports,
                startStatus = data.startStatus
            )
            StartExtraFieldsPanel(
                modifier = innerModifier,
                place = data.startPlace,
                organizers = data.organizers,
                startDate = data.startTime,
                isPartialData = isPartialData
            )
            StartDescription(
                modifier = innerModifier,
                description = data.description,
                isPartialData = isPartialData
            )
        }
        AnimatedVisibility(
            visible = showConditionGrid,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartConditionGrid(
                modifier = innerModifier,
                conditionItems = data.conditionDetails,
                conditionFile = data.conditionFile,
                onClickFile = onClickUrl
            )
        }
        AnimatedVisibility(
            visible = showReviewPanel,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartReviewPanel(
                modifier = innerModifier,
                reviewState = data.reviewState,
            )
        }
        AnimatedVisibility(
            visible = showDistanceInfo,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartDistanceInfo(
                modifier = innerModifier,
                distances = data.distanceInfoNew,
                membersUi = data.startMembersUi
            )
        }
        AnimatedVisibility(
            visible = showAlbums,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartAlbums(modifier = innerModifier, albums = data.startAlbum, onCLickFullAlbum = {
                onClickFullAlbum(it)
            })
        }
        AnimatedVisibility(
            visible = showAllFiles,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartAllFiles(
                modifier = innerModifier,
                results = data.result,
                usefulLinks = data.usefulLinks,
                conditionFile = data.conditionFile,
                onClickFile = { url ->
                    onClickUrl(url)
                }
            )
        }
        AnimatedVisibility(
            visible = showOrganizers,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartOrganizers(
                modifier = innerModifier,
                organizer = data.organizers,
                onClickUrl = onClickUrl,
                onClickPhone = onClickPhone
            )
        }
        AnimatedVisibility(
            visible = showStartSeries,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartSeriesPanel(
                modifier = innerModifier,
                currentStartId = data.id,
                items = seriesStarts,
                onClickRelatedStarts = onClickRelatedStarts,
                onItemClick = onClickRecommendedStart,
            )
        }
        AnimatedVisibility(
            visible = showConditionPanel,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartConditionPanel(
                modifier = innerModifier,
                file = data.conditionFile,
                onClickFile = onClickUrl
            )
        }
        AnimatedVisibility(
            visible = showMembersPanel,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartMembersPanel(
                modifier = innerModifier,
                membersCount = data.startMembersUi.size
            ) {
                onClickMembers(data.startMembersUi)
            }
        }
        StartMembersResultPanel(
            modifier = innerModifier,
            membersResultCount = data.membersResults.size,
            onClick = onClickMembersResults
        )
        StartsRecommendationPanel(
            modifier = innerModifier,
            items = recommendedStarts,
            onItemClick = onClickRecommendedStart
        )
        donations(innerModifier)
        if (isPartialData && error != null) {
            error.mapToSauceError().SauceErrorSimpleContent(
                modifier = innerModifier,
            ) {
                onClickRetry()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
    comments(Modifier)
    if (data.startStatus.code == 3 || data.startStatus.code == 2)
        Spacer(modifier = Modifier.height(77.dp))
}
