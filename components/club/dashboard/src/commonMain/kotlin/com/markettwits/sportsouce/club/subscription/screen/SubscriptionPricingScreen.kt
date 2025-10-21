package com.markettwits.sportsouce.club.subscription.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core.errors.api.composable.SauceErrorSimpleContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.ScrollToTopBabButton
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.extensions.formatRubles
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionColor
import com.markettwits.sportsouce.club.dashboard.domain.Subscription
import com.markettwits.sportsouce.club.info.presentation.components.common.SportHeaderSection
import com.markettwits.sportsouce.club.subscription.component.SubscriptionPricingComponent
import com.markettwits.sportsouce.club.subscription.store.SubscriptionPricingStore
import kotlinx.coroutines.launch

@Composable
fun SubscriptionPricingScreen(
    component: SubscriptionPricingComponent,
) {
    val state by component.state.collectAsState()

    val pagerState = rememberPagerState(pageCount = { state.subscriptions.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Calculate current subscription color based on pager state
            val targetSubscriptionColor =
                if (state.subscriptions.isNotEmpty() && pagerState.currentPage < state.subscriptions.size) {
                    val currentSubscriptions = state.subscriptions[pagerState.currentPage].subscription
                    if (currentSubscriptions.isNotEmpty()) {
                        getSubscriptionColors(currentSubscriptions.first().color).first
                    } else {
                        SportSouceColor.SportSouceRegistryOpenGreen
                    }
                } else {
                    SportSouceColor.SportSouceRegistryOpenGreen
                }

            // Animate color transitions
            val animatedSubscriptionColor by animateColorAsState(
                targetValue = targetSubscriptionColor,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                label = "header_color_transition"
            )

            SubscriptionHeader(
                primaryColor = animatedSubscriptionColor
            )
            Column(
                modifier = Modifier
                    .offset(y = (-35).dp)
                    .padding(horizontal = 16.dp)
            ) {

                when {
                    state.isLoading -> {
                        // Filter chips shimmer
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 16.dp)
                        ) {
                            repeat(4) {
                                ShimmerFilterChip()
                            }
                        }

                        // Subscription cards shimmer
                        AdaptivePane {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth(),
                                maxItemsInEachRow = 2
                            ) {
                                repeat(4) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .widthIn(min = 280.dp, max = 400.dp)
                                    ) {
                                        ShimmerSubscriptionCard()
                                    }
                                }
                            }
                        }
                    }

                    state.error != null -> {
                        AdaptivePane {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp)
                            ) {
                                state.error?.let {
                                    it.SauceErrorSimpleContent(
                                        onClickRetry = {
                                            component.obtainEvent(SubscriptionPricingStore.Intent.RetryRequest)
                                        }
                                    )
                                }
                            }
                        }
                    }


                    state.subscriptions.isNotEmpty() -> {
                        // Filter chips for pager navigation
                        AdaptivePane {
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
                                FlowRow(

                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    maxLines = 3,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(horizontal = 20.dp, vertical = 16.dp)
                                ) {
                                    state.subscriptions.forEachIndexed { index, subscriptionItem ->
                                        AnimatedFilterChip(
                                            selected = pagerState.currentPage == index,
                                            onClick = {
                                                scope.launch {
                                                    pagerState.animateScrollToPage(index)
                                                }
                                            },
                                            label = subscriptionItem.groupName
                                        )
                                    }
                                }
                            }

                        }

                        AdaptivePane {
                            Spacer(modifier = Modifier.height(20.dp))

                            // HorizontalPager with subscription grids
                            HorizontalPager(
                                state = pagerState,
                                userScrollEnabled = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) { pageIndex ->
                                val subscriptions = state.subscriptions[pageIndex].subscription

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(24.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    maxItemsInEachRow = 2
                                ) {
                                    subscriptions.forEach { subscription ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .widthIn(min = 280.dp, max = 400.dp)
                                        ) {
                                            SubscriptionCard(
                                                modifier = Modifier.padding(horizontal = 12.dp),
                                                subscription = subscription,
                                                priceCalculationState = state.priceCalculations[subscription.id.toString()],
                                                onMonthCountChanged = { monthCount ->
                                                    component.obtainEvent(
                                                        SubscriptionPricingStore.Intent.UpdateMonthCount(
                                                            subscriptionId = subscription.id,
                                                            monthCount = monthCount
                                                        )
                                                    )
                                                },
                                                onSubscribeClick = {
                                                    component.obtainEvent(
                                                        SubscriptionPricingStore.Intent.OnClickSubscribe(
                                                            subscription.id,
                                                            subscription.name,
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        EmptySubscriptionsMessage()
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        BackFloatingActionButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            component.obtainEvent(SubscriptionPricingStore.Intent.OnClickBack)
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
                .align(Alignment.BottomEnd)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
        ) {
            ScrollToTopBabButton(
                onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
            )
        }
    }
}

@Composable
private fun SubscriptionHeader(
    primaryColor: Color = SportSouceColor.SportSouceRegistryOpenGreen,
) {
    SportHeaderSection(
        title = "Абонементы и цены",
        subtitle = "на тренировки",
        description = "Выберите подходящий план тренировок",
        badge = "СПОРТ СОЮЗ",
        primaryColor = primaryColor
    )
}

@Composable
private fun AnimatedFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
) {
    FilterChipBase(
        selected = selected,
        onClick = onClick,
        label = label
    )
}

@Composable
private fun SubscriptionCard(
    modifier: Modifier = Modifier,
    subscription: Subscription,
    priceCalculationState: SubscriptionPricingStore.PriceCalculationState?,
    onMonthCountChanged: (Int) -> Unit,
    onSubscribeClick: () -> Unit,
) {
    val currentMonthCount = priceCalculationState?.monthCount ?: 1
    val isLoading = priceCalculationState?.isLoading ?: false
    val workoutPrice = priceCalculationState?.workoutPrice
    val colors = getSubscriptionColors(subscription.color)

    // Initialize price calculation on first composition
    LaunchedEffect(subscription.id) {
        if (priceCalculationState == null) {
            onMonthCountChanged(1)
        }
    }

    OnBackgroundCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Modern gradient background with wave pattern
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                colors.first.copy(alpha = 0.08f),
                                colors.first.copy(alpha = 0.03f),
                                Color.Transparent
                            ),
                            radius = 400f
                        ),
                        RoundedCornerShape(20.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = if (subscription.priceDependsOnCount) Arrangement.SpaceBetween else Arrangement.Top
            ) {
                // Header section with title
                Column {
                    Text(
                        text = subscription.name.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.first,
                        fontFamily = FontNunito.extraBold(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HtmlText(
                        text = subscription.description,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        fontFamily = FontNunito.regular(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Add spacer for cards without quantity counter to center the price vertically
                if (!subscription.priceDependsOnCount) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Main content section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    if (subscription.priceDependsOnCount) {
                        // Modern month counter with clean styling
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = colors.first.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            // Decrease button
                            TextButton(
                                modifier = Modifier
                                    .size(32.dp),
                                shape = RoundedCornerShape(8.dp),
                                enabled = currentMonthCount > 1,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.first,
                                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    if (currentMonthCount > 1) {
                                        onMonthCountChanged(currentMonthCount - 1)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
//
                            Box(
                                modifier = Modifier.width(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AnimatedContent(
                                    targetState = currentMonthCount,
                                    transitionSpec = {
                                        if (targetState > initialState) {
                                            slideInVertically { height -> height } + fadeIn() togetherWith
                                                    slideOutVertically { height -> -height } + fadeOut()
                                        } else {
                                            slideInVertically { height -> -height } + fadeIn() togetherWith
                                                    slideOutVertically { height -> height } + fadeOut()
                                        }.using(SizeTransform(clip = false))
                                    },
                                    label = "month_count"
                                ) { count ->
                                    Text(
                                        text = count.toString(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = colors.first,
                                        fontFamily = FontNunito.extraBold(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            TextButton(
                                modifier = Modifier
                                    .size(32.dp),
                                shape = RoundedCornerShape(8.dp),
                                enabled = currentMonthCount < subscription.maxAmount,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.first,
                                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    if (currentMonthCount < subscription.maxAmount) {
                                        onMonthCountChanged(currentMonthCount + 1)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Modern price display section
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.height(70.dp)
                    ) {
                        val alpha by animateFloatAsState(
                            targetValue = if (isLoading) 0.3f else 1f,
                            animationSpec = tween(300),
                            label = "price_alpha"
                        )

                        Box(
                            modifier = Modifier.alpha(alpha),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (isLoading) {
                                    // Shimmer for discount price if it would be shown
                                    if (subscription.priceDependsOnCount) {
                                        Box(
                                            modifier = Modifier
                                                .width(60.dp)
                                                .height(16.dp)
                                                .shimmer()
                                                .background(
                                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                                    RoundedCornerShape(4.dp)
                                                )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }

                                    // Shimmer for main price
                                    Box(
                                        modifier = Modifier
                                            .width(120.dp)
                                            .height(36.dp)
                                            .shimmer()
                                            .background(
                                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                                RoundedCornerShape(8.dp)
                                            )
                                    )
                                } else {
                                    workoutPrice?.let { price ->
                                        val discountPercent = if (price.priceWithoutDiscounts > price.totalPrice) {
                                            (price.priceWithoutDiscounts - price.totalPrice) * 100 / price.priceWithoutDiscounts
                                        } else null

                                        // Show discount info first if available
                                        if (discountPercent != null) {
                                            Text(
                                                text = "₽ ${price.priceWithoutDiscounts.formatRubles()}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                                textDecoration = TextDecoration.LineThrough,
                                                fontFamily = FontNunito.bold()
                                            )
                                        }

                                        // Main price display
                                        Text(
                                            text = "₽ ${price.totalPrice.formatRubles()}",
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = colors.first,
                                            fontFamily = FontNunito.extraBold()
                                        )
                                    } ?: run {
                                        Text(
                                            text = "₽ ${subscription.price.formatRubles()}",
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = colors.first,
                                            fontFamily = FontNunito.extraBold()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Add another spacer for cards without quantity counter to center the price vertically
                if (!subscription.priceDependsOnCount) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Modern subscribe button
                val buttonAlpha by animateFloatAsState(
                    targetValue = if (isLoading) 0.7f else 1f,
                    animationSpec = tween(300),
                    label = "button_alpha"
                )

                Button(
                    onClick = onSubscribeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .alpha(buttonAlpha),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.first,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Text(
                        text = "Записаться",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontNunito.extraBold()
                    )
                }
            }
        }
    }
}

@Composable
private fun ShimmerFilterChip() {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)
            .shimmer()
            .background(
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                RoundedCornerShape(20.dp)
            )
    )
}

@Composable
private fun ShimmerSubscriptionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .shimmer(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(6.dp)
                        )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(16.dp)
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            RoundedCornerShape(4.dp)
                        )
                )
            }

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(40.dp)
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(6.dp)
                        )
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            RoundedCornerShape(16.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun EmptySubscriptionsMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "💸 Абонементы временно недоступны",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🔄 Попробуйте обновить страницу",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.regular()
            )
        }
    }
}


private fun getSubscriptionColors(color: SubscriptionColor): Pair<Color, Color> {
    return when (color) {
        SubscriptionColor.BLUE -> Pair(
            SportSouceColor.SportSouceLighBlue,
            SportSouceColor.SportSouceBlue
        )

        SubscriptionColor.GREEN -> Pair(
            SportSouceColor.SportSouceRegistryOpenGreen,
            SportSouceColor.SportSouceRegistryOpenGreen.copy(alpha = 0.8f)
        )

        SubscriptionColor.PURPLE -> Pair(
            SportSouceColor.SportSouceStartEndedPink,
            SportSouceColor.SportSouceStartEndedPink.copy(alpha = 0.8f)
        )

        SubscriptionColor.ORANGE -> Pair(
            SportSouceColor.SportSouceRegistryCommingSoonYellow,
            SportSouceColor.SportSouceRegistryCommingSoonYellow.copy(alpha = 0.8f)
        )
    }
}