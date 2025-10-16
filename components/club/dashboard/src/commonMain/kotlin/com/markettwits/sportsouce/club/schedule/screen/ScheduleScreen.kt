package com.markettwits.sportsouce.club.schedule.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core.errors.api.composable.SauceErrorSimpleContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.sportsouce.club.info.domain.models.Schedule
import com.markettwits.sportsouce.club.info.presentation.components.common.SportHeaderSection
import com.markettwits.sportsouce.club.schedule.component.ScheduleComponent
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ScheduleScreen(
    component: ScheduleComponent,
) {
    val state by component.state.collectAsState()
    val windowSizeClass = calculateWindowSizeClass()
    val isLargeScreen = windowSizeClass.isLarge

    Box(modifier = Modifier.fillMaxSize()) {
        ScheduleContent(
            state = state,
            isLargeScreen = isLargeScreen,
            onClickWorkoutType = { workoutId ->
                component.obtainEvent(
                    ScheduleStore.Intent.OnClickWorkoutType(
                        workoutId
                    )
                )
            },
            onClickScheduleItem = { scheduleId, scheduleName ->
                component.obtainEvent(
                    ScheduleStore.Intent.OnClickScheduleItem(
                        scheduleId,
                        scheduleName
                    )
                )
            },
            onRetry = {
                component.obtainEvent(ScheduleStore.Intent.RetryRequest)
            }
        )

        BackFloatingActionButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            component.obtainEvent(ScheduleStore.Intent.OnClickBack)
        }
    }
}

@Composable
private fun SchedulePromoHeader() {
    SportHeaderSection(
        title = "Расписание тренировок",
        subtitle = null,
        description = "Выберите удобное время для занятий и достигайте новых высот",
        badge = "СПОРТ СОЮЗ",
        height = 350,
        primaryColor = SportSouceColor.SportSouceLighBlue
    )
}


@Composable
private fun ScheduleContent(
    state: ScheduleStore.State,
    isLargeScreen: Boolean,
    onClickWorkoutType: (Int?) -> Unit,
    onClickScheduleItem: (String, String) -> Unit,
    onRetry: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SchedulePromoHeader()

            Column(
                modifier = Modifier
                    .offset(y = (-35).dp)
                    .padding(horizontal = 16.dp)
            ) {
                state.error?.SauceErrorSimpleContent(onClickRetry = onRetry)

                if (state.allWorkoutTypes.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            WorkoutTypeFilter(
                                workoutTypes = state.allWorkoutTypes,
                                selectedWorkoutId = state.selectedWorkoutId,
                                onWorkoutTypeClick = onClickWorkoutType,
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }

                AdaptivePane {
                    when {
                        state.isLoading -> {
                            Card(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 8.dp,
                                    bottomEnd = 8.dp
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        repeat(4) {
                                            ShimmerScheduleCard(isLargeScreen = isLargeScreen)
                                        }
                                    }
                                }
                            }
                        }

                        state.schedules.isNotEmpty() -> {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp),
                                modifier = Modifier.fillMaxWidth(),
                                maxItemsInEachRow = 2
                            ) {
                                state.schedules.forEach { schedule ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .widthIn(min = if (isLargeScreen) 320.dp else 280.dp)
                                    ) {
                                        ScheduleCard(
                                            schedule = schedule,
                                            onItemClick = {
                                                onClickScheduleItem(
                                                    schedule.id.toString(),
                                                    schedule.workoutTitle
                                                )
                                            },
                                            isLargeScreen = isLargeScreen
                                        )
                                    }
                                }
                            }
                        }

                        state.schedules.isEmpty() && !state.isLoading && state.error == null -> {
                            EmptyScheduleMessage(isLargeScreen = isLargeScreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Scroll to top FAB
        val showScrollToTop by remember {
            derivedStateOf { scrollState.value > 200 }
        }

        AnimatedVisibility(
            visible = showScrollToTop,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    }
}

@Composable
private fun WorkoutTypeFilter(
    workoutTypes: List<String>,
    selectedWorkoutId: Int?,
    onWorkoutTypeClick: (Int?) -> Unit,
) {
    FlowRow(
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        workoutTypes.forEachIndexed { index, workoutTitle ->
            val workoutId = index + 1
            val isSelected = workoutId == selectedWorkoutId

            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally() + fadeIn(),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                FilterChipBase(
                    selected = isSelected,
                    onClick = { onWorkoutTypeClick(workoutId) },
                    label = workoutTitle,
                )
            }
        }

    }
}


@Composable
private fun ScheduleCard(
    schedule: Schedule,
    onItemClick: () -> Unit,
    isLargeScreen: Boolean,
) {
    OnBackgroundCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isLargeScreen) 20.dp else 16.dp),
        onClick = onItemClick
    ) {
        Column(
            modifier = Modifier.padding(if (isLargeScreen) 20.dp else 16.dp)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (schedule.weekday.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(if (isLargeScreen) 18.dp else 16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(if (isLargeScreen) 6.dp else 4.dp))
                        Text(
                            text = schedule.weekday,
                            fontSize = if (isLargeScreen) 16.sp else 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontNunito.medium()
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(if (isLargeScreen) 18.dp else 16.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(if (isLargeScreen) 6.dp else 4.dp))
                    Text(
                        text = schedule.startDate,
                        fontSize = if (isLargeScreen) 16.sp else 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontNunito.medium()
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (isLargeScreen) 16.dp else 12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    modifier = Modifier.size(if (isLargeScreen) 22.dp else 18.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(if (isLargeScreen) 12.dp else 8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = schedule.workoutTitle,
                        fontSize = if (isLargeScreen) 18.sp else 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontNunito.bold(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (schedule.address.isNotEmpty()) {
                        Text(
                            text = schedule.address,
                            fontSize = if (isLargeScreen) 14.sp else 12.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontNunito.regular(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            if (schedule.trainerFullName.isNotEmpty()) {
                Spacer(modifier = Modifier.height(if (isLargeScreen) 12.dp else 8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(if (isLargeScreen) 18.dp else 16.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(if (isLargeScreen) 6.dp else 4.dp))
                    Text(
                        text = schedule.trainerFullName,
                        fontSize = if (isLargeScreen) 16.sp else 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontNunito.medium()
                    )
                }
            }

        }
    }
}


@Composable
private fun ShimmerScheduleCard(isLargeScreen: Boolean) {
    Card(
        modifier = Modifier
            .shimmer(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(16.dp)
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(16.dp)
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(4.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(16.dp)
                    .background(
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

@Composable
private fun EmptyScheduleMessage(isLargeScreen: Boolean) {
    OnBackgroundCard { modifier ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(if (isLargeScreen) 300.dp else 240.dp)
                .padding(if (isLargeScreen) 48.dp else 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Расписание пусто",
                    fontSize = if (isLargeScreen) 24.sp else 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontFamily = FontNunito.bold()
                )
                Spacer(modifier = Modifier.height(if (isLargeScreen) 16.dp else 12.dp))
                Text(
                    text = "Выберите тип тренировки выше или попробуйте позже",
                    fontSize = if (isLargeScreen) 16.sp else 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    fontFamily = FontNunito.regular()
                )
            }
        }
    }
}