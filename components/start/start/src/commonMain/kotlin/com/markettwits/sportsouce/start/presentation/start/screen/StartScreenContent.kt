package com.markettwits.sportsouce.start.presentation.start.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import com.markettwits.core_ui.items.components.buttons.sauceFloatingActionTopPadding
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.screens.FullImageContent
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.domain.StartRefundRules
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.start.component.StartFavoriteState
import com.markettwits.sportsouce.start.presentation.start.components.*
import com.markettwits.sportsouce.start.support.domain.StartDonationCheck
import com.markettwits.sportsouce.start.support.presentation.components.StartRegistrationNotice
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlin.time.Clock

@Composable
internal fun StartScreenContent(
    data: StartItem,
    favoriteState: StartFavoriteState,
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
    error: Throwable? = null,
    isLoading: Boolean,
    isPartialData: Boolean = false,
    onClickRetry: () -> Unit,
    onClickBack: () -> Unit,
    onClickRegistration: () -> Unit,
    onClickMembers: (List<StartMembersUi>) -> Unit,
    onClickMembersResults: () -> Unit,
    onClickImage: () -> Unit,
    onClickFullAlbum: (StartItem.Album) -> Unit,
    onClickUrl: (String) -> Unit,
    onClickPhone: (String) -> Unit,
    onClickShare: () -> Unit,
    onClickRelatedStarts: () -> Unit,
    onClickRecommendedStart: (StartsListItem) -> Unit,
    onClickAddToFavourites: () -> Unit,
    comments: @Composable (Modifier) -> Unit,
    donations: @Composable (Modifier) -> Unit,
    navigationOverlap: Dp = 0.dp,
    registrationCheck: StartDonationCheck? = null,
) {
    key(data.id) {
        PullToRefreshScreen(isRefreshing = isLoading, onRefresh = onClickRetry) {
            BoxWithConstraints(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                val wide = maxWidth >= 840.dp && maxHeight >= 600.dp
                val scrollState = rememberScrollState()
                val coverBackdrop = rememberHazeState()
                var coverHeight by remember { mutableIntStateOf(0) }
                val density = LocalDensity.current
                val hasRegistration = data.startStatus.code == 3
                val registration = registrationCheck?.takeIf { it.hasRegistration }
                val topControlsHeight = with(density) { (sauceFloatingActionTopPadding() + SauceSizes.touchTarget).toPx() }
                val minimumRegistrationSpace = SauceSizes.touchTarget + SauceSpacing.small * 2
                var registrationHeight by remember(hasRegistration) { mutableIntStateOf(0) }
                var inlineBounds by remember(hasRegistration) { mutableStateOf<Rect?>(null) }
                var dockBounds by remember(hasRegistration) { mutableStateOf<Rect?>(null) }
                val showFloatingRegistration by remember(topControlsHeight, hasRegistration) {
                    derivedStateOf(structuralEqualityPolicy()) {
                        val inline = inlineBounds
                        val dock = dockBounds
                        hasRegistration && inline != null && dock != null &&
                            (inline.bottom <= dock.top + topControlsHeight || inline.top >= dock.bottom)
                    }
                }
                Row(Modifier.fillMaxSize()) {
                    if (wide) {
                        Box(Modifier.weight(0.4f).fillMaxHeight()
                            .semantics { contentDescription = "Открыть афишу старта" }
                            .clickable(role = Role.Button, onClick = onClickImage)) {
                            FullImageContent(
                                modifier = Modifier.fillMaxSize(),
                                backgroundColor = SauceTheme.colors.background,
                                imageUrl = data.image,
                                isPortrait = false,
                            )
                        }
                    }
                    Box(Modifier.weight(if (wide) 0.6f else 1f).fillMaxHeight()) {
                        Column(Modifier.fillMaxSize().verticalScroll(scrollState)) {
                            if (!wide) {
                                Box(Modifier.onSizeChanged { coverHeight = it.height }
                                    .hazeSource(coverBackdrop)
                                    .semantics { contentDescription = "Открыть афишу старта" }
                                    .clickable(role = Role.Button, onClick = onClickImage)) {
                                    FullImageContent(
                                        backgroundColor = SauceTheme.colors.background,
                                        imageUrl = data.image,
                                        backdropScrollOffset = { scrollState.value },
                                    )
                                }
                            }
                            StartScreenInnerContent(
                                modifier = Modifier,
                                data = data,
                                error = error,
                                recommendedStarts = recommendedStarts,
                                seriesStarts = seriesStarts,
                                archiveStarts = archiveStarts,
                                archiveLoading = archiveLoading,
                                archiveError = archiveError,
                                refundRules = refundRules,
                                refundRulesLoading = refundRulesLoading,
                                refundRulesError = refundRulesError,
                                onRetryRefundRules = onRetryRefundRules,
                                onClickArchive = onClickArchive,
                                onRetryArchive = onRetryArchive,
                                isPartialData = isPartialData,
                                onClickMembers = onClickMembers,
                                onClickRecommendedStart = onClickRecommendedStart,
                                onClickMembersResults = onClickMembersResults,
                                onClickUrl = onClickUrl,
                                onClickPhone = onClickPhone,
                                onClickFullAlbum = onClickFullAlbum,
                                onClickRetry = onClickRetry,
                                onClickRelatedStarts = onClickRelatedStarts,
                                donations = donations,
                                comments = comments,
                                registration = {
                                    if (hasRegistration || registration != null) {
                                        Surface(
                                            modifier = Modifier.fillMaxWidth().padding(horizontal = SauceSpacing.large),
                                            shape = SauceShapes.card,
                                            color = SauceTheme.colors.surfaceRaised,
                                            border = BorderStroke(1.dp, SauceTheme.colors.border),
                                        ) {
                                            Column {
                                                if (hasRegistration) {
                                                    StartRegistrationButton(
                                                        modifier = Modifier.padding(
                                                            horizontal = SauceSpacing.medium,
                                                            vertical = SauceSpacing.small,
                                                        ).onGloballyPositioned { coordinates ->
                                                            inlineBounds = Rect(coordinates.positionInRoot(), coordinates.size.toSize())
                                                        }.graphicsLayer { alpha = if (showFloatingRegistration) 0f else 1f },
                                                        startStatus = data.startStatus,
                                                        isLoading = isLoading,
                                                        isPartialData = isPartialData,
                                                        hasError = error != null,
                                                        isInteractive = !showFloatingRegistration,
                                                        onClickRegistration = onClickRegistration,
                                                    )
                                                }
                                                if (registration != null) {
                                                    StartRegistrationNotice(
                                                        check = registration,
                                                        isUpcoming = data.startTimes.startsAt?.let { it > Clock.System.now() } == true,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                },
                            )
                            Spacer(Modifier.navigationBarsPadding().height(
                                if (hasRegistration) with(density) { registrationHeight.toDp() }
                                    .coerceAtLeast(minimumRegistrationSpace) else 0.dp,
                            ))
                        }
                        if (hasRegistration) {
                            Box(
                                Modifier.matchParentSize()
                                    .padding(bottom = navigationOverlap)
                                    .navigationBarsPadding()
                                    .onGloballyPositioned { coordinates ->
                                        dockBounds = Rect(coordinates.positionInRoot(), coordinates.size.toSize())
                                    },
                                contentAlignment = Alignment.BottomCenter,
                            ) {
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = showFloatingRegistration,
                                    enter = fadeIn(tween(200)) + slideInVertically(tween(260, easing = FastOutSlowInEasing)) { it / 3 },
                                    exit = fadeOut(tween(130)) + slideOutVertically(tween(160)) { it / 4 },
                                ) {
                                    Box(Modifier.onSizeChanged { registrationHeight = it.height }
                                        .padding(horizontal = SauceSpacing.extraLarge, vertical = SauceSpacing.small)) {
                                        StartRegistrationButton(
                                            modifier = Modifier.widthIn(max = 288.dp).shadow(4.dp, SauceShapes.button),
                                            startStatus = data.startStatus,
                                            isLoading = isLoading,
                                            isPartialData = isPartialData,
                                            hasError = error != null,
                                            isInteractive = showFloatingRegistration,
                                            onClickRegistration = onClickRegistration,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                StartTopActions(
                    modifier = Modifier.align(Alignment.TopCenter),
                    backdrop = coverBackdrop,
                    coverBottom = { if (wide) 0 else coverHeight - scrollState.value },
                    favoriteState = favoriteState,
                    onBack = onClickBack,
                    onShare = onClickShare,
                    onFavorite = onClickAddToFavourites,
                )
            }
        }
    }
}
