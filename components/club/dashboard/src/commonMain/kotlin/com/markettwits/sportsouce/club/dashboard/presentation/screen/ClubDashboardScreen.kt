package com.markettwits.sportsouce.club.dashboard.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.components.buttons.rememberSauceFloatingActionGlass
import com.markettwits.core_ui.items.components.navigation.LocalScrollContentBottomPadding
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.topbar.LocalSauceTopBar
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.ClubLoading
import com.markettwits.sportsouce.club.common.presentation.ClubNotice
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.sportsouce.club.dashboard.presentation.components.*
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

private val ClubGutter = 20.dp
private const val IntroKey = "intro"

@Composable
fun ClubDashboardScreen(component: ClubDashboardComponent) {
    val state by component.state.collectAsState()
    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
        CompositionLocalProvider(
            LocalSauceTopBar provides null,
            LocalScrollContentBottomPadding provides WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding(),
        ) {
            val topInset = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
            AdaptivePane(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))) {
                ClubDashboardContent(
                    state = state,
                    topContentPadding = topInset + 56.dp,
                    statusBarInset = topInset,
                    onBack = { component.obtainEvent(ClubDashboardStore.Intent.OnClickBack) },
                    event = component::obtainEvent,
                )
            }
        }
    }
}

@Composable
internal fun ClubDashboardContent(
    state: ClubDashboardStore.State,
    topContentPadding: Dp = 20.dp,
    onBack: (() -> Unit)? = null,
    statusBarInset: Dp = 0.dp,
    event: (ClubDashboardStore.Intent) -> Unit,
) {
    val data = state.bottomSheetData
    val listState = rememberLazyListState()
    val heroBackdrop = rememberHazeState()
    val parallaxDistance = with(LocalDensity.current) { 264.dp.toPx() }
    val statusBarFadeDistance = with(LocalDensity.current) { 48.dp.toPx() }
    val open: (MenuBottomSheetType) -> Unit = { event(ClubDashboardStore.Intent.OpenClubInfoDetail(it, data)) }
    val retry = { event(ClubDashboardStore.Intent.RetryRequest) }
    Box(Modifier.fillMaxSize()) {
        PullToRefreshScreen(isRefreshing = state.isLoading && state.hasLoaded, onRefresh = retry) {
            SauceLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp),
            ) {
                item(key = IntroKey) {
                    ClubDashboardHeader(
                        modifier = if (onBack != null) Modifier.hazeSource(heroBackdrop) else Modifier,
                        imageUrl = data.mainImage?.imageUrl.orEmpty(),
                        statistics = data.statistics,
                        topPadding = topContentPadding,
                        scrollFraction = {
                            if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset / parallaxDistance else 1f
                        },
                        onSchedule = { event(ClubDashboardStore.Intent.OnClickSchedule) },
                        onStatistics = { open(MenuBottomSheetType.Statistics) },
                    )
                }
                if (state.error != null) item(key = "error") {
                    ClubInsetSection {
                        ClubNotice("Не удалось загрузить информацию о клубе", "Проверьте соединение и повторите попытку.", retry)
                    }
                }
                when {
                    state.isLoading && !state.hasLoaded -> item(key = "loading") {
                        ClubInsetSection { ClubLoading() }
                    }
                    state.hasLoaded -> {
                        item(key = "directions") {
                            ClubCarouselSection("Направления тренировок", { open(MenuBottomSheetType.Trainings) }) {
                                items(data.trainings, key = { it.id }) { training ->
                                    ClubTrainingPreview(training) { open(MenuBottomSheetType.TrainingDetails(training.id)) }
                                }
                            }
                        }
                        item(key = "subscriptions") {
                            ClubInsetSection {
                                SubscriptionInfoCard(
                                    onClick = { event(ClubDashboardStore.Intent.OnClickSubscriptions) },
                                    onTrialClick = { open(MenuBottomSheetType.Trainings) },
                                )
                            }
                        }
                        item(key = "trainers") {
                            ClubCarouselSection("Наши тренеры", { open(MenuBottomSheetType.OurTeam) }) {
                                items(data.trainers, key = { it.id }) { trainer ->
                                    ClubTrainerPreview(trainer) { open(MenuBottomSheetType.TrainerDetails(trainer.id)) }
                                }
                            }
                        }
                        item(key = "about") {
                            ClubInsetSection {
                                ClubAboutSection(
                                    onPlanClick = { open(MenuBottomSheetType.Plan) },
                                    onClubBonusesClick = { open(MenuBottomSheetType.ClubBonuses) },
                                    onFaqClick = { open(MenuBottomSheetType.Faq) },
                                    questionCount = data.questions.size,
                                    onStatisticsClick = if (data.statistics.isEmpty()) {
                                        { open(MenuBottomSheetType.Statistics) }
                                    } else null,
                                )
                            }
                        }
                    }
                }
            }
        }
        if (statusBarInset > 0.dp) {
            Spacer(
                Modifier.fillMaxWidth().height(statusBarInset)
                    .graphicsLayer {
                        alpha = if (listState.firstVisibleItemIndex > 0) 1f else
                            (listState.firstVisibleItemScrollOffset / statusBarFadeDistance).coerceIn(0f, 1f)
                    }
                    .background(SauceTheme.colors.background),
            )
        }
        if (onBack != null) {
            val glass = rememberSauceFloatingActionGlass(
                backdrop = heroBackdrop,
                tint = SauceTheme.colors.surfaceSubtle.copy(alpha = .54f),
            ) {
                listState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == IntroKey }
                    ?.let { it.offset + it.size } ?: 0
            }
            BackFloatingActionButton(Modifier.align(Alignment.TopStart), glass = glass, back = onBack)
        }
    }
}

@Composable
private fun ClubInsetSection(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(horizontal = ClubGutter)) { content() }
}

@Composable
private fun ClubCarouselSection(title: String, onClick: () -> Unit, content: LazyListScope.() -> Unit) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ClubInsetSection {
            SauceClickTarget(modifier = Modifier.fillMaxWidth(), onClick = onClick, onClickLabel = "Показать все") {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        title,
                        Modifier.weight(1f).semantics { heading() },
                        style = MaterialTheme.typography.headlineSmall,
                        color = SauceTheme.colors.textPrimary,
                    )
                    Box(
                        Modifier.size(36.dp).background(SauceTheme.colors.actionSoft, SauceShapes.field),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(19.dp), tint = SauceTheme.colors.action)
                    }
                }
            }
        }
        LazyRow(
            Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = ClubGutter),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}
