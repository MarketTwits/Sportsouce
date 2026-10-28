package com.markettwits.sportsouce.club.dashboard.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.navigation.LocalScrollContentBottomPadding
import com.markettwits.core_ui.items.components.topbar.LocalSauceTopBar
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.sportsouce.club.common.presentation.*
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.sportsouce.club.dashboard.presentation.components.*
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType

@Composable
fun ClubDashboardScreen(component: ClubDashboardComponent) {
    val state by component.state.collectAsState()
    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
        CompositionLocalProvider(LocalSauceTopBar provides null,
            LocalScrollContentBottomPadding provides WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()) {
            AdaptivePane(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))) {
                ClubDashboardContent(state, topContentPadding = 12.dp, event = component::obtainEvent)
            }
        }
        BackFloatingActionButton(Modifier.align(Alignment.TopStart)) {
            component.obtainEvent(ClubDashboardStore.Intent.OnClickBack)
        }
    }
}

@Composable
internal fun ClubDashboardContent(state: ClubDashboardStore.State, topContentPadding: Dp = 20.dp, event: (ClubDashboardStore.Intent) -> Unit) {
    val data = state.bottomSheetData
    val open: (MenuBottomSheetType) -> Unit = { event(ClubDashboardStore.Intent.OpenClubInfoDetail(it, data)) }
    PullToRefreshScreen(isRefreshing = state.isLoading && state.hasLoaded, onRefresh = { event(ClubDashboardStore.Intent.RetryRequest) }) {
        SauceLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(top = topContentPadding, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item(key = "intro") { ClubInsetSection { ClubDashboardHeader(data.mainImage?.imageUrl.orEmpty()) } }
            item(key = "schedule") { ClubInsetSection { SauceButton("Расписание тренировок", { event(ClubDashboardStore.Intent.OnClickSchedule) }, modifier = Modifier.fillMaxWidth()) } }
            if (data.statistics.isNotEmpty()) item(key = "numbers") {
                ClubInsetSection {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        data.statistics.take(2).forEach { stat ->
                            SauceClickTarget(modifier = Modifier.weight(1f), onClick = { open(MenuBottomSheetType.Statistics) }) {
                                Column(Modifier.padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(stat.value, style = MaterialTheme.typography.headlineMedium, color = SauceTheme.colors.textPrimary)
                                    HtmlText(text = stat.title, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted, showImages = false)
                                }
                            }
                        }
                    }
                }
            }
            if (state.error != null) item(key = "error") {
                ClubInsetSection { ClubNotice("Не удалось загрузить информацию о клубе", "Проверьте соединение и повторите попытку.", { event(ClubDashboardStore.Intent.RetryRequest) }) }
            }
            when {
                state.isLoading && !state.hasLoaded -> item(key = "loading") { ClubInsetSection { ClubLoading() } }
                state.hasLoaded -> {
                    item(key = "directions") {
                        ClubCarouselSection("Каждому свой темп", "Направления тренировок", { open(MenuBottomSheetType.Trainings) }) {
                            items(data.trainings, key = { it.id }) { training ->
                                ClubTrainingPreview(training) { open(MenuBottomSheetType.TrainingDetails(training.id)) }
                            }
                        }
                    }
                    item(key = "subscriptions") { ClubInsetSection { SubscriptionInfoCard(onClick = { event(ClubDashboardStore.Intent.OnClickSubscriptions) }, onTrialClick = { open(MenuBottomSheetType.Trainings) }) } }
                    item(key = "trainers") {
                        ClubCarouselSection("Люди, которые поддержат", "Наши тренеры", { open(MenuBottomSheetType.OurTeam) }) {
                            items(data.trainers, key = { it.id }) { trainer ->
                                ClubTrainerPreview(trainer) { open(MenuBottomSheetType.TrainerDetails(trainer.id)) }
                            }
                        }
                    }
                    item(key = "menu") { ClubInsetSection { ClubMenuGrid({ open(MenuBottomSheetType.Plan) }, { open(MenuBottomSheetType.ClubBonuses) }) } }
                    item(key = "more") { ClubInsetSection { ClubMoreSection({ open(MenuBottomSheetType.Faq) }, { open(MenuBottomSheetType.Statistics) }) } }
                }
            }
        }
    }
}

@Composable
private fun ClubInsetSection(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) { content() }
}

@Composable
private fun ClubCarouselSection(eyebrow: String, title: String, onClick: () -> Unit, content: LazyListScope.() -> Unit) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ClubInsetSection { ClubSectionTitle(eyebrow, title, onClick) }
        LazyRow(Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp), content = content)
    }
}

@Composable
private fun ClubSectionTitle(eyebrow: String, title: String, onClick: () -> Unit) {
    Column {
        Text(eyebrow, style = MaterialTheme.typography.labelMedium, color = SauceTheme.colors.textMuted)
        SauceClickTarget(modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), onClick = onClick) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(title, Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall, color = SauceTheme.colors.textPrimary)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(20.dp), tint = SauceTheme.colors.action)
            }
        }
    }
}
