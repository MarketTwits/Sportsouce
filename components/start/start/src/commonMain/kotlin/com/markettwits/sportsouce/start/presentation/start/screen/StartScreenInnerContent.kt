package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.start.domain.StartRefundRules
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.start.components.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartScreenInnerContent(
    modifier: Modifier,
    data: StartItem,
    error: Throwable? = null,
    recommendedStarts: List<StartsListItem>,
    seriesStarts: List<StartsListItem>,
    archiveStarts: List<StartsListItem> = emptyList(),
    archiveLoading: Boolean = false,
    archiveError: Boolean = false,
    refundRules: StartRefundRules? = null,
    refundRulesLoading: Boolean = false,
    refundRulesError: Boolean = false,
    onRetryRefundRules: () -> Unit = {},
    onClickArchive: () -> Unit = {},
    onRetryArchive: () -> Unit = {},
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
    val innerModifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

    Column(modifier = modifier) {
        Column(
            modifier = Modifier.padding(vertical = SauceSpacing.small),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.small),
        ) {
            StartTitle(
                modifier = Modifier.padding(horizontal = SauceSpacing.large),
                title = data.title,
            )
            StartStatus(
                organizers = data.organizers,
                kindOfSports = data.kindOfSports,
                startStatus = data.startStatus
            )
            StartExtraFieldsPanel(
                modifier = Modifier.padding(horizontal = SauceSpacing.large),
                place = data.startPlace,
                organizers = data.organizers,
                startDate = data.startTime,
                isPartialData = isPartialData
            )
        }
        if (error != null) {
            StartDetailsLoadError(innerModifier, isPartialData = isPartialData, hasData = true, onClickRetry = onClickRetry)
        }
        StartEntryActions(
            modifier = innerModifier,
            membersCount = if (isPartialData) 0 else data.startMembersUi.size,
            resultsCount = data.membersResults.size,
            onClickMembers = { onClickMembers(data.startMembersUi) },
            onClickResults = onClickMembersResults,
        )
        if (!isPartialData) {
            StartDistanceInfo(innerModifier, data.distanceInfoNew, data.startMembersUi)
            StartConditionGrid(innerModifier, data.conditionDetails, data.conditionFile, onClickUrl)
        }
        if (data.description.isNotBlank()) {
            Column(innerModifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                StartSectionHeading("О старте")
                StartDescription(Modifier, data.description)
            }
        }
        if (!isPartialData) {
            StartRefundRulesPanel(innerModifier, refundRules, refundRulesLoading, refundRulesError, onRetryRefundRules)
            StartAllFiles(innerModifier, data.result, data.usefulLinks, data.conditionFile, onClickUrl)
            StartOrganizers(innerModifier, data.organizers, onClickUrl, onClickPhone)
            StartReviewPanel(innerModifier, data.reviewState)
            StartAlbums(Modifier.padding(vertical = SauceSpacing.small), data.startAlbum, onClickFullAlbum)
            StartSeriesPanel(innerModifier, seriesStarts, data.id, onClickRelatedStarts, onClickRecommendedStart)
            if (data.startArchive != null) StartArchivePanel(innerModifier, archiveStarts, data.id,
                archiveLoading, archiveError, onClickArchive, onRetryArchive, onClickRecommendedStart)
        }
        StartsRecommendationPanel(
            modifier = Modifier.padding(vertical = 8.dp),
            items = recommendedStarts,
            onItemClick = onClickRecommendedStart
        )
        donations(innerModifier)
        StartInsurancePanel(innerModifier, onClickUrl)
        Spacer(modifier = Modifier.height(16.dp))
        comments(Modifier)
        Spacer(modifier = Modifier.height(16.dp))
    }
}
