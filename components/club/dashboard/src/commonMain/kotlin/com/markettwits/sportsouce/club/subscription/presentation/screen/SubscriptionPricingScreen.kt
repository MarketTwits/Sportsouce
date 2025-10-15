package com.markettwits.sportsouce.club.subscription.presentation.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core.errors.api.composable.SauceErrorSimpleContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionColor
import com.markettwits.sportsouce.club.dashboard.domain.Subscription
import com.markettwits.sportsouce.club.info.presentation.components.common.SportHeaderSection
import com.markettwits.sportsouce.club.subscription.presentation.component.SubscriptionPricingComponent
import com.markettwits.sportsouce.club.subscription.presentation.store.SubscriptionPricingStore
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import sportsouce.components.club.dashboard.generated.resources.Res
import sportsouce.components.club.dashboard.generated.resources.im_club_main_image
import kotlin.math.sin

@Composable
fun SubscriptionPricingScreen(
    component: SubscriptionPricingComponent,
) {
    val state by component.state.collectAsState()
    val hazeState = remember { HazeState() }

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
                hazeState = hazeState,
                primaryColor = animatedSubscriptionColor
            )

            Column(
                modifier = Modifier
                    .offset(y = (-35).dp)
                    .hazeSource(state = hazeState)
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

                    state.error != null -> {
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

                    state.subscriptions.isNotEmpty() -> {
                        // Filter chips for pager navigation
                        OnBackgroundCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                maxLines = 3,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
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
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
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
                                                    )
                                                )
                                            }
                                        )
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
    }
}


@Composable
private fun SubscriptionHeader(
    hazeState: HazeState,
    primaryColor: Color = SportSouceColor.SportSouceRegistryOpenGreen,
) {
    SportHeaderSection(
        title = "Абонементы и цены",
        subtitle = "на тренировки",
        description = "Выберите подходящий план тренировок",
        badge = "СПОРТ СОЮЗ",
        hazeState = hazeState,
        backgroundImage = painterResource(Res.drawable.im_club_main_image),
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
    subscription: Subscription,
    priceCalculationState: SubscriptionPricingStore.PriceCalculationState?,
    onMonthCountChanged: (Int) -> Unit,
    onSubscribeClick: () -> Unit,
) {
    val currentMonthCount = priceCalculationState?.monthCount ?: 1
    val isLoading = priceCalculationState?.isLoading ?: false
    val workoutPrice = priceCalculationState?.workoutPrice
    val colors = getSubscriptionColors(subscription.color)

    // Wave animation state
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )

    // Initialize price calculation on first composition
    LaunchedEffect(subscription.id) {
        if (priceCalculationState == null) {
            onMonthCountChanged(1)
        }
    }

    OnBackgroundCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 0.8.dp,
                color = colors.first.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .drawBehind {
                drawWaveBackground(colors.first, waveOffset)
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(colors.first)
                    .align(Alignment.TopCenter)
            )

            // Subtle gradient background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                colors.first.copy(alpha = 0.03f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title section
                Column {
                    Text(
                        text = subscription.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontNunito.bold(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HtmlText(
                        text = subscription.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        fontFamily = FontNunito.regular(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Enhanced price and controls section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (subscription.priceDependsOnCount) {
                        // Month counter
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (currentMonthCount > 1) {
                                        onMonthCountChanged(currentMonthCount - 1)
                                    }
                                },
                                enabled = currentMonthCount > 1,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = if (currentMonthCount > 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(
                                        alpha = 0.5f
                                    ),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Animated number display
                            Box(
                                modifier = Modifier.width(40.dp),
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
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontFamily = FontNunito.bold(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    if (currentMonthCount < subscription.maxAmount) {
                                        onMonthCountChanged(currentMonthCount + 1)
                                    }
                                },
                                enabled = currentMonthCount < subscription.maxAmount,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = if (currentMonthCount < subscription.maxAmount) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(
                                        alpha = 0.5f
                                    ),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Enhanced price display with smooth transitions
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.height(60.dp)
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
                            if (isLoading) {
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(24.dp)
                                        .shimmer()
                                )
                            } else {
                                workoutPrice?.let { price ->
                                    val discountPercent = if (price.priceWithoutDiscounts > price.totalPrice) {
                                        (price.priceWithoutDiscounts - price.totalPrice) * 100 / price.priceWithoutDiscounts
                                    } else null

                                    // Price elements: current price on left, discount info on right
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Current price on the left
                                        Text(
                                            text = "₽ ${price.totalPrice}",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = colors.first,
                                            fontFamily = FontNunito.extraBold()
                                        )

                                        // Show discount info on the right if there's a discount
                                        if (discountPercent != null) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "₽ ${price.priceWithoutDiscounts}",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                                    textDecoration = TextDecoration.LineThrough,
                                                    fontFamily = FontNunito.bold()
                                                )
                                                Text(
                                                    text = "-${discountPercent}%",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = colors.first,
                                                    fontFamily = FontNunito.bold()
                                                )
                                            }
                                        }
                                    }
                                } ?: run {
                                    Text(
                                        text = "₽ ${subscription.price}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = colors.first,
                                        fontFamily = FontNunito.extraBold()
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Enhanced subscribe button with animation
                    val buttonAlpha by animateFloatAsState(
                        targetValue = if (isLoading) 0.7f else 1f,
                        animationSpec = tween(300),
                        label = "button_alpha"
                    )

                    Button(
                        onClick = onSubscribeClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(buttonAlpha),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.first,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Записаться",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontNunito.bold(),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
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

private fun DrawScope.drawWaveBackground(color: Color, offset: Float) {
    val width = size.width
    val height = size.height
    val waveHeight = 8f
    val frequency = 0.02f

    val path = Path()

    // Create a subtle wave at the bottom
    path.moveTo(0f, height - 20f)

    for (x in 0..width.toInt() step 2) {
        val y = height - 20f + sin((x * frequency) + (offset * 0.017f)) * waveHeight
        path.lineTo(x.toFloat(), y)
    }

    path.lineTo(width, height)
    path.lineTo(0f, height)
    path.close()

    drawPath(
        path = path,
        color = color.copy(alpha = 0.04f)
    )

    // Add another smaller wave for depth
    val path2 = Path()
    path2.moveTo(0f, height - 35f)

    for (x in 0..width.toInt() step 3) {
        val y = height - 35f + sin((x * frequency * 1.5f) + (offset * 0.023f)) * (waveHeight * 0.6f)
        path2.lineTo(x.toFloat(), y)
    }

    path2.lineTo(width, height)
    path2.lineTo(0f, height)
    path2.close()

    drawPath(
        path = path2,
        color = color.copy(alpha = 0.02f)
    )
}