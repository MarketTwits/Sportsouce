package com.markettwits.sportsouce.club.dashboard.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core.errors.api.composable.SauceErrorSimpleContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.sportsouce.club.dashboard.presentation.components.bottomsheet.MenuBottomSheet
import com.markettwits.sportsouce.club.dashboard.presentation.components.bottomsheet.MenuBottomSheetItem
import com.markettwits.sportsouce.club.dashboard.presentation.components.bottomsheet.MenuBottomSheetType
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import dev.chrisbanes.haze.*
import org.jetbrains.compose.resources.painterResource
import sportsouce.components.club.dashboard.generated.resources.Res
import sportsouce.components.club.dashboard.generated.resources.im_club_main_image

data class PriceItem(
    val price: String,
    val oldPrice: String,
    val description: String,
)

@Composable
fun ClubDashboardScreen(
    component: ClubDashboardComponent
) {
    val state by component.state.collectAsState()
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedBottomSheetTab by remember { mutableStateOf<MenuBottomSheetType?>(null) }
    val hazeState = remember { HazeState() }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ClubDashboardHeader(hazeState = hazeState)

            Column(
                modifier = Modifier
                    .offset(y = (-35).dp)
                    .hazeSource(state = hazeState)
                    .padding(horizontal = 16.dp)
            ) {

            if (state.isLoading) {
                LoadingFullScreen()
            }

                state.error?.SauceErrorSimpleContent(
                    onClickRetry = { component.obtainEvent(ClubDashboardStore.Intent.RetryRequest) }
                )

                SubscriptionInfoCard {
                    component.obtainEvent(ClubDashboardStore.Intent.OnClickSubscriptions)
                }

                Spacer(modifier = Modifier.height(24.dp))

                MenuGrid(
                    onPlanClick = {
                        selectedBottomSheetTab = MenuBottomSheetType.PLAN
                        isBottomSheetVisible = true
                    },
                    onTrainingsClick = {
                        selectedBottomSheetTab = MenuBottomSheetType.TRAININGS
                        isBottomSheetVisible = true
                    },
                    onClubBonusesClick = {
                        selectedBottomSheetTab = MenuBottomSheetType.CLUB_BONUSES
                        isBottomSheetVisible = true
                    },
                    onOurTeamClick = {
                        selectedBottomSheetTab = MenuBottomSheetType.OUR_TEAM
                        isBottomSheetVisible = true
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ScheduleCard {
                    component.obtainEvent(ClubDashboardStore.Intent.OnClickSchedule)
                }

                Spacer(modifier = Modifier.height(24.dp))

                MoreSection {
                    selectedBottomSheetTab = MenuBottomSheetType.FAQ
                    isBottomSheetVisible = true
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        BackFloatingActionButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            component.obtainEvent(ClubDashboardStore.Intent.OnClickBack)
        }

        MenuBottomSheet(
            isVisible = isBottomSheetVisible,
            selectedTab = selectedBottomSheetTab,
            bottomSheetData = state.bottomSheetData,
            onDismiss = {
                isBottomSheetVisible = false
                selectedBottomSheetTab = null
            },
            onClickSubscribe = {
                component.obtainEvent(ClubDashboardStore.Intent.OnClickRegistration(RegistrationType.Trainer(5)))
            },
            onMenuItemClick = { menuItem ->
                isBottomSheetVisible = false
                selectedBottomSheetTab = null
                when (menuItem) {
                    MenuBottomSheetItem.PLAN -> selectedBottomSheetTab = MenuBottomSheetType.PLAN
                    MenuBottomSheetItem.EVENTS -> selectedBottomSheetTab = MenuBottomSheetType.TRAININGS
                    MenuBottomSheetItem.CLUB_BONUSES -> selectedBottomSheetTab = MenuBottomSheetType.CLUB_BONUSES
                    MenuBottomSheetItem.OUR_TEAM -> selectedBottomSheetTab = MenuBottomSheetType.OUR_TEAM
                    MenuBottomSheetItem.TRAINING_SCHEDULE ->
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickSchedule)

                    MenuBottomSheetItem.FAQ -> selectedBottomSheetTab = MenuBottomSheetType.FAQ
                    else -> {}
                }
            }
        )
    }
}

@Composable
private fun ClubDashboardHeader(
    hazeState: HazeState,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.im_club_main_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            SportSouceColor.SportSouceLighBlue.copy(alpha = 0.9f),
                            SportSouceColor.SportSouceRegistryOpenGreen.copy(alpha = 0.8f),
                            SportSouceColor.SportSouceLighBlue.copy(alpha = 0.8f),
                            SportSouceColor.SportSouceRegistryOpenGreen.copy(alpha = 0.8f),
                            SportSouceColor.SportSouceLighBlue.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        // Blur эффект
        Box(
            modifier = Modifier
                .matchParentSize()
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        tint = HazeTint(MaterialTheme.colorScheme.background.copy(alpha = 0.2f)),
                        blurRadius = 32.dp
                    )
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "СПОРТ СОЮЗ",
                fontSize = 34.sp,
                fontFamily = FontNunito.black(),
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "КОМАНДА И КЛУБ",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "ЛЫЖИ ПЛАВАНИЕ БЕГ",
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
private fun SubscriptionPromoCard(
    priceItems: List<PriceItem>,
    onViewAllClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Преимущества с подпиской",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontNunito.extraBold()
                )
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            priceItems.forEach { priceItem ->
                PriceItemRow(priceItem)
                if (priceItem != priceItems.last()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(
                onClick = onViewAllClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Посмотреть все",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontNunito.bold()
                )
            }
        }
    }
}

@Composable
private fun PriceItemRow(item: PriceItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.price,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontNunito.extraBold()
                )
                Text(
                    text = item.oldPrice,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                    fontFamily = FontNunito.regular()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.outline,
                lineHeight = 16.sp,
                fontFamily = FontNunito.regular()
            )
        }
    }
}

@Composable
private fun SubscriptionInfoCard(onClick: () -> Unit) {
    InfoCard(
        title = "Абонементы и цены",
        description = "Наша команда — мастера спорта и опытные тренеры, чемпионы и призёры России и мира по лыжным гонкам, плаванию и полиатлону.",
        icon = Icons.Default.CreditCard,
        onClick = onClick
    )
}

@Composable
private fun MenuGrid(
    onPlanClick: () -> Unit,
    onTrainingsClick: () -> Unit,
    onClubBonusesClick: () -> Unit,
    onOurTeamClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MenuGridItem(
                title = "План",
                description = "Изучите наш план тренировок",
                icon = Icons.AutoMirrored.Filled.EventNote,
                onClick = onPlanClick
            )
            MenuGridItem(
                title = "Бонусы клуба",
                description = "Специальные предложения",
                icon = Icons.Default.Star,
                onClick = onClubBonusesClick
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MenuGridItem(
                title = "Тренировки",
                description = "Мероприятия и тренировки",
                icon = Icons.Default.FitnessCenter,
                onClick = onTrainingsClick
            )
            MenuGridItem(
                title = "Наша команда",
                description = "Познакомьтесь с тренерами",
                icon = Icons.Default.Group,
                onClick = onOurTeamClick
            )
        }
    }
}

@Composable
private fun MenuGridItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                fontFamily = FontNunito.regular()
            )
        }
    }
}

@Composable
private fun ScheduleCard(onClick: () -> Unit) {
    InfoCard(
        title = "Расписание тренировок",
        description = "Расписание актуально на текущую неделю и обновляется в воскресенье",
        icon = Icons.Default.CalendarMonth,
        onClick = onClick
    )
}

@Composable
private fun MoreSection(onFaqClick: () -> Unit) {
    Column {
        Text(
            text = "Еще",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontFamily = FontNunito.extraBold()
        )
        Spacer(modifier = Modifier.height(16.dp))
        InfoCard(
            title = "Вопросы и ответы",
            description = "Здесь вы найдете ответы на самые часто задаваемые вопросы",
            icon = Icons.AutoMirrored.Filled.HelpOutline,
            onClick = onFaqClick
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontNunito.extraBold()
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 8.dp),
                    lineHeight = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
