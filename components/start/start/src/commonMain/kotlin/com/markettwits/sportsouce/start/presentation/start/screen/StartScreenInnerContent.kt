package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
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
    starts: List<StartsListItem>,
    isPartialData: Boolean = false,
    onClickRegistration: () -> Unit,
    onClickMembers: (List<StartMembersUi>) -> Unit,
    onClickMembersResults: () -> Unit,
    onClickFullAlbum: () -> Unit,
    onClickUrl: (String) -> Unit,
    onClickRetry: () -> Unit,
    onClickRecommendedStart: (StartsListItem) -> Unit,
    comments: @Composable (Modifier) -> Unit,
    donations: @Composable (Modifier) -> Unit,
) {
    val innerModifier = Modifier.padding(10.dp)

    // Animation states for staggered appearance
    var showRegistrationPanel by rememberSaveable { mutableStateOf(false) }
    var showAlbums by rememberSaveable { mutableStateOf(false) }
    var showMembersStatistics by rememberSaveable { mutableStateOf(false) }
    var showResults by rememberSaveable { mutableStateOf(false) }
    var showUsefulLinks by rememberSaveable { mutableStateOf(false) }
    var showConditionPanel by rememberSaveable { mutableStateOf(false) }
    var showMembersPanel by rememberSaveable { mutableStateOf(false) }

    // Track animation state to ensure animations only play once
    var hasAnimated by rememberSaveable { mutableStateOf(false) }

    // Track previous partial state to detect transition from partial to full data
    var previousPartialState by rememberSaveable { mutableStateOf(isPartialData) }

    // Staggered animation timing - trigger when transitioning from partial to full data
    LaunchedEffect(isPartialData, data.id) {
        // Only trigger animations if we haven't animated before and under specific conditions
        if (!hasAnimated) {
            if (previousPartialState && !isPartialData) {
                // Wait for StartExtraFieldsPanel to complete its internal animations (~550ms)
                delay(600) // Allow time for StartExtraFieldsPanel animations to complete
                showRegistrationPanel = true
                delay(120)
                showAlbums = true
                delay(120)
                showMembersStatistics = true
                delay(120)
                showResults = true
                delay(120)
                showUsefulLinks = true
                delay(120)
                showConditionPanel = true
                delay(120)
                showMembersPanel = true
                hasAnimated = true
            } else if (!previousPartialState && !isPartialData) {
                // If not partial data from the start (direct load), show with staggered timing
                delay(600) // Account for StartExtraFieldsPanel timing even on direct load
                showRegistrationPanel = true
                delay(120)
                showAlbums = true
                delay(120)
                showMembersStatistics = true
                delay(120)
                showResults = true
                delay(120)
                showUsefulLinks = true
                delay(120)
                showConditionPanel = true
                delay(120)
                showMembersPanel = true
                hasAnimated = true
            }
        }
        previousPartialState = isPartialData
    }

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
            startDate = data.startTime,
            isPartialData = isPartialData
        )
        AnimatedVisibility(
            visible = showRegistrationPanel,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartRegistrationPanel(
                modifier = innerModifier,
                distance = data.distanceInfoNew,
                startStatus = data.startStatus,
                regLink = data.regLink,
                onClickRegistration = {
                    onClickRegistration()
                },
            )
        }
        StartDescription(modifier = innerModifier, description = data.description, isPartialData = isPartialData)
        AnimatedVisibility(
            visible = showAlbums,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartAlbums(modifier = innerModifier, albums = data.startAlbum, onCLickFullAlbum = {
                onClickFullAlbum()
            })
        }
        AnimatedVisibility(
            visible = showMembersStatistics,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartMembersStatistics(
                modifier = innerModifier,
                membersUi = data.startMembersUi
            )
        }
        AnimatedVisibility(
            visible = showResults,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartResult(
                modifier = innerModifier,
                results = data.result,
                title = "Результаты",
                onClickResult = {
                    onClickUrl(it)
                }
            )
        }
        AnimatedVisibility(
            visible = showUsefulLinks,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            StartResult(
                modifier = innerModifier,
                results = data.usefulLinks,
                title = "Полезные ссылки",
                onClickResult = {
                    onClickUrl(it)
                }
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
            items = starts,
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
    }
    comments(modifier)
}