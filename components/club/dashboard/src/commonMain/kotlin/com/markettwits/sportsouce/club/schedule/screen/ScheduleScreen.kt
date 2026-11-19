package com.markettwits.sportsouce.club.schedule.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.components.status.*
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.*
import com.markettwits.sportsouce.club.info.domain.models.Schedule
import com.markettwits.sportsouce.club.schedule.component.ScheduleComponent
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore
import kotlinx.datetime.*
import kotlin.time.Clock

@Composable
fun ScheduleScreen(component: ScheduleComponent) {
    val state by component.state.collectAsState()
    SauceScaffold(topBar = { TopBarWithClip(title = "Расписание тренировок", goBack = { component.obtainEvent(ScheduleStore.Intent.OnClickBack) }) }) { padding ->
        AdaptivePane(Modifier.fillMaxSize().padding(padding)) { ScheduleContent(state, component::obtainEvent) }
    }
}

@Composable
internal fun ScheduleContent(state: ScheduleStore.State, event: (ScheduleStore.Intent) -> Unit,
    today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) {
    var dateString by rememberSaveable { mutableStateOf(today.toString()) }
    var view by rememberSaveable { mutableStateOf(ClubCalendarView.Week) }
    val date = LocalDate.parse(dateString)
    val week = date.clubWeek()
    val dates = if (view == ClubCalendarView.List) week else listOf(date)
    SauceLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item(key = "intro") {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Планируйте неделю", style = MaterialTheme.typography.headlineSmall, color = SauceTheme.colors.textPrimary)
                Text("Найдите своё направление и удобное время.", style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            }
        }
        item(key = "view") {
            Row(Modifier.fillMaxWidth().background(SauceTheme.colors.surfaceSubtle, SauceShapes.field).padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ClubCalendarView.entries.forEach { mode ->
                    Surface(onClick = { view = mode }, modifier = Modifier.weight(1f).semantics { selected = view == mode },
                        shape = SauceShapes.field, color = if (view == mode) SauceTheme.colors.surface else SauceTheme.colors.surfaceSubtle) {
                        Text(mode.title, Modifier.heightIn(min = 48.dp).wrapContentHeight().padding(horizontal = 4.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center,
                            color = if (view == mode) SauceTheme.colors.action else SauceTheme.colors.textSecondary)
                    }
                }
            }
        }
        if (state.allSchedules.isNotEmpty()) item(key = "filters") {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChipBase(selected = state.selectedWorkoutId == null, onClick = { event(ScheduleStore.Intent.OnClickWorkoutType(null)) }, label = "Все направления")
                state.allSchedules.distinctBy { it.workoutId }.filter { it.workoutId > 0 }.sortedBy { it.workoutTitle }.forEach { workout ->
                    FilterChipBase(selected = state.selectedWorkoutId == workout.workoutId,
                        onClick = { event(ScheduleStore.Intent.OnClickWorkoutType(workout.workoutId)) }, label = workout.workoutTitle.ifBlank { workout.kindOfSport },
                        selectedColors = clubDirectionAccent(workout.workoutTitle.ifBlank { workout.kindOfSport }).clubColors().selectionColors)
                }
            }
        }
        item(key = "calendar") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(if (view == ClubCalendarView.Month) date.clubMonthLabel() else "${week.first().clubDateLabel()} — ${week.last().clubDateLabel()}",
                        Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                    IconButton(modifier = Modifier.size(48.dp), onClick = { dateString = date.clubMovePeriod(view, -1).toString() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Предыдущий период", tint = SauceTheme.colors.textSecondary)
                    }
                    IconButton(modifier = Modifier.size(48.dp), onClick = { dateString = date.clubMovePeriod(view, 1).toString() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Следующий период", tint = SauceTheme.colors.textSecondary)
                    }
                }
                if (view != ClubCalendarView.List) {
                    val grid = if (view == ClubCalendarView.Month) date.clubMonthGrid() else week
                    Row(Modifier.fillMaxWidth()) { clubShortDays.forEach { Text(it, Modifier.weight(1f), textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall, color = SauceTheme.colors.textMuted) } }
                    grid.chunked(7).forEach { row ->
                        Row(Modifier.fillMaxWidth()) {
                            row.forEach { day ->
                                CalendarDay(day, selected = day == date, today = day == today, inMonth = day.month == date.month,
                                    sessions = state.schedules.clubSessions(day), modifier = Modifier.weight(1f)) { dateString = day.toString() }
                            }
                        }
                    }
                }
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    SauceButton("Сегодня", { dateString = today.toString() }, variant = SauceButtonVariant.Text, fillWidth = false)
                }
            }
        }
        if (state.error != null) item(key = "error") { ClubNotice("Не удалось загрузить расписание", "Попробуйте обновить данные.", { event(ScheduleStore.Intent.RetryRequest) }) }
        if (state.isLoading && state.allSchedules.isEmpty()) item(key = "loading") { ClubLoading("Загрузка расписания") }
        else if (state.error == null || state.schedules.isNotEmpty()) {
            dates.forEach { day ->
                val sessions = state.schedules.clubSessions(day)
                item(key = "day-$day") {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("${clubWeekdays[day.dayOfWeek.ordinal]}, ${day.clubDateLabel()}",
                            style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                        if (sessions.isEmpty()) Text("Занятий нет — время для восстановления", style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textMuted)
                    }
                }
                items(sessions, key = { "session-$day-${it.id}" }) { schedule ->
                    ScheduleSessionCard(schedule) { event(ScheduleStore.Intent.OnClickScheduleItem(schedule.id.toString(), schedule.workoutTitle)) }
                }
            }
        }
        item(key = "hint") { Text("Занятия повторяются еженедельно. Точное место и тренера уточняйте при записи.", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted) }
    }
}

@Composable
private fun CalendarDay(date: LocalDate, selected: Boolean, today: Boolean, inMonth: Boolean, sessions: List<Schedule>,
    modifier: Modifier, onClick: () -> Unit) {
    val palette = SauceTheme.colors
    val directions = sessions.map { clubDirectionAccent(it.workoutTitle.ifBlank { it.kindOfSport }) }
        .distinct().sortedBy { it.ordinal }
    Surface(onClick = onClick, modifier = modifier.heightIn(min = 52.dp).semantics {
        contentDescription = "${date.clubDateLabel()} ${date.year}"; this.selected = selected
        stateDescription = if (sessions.isEmpty()) "Нет тренировок" else sessions
            .map { it.workoutTitle.ifBlank { it.kindOfSport } }.filter(String::isNotBlank)
            .distinct().joinToString(", ").ifBlank { "Есть тренировки" }
    }, shape = SauceShapes.field, color = if (selected) palette.actionSoft else palette.background) {
        Column(Modifier.padding(vertical = 6.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(date.day.toString(), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected || today) FontWeight.Bold else FontWeight.Normal),
                color = when { selected || today -> palette.action; !inMonth -> palette.textMuted; else -> palette.textPrimary })
            Row(Modifier.height(5.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                directions.forEach { direction ->
                    Box(Modifier.size(5.dp).background(direction.clubColors().content, CircleShape))
                }
            }
        }
    }
}

@Composable
private fun ScheduleSessionCard(schedule: Schedule, onRegister: () -> Unit) {
    val direction = clubDirectionAccent(schedule.workoutTitle.ifBlank { schedule.kindOfSport })
    val accent = direction.clubColors()
    OnBackgroundCard {
        ClubAccentStripe(direction)
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
                Surface(color = accent.container, shape = SauceShapes.field) {
                    Text(schedule.startDate, Modifier.padding(horizontal = 10.dp, vertical = 12.dp), style = MaterialTheme.typography.titleMedium,
                        color = accent.content)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(schedule.workoutTitle.ifBlank { schedule.kindOfSport }, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                    if (schedule.description.isNotBlank()) HtmlText(text = schedule.description, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary, showImages = false)
                }
            }
            if (schedule.address.isNotBlank()) Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.LocationOn, null, Modifier.size(18.dp), tint = SauceTheme.colors.textMuted)
                Text(schedule.address, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
            }
            if (schedule.trainerFullName.isNotBlank()) Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.PersonOutline, null, Modifier.size(18.dp), tint = SauceTheme.colors.textMuted)
                Text(schedule.trainerFullName, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
            }
            if (schedule.note.isNotBlank()) HtmlText(text = schedule.note, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary, showImages = false)
            if (schedule.isTempDisabled) SauceStatusBadge("Временно недоступно", tone = SauceStatusTone.Neutral, showIndicator = false)
            else SauceButton("Записаться", onRegister, variant = SauceButtonVariant.Outlined, modifier = Modifier.fillMaxWidth())
        }
    }
}
