package com.markettwits.sportsouce.profile.upcoming

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.image.*
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowScreen
import com.markettwits.sportsouce.notification.impl.presentation.components.NotificationButton
import com.markettwits.sportsouce.notification.impl.presentation.screen.NotificationScreen
import com.markettwits.sportsouce.profile.registrations.presentation.root.RootRegistrationsScreen
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.*
import com.markettwits.sportsouce.settings.root.RootSettingsScreen
import com.markettwits.sportsouce.start.root.RootStartScreen
import com.markettwits.sportsouce.start.search.root.RootStartsSearchScreen
import com.markettwits.sportsouce.start.search.search.presentation.components.publish.StartsSearchBarPublic
import com.markettwits.sportsouce.starts.albums.root.RootStartsAlbumsScreen

@Composable
fun UpcomingStartsScreen(component: UpcomingStartsComponent) {
    val unreadCount by component.unreadCount.collectAsState()
    Children(stack = component.childStack, animation = stackAnimation(fade())) { child ->
        when (val screen = child.instance) {
            is UpcomingStartsComponent.Child.Dashboard -> {
                val state by screen.component.state.collectAsState()
                UpcomingStartsContent(state, unreadCount, screen.component::obtainEvent)
            }
            is UpcomingStartsComponent.Child.Registrations -> RootRegistrationsScreen(screen.component)
            is UpcomingStartsComponent.Child.Auth -> RootAuthFlowScreen(screen.component)
            is UpcomingStartsComponent.Child.Search -> RootStartsSearchScreen(screen.component)
            is UpcomingStartsComponent.Child.Settings -> RootSettingsScreen(screen.component)
            is UpcomingStartsComponent.Child.Inbox -> NotificationScreen(screen.component)
            is UpcomingStartsComponent.Child.PhotoArchive -> RootStartsAlbumsScreen(screen.component)
            is UpcomingStartsComponent.Child.Start -> RootStartScreen(screen.component)
        }
    }
}

@Composable
internal fun UpcomingStartsContent(state: UpcomingStartsStore.State, unreadCount: Int, onEvent: (Intent) -> Unit) {
    val content = state.content
    val (longRunning, regular) = remember(content, state.now) {
        (content as? Content.Ready)?.registrations.orEmpty()
            .filter { it.expires > state.now }
            .partition { it.isLongRunning }
    }
    val (paid, unpaid) = remember(regular) { regular.partition { it.isPaid } }
    val next = paid.firstOrNull() ?: longRunning.firstOrNull { it.isPaid }.takeIf { regular.isEmpty() }
    val later = remember(paid, next) { paid.filterNot { it == next } }
    val series = remember(longRunning, next) { longRunning.filterNot { it == next } }
    val colors = SauceTheme.colors
    val listState = rememberLazyListState()
    val toolbarState = rememberCollapsingToolbarScaffoldState()
    var heroHeight by remember { mutableIntStateOf(0) }
    var coverColor by remember(next?.order?.id, next?.order?.image) { mutableStateOf(Color.Unspecified) }
    val tint by rememberImageBackdropTint(coverColor)
    val heroVisibility by remember(listState) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0 || heroHeight == 0) 0f
            else (1f - listState.firstVisibleItemScrollOffset.toFloat() / heroHeight).coerceIn(0f, 1f)
        }
    }
    val glassFadeDistance = with(LocalDensity.current) { 40.dp.toPx() }
    val glassAlpha by remember(listState, glassFadeDistance) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / glassFadeDistance).coerceIn(0f, 1f)
        }
    }
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize().drawBehind {
            if (heroVisibility > 0f && tint != colors.background) {
                val toolbarBottom = (toolbarState.toolbarState.height + toolbarState.offsetY).coerceAtLeast(0)
                drawRect(
                    Brush.verticalGradient(
                        colors = listOf(lerp(colors.background, tint, heroVisibility), colors.background),
                        startY = toolbarBottom.toFloat(),
                        endY = toolbarBottom + heroHeight * heroVisibility,
                    )
                )
            } else {
                drawRect(colors.background)
            }
        },
        state = toolbarState,
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbarGlassColor = if (tint == colors.background) colors.surface else lerp(colors.surface, tint, heroVisibility),
        toolbarGlassAlpha = glassAlpha,
        toolbar = {
            StartsSearchBarPublic(
                onClickSearchPanel = { onEvent(Intent.OpenToolbar(ToolbarDestination.Search)) },
                onClickSettings = { onEvent(Intent.OpenToolbar(ToolbarDestination.Settings)) },
                actions = { NotificationButton(unreadCount) { onEvent(Intent.OpenToolbar(ToolbarDestination.Inbox)) } },
            )
        },
    ) {
        Box(Modifier.fillMaxSize(), Alignment.TopCenter) {
            PullToRefreshScreen(
                modifier = Modifier.widthIn(max = 600.dp).fillMaxSize(),
                isRefreshing = state.isRefreshing,
                onRefresh = { onEvent(Intent.Refresh) },
            ) {
                if (content is Content.Failed) {
                    content.error.SauceErrorScreen(onClickRetry = { onEvent(Intent.Refresh) })
                } else {
                    SauceLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        when (content) {
                            Content.Loading -> item { UpcomingStartsPlaceholder() }
                            Content.Guest -> item {
                                NoUpcomingStarts(guest = true, onClick = { onEvent(Intent.SignIn) })
                            }
                            is Content.Ready -> {
                                if (next == null && unpaid.isEmpty() && series.isEmpty()) {
                                    item { NoUpcomingStarts(guest = false, onClick = {}) }
                                }
                                if (next != null) {
                                    item(key = "next-${next.order.id}") {
                                        NextStartCard(
                                            registration = next,
                                            ongoing = next.isInProgress(state.now),
                                            countdown = next.countdown(state.now),
                                            modifier = Modifier.onSizeChanged { heroHeight = it.height },
                                            onCoverColor = { coverColor = it },
                                        ) {
                                            onEvent(Intent.OpenRegistration(next.order.id))
                                        }
                                    }
                                }
                                if (later.isNotEmpty()) {
                                    item(key = "later-header") { UpcomingHeading("Дальше по календарю", Modifier.padding(top = 8.dp)) }
                                    items(later, key = { it.order.id }) { registration ->
                                        RegistrationRow(registration, ongoing = registration.isInProgress(state.now)) {
                                            onEvent(Intent.OpenRegistration(registration.order.id))
                                        }
                                    }
                                }
                                if (unpaid.isNotEmpty()) {
                                    item(key = "unpaid-header") {
                                        UpcomingHeading("Ожидают оплаты", Modifier.padding(top = if (next == null) 0.dp else 8.dp))
                                    }
                                    items(unpaid, key = { it.order.id }) { registration ->
                                        RegistrationRow(registration) { onEvent(Intent.OpenRegistration(registration.order.id)) }
                                    }
                                }
                                if (series.isNotEmpty()) {
                                    item(key = "series-header") {
                                        UpcomingHeading("Длительные старты", Modifier.padding(top = 8.dp))
                                    }
                                    items(series, key = { it.order.id }) { registration ->
                                        RegistrationRow(
                                            registration,
                                            ongoing = registration.isPaid && registration.isInProgress(state.now),
                                            showPaymentStatus = !registration.isPaid,
                                        ) {
                                            onEvent(Intent.OpenRegistration(registration.order.id))
                                        }
                                    }
                                }
                            }
                            is Content.Failed -> Unit
                        }
                        if (content is Content.Ready || content == Content.Guest) {
                            item(key = "quick-links") {
                                StartShortcuts(
                                    onBrowse = { onEvent(Intent.Browse) },
                                    onHistory = { onEvent(Intent.OpenHistory) },
                                    onPhotos = { onEvent(Intent.OpenToolbar(ToolbarDestination.PhotoArchive)) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NextStartCard(
    registration: UpcomingRegistration,
    ongoing: Boolean,
    countdown: String,
    modifier: Modifier,
    onCoverColor: (Color) -> Unit,
    onClick: () -> Unit,
) {
    val colors = SauceTheme.colors
    val order = registration.order
    Surface(onClick = onClick, shape = SauceShapes.card, color = colors.surface, modifier = modifier.fillMaxWidth()) {
        Column {
            Box(Modifier.fillMaxWidth()) {
                StartImage(order.image, Modifier.matchParentSize(), onCoverColor)
                Box(Modifier.matchParentSize().background(Brush.verticalGradient(
                    listOf(Color.Black.copy(alpha = 0.55f), Color.Black.copy(alpha = 0.25f), Color.Black),
                )))
                Column(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 188.dp).padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    SauceStatusBadge("Оплачено", tone = SauceStatusTone.Success)
                    Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(if (ongoing) "Идёт сейчас" else "Ближайший старт",
                            color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.labelLarge)
                        Text(
                            order.name,
                            modifier = Modifier.semantics { heading() },
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.CalendarToday, contentDescription = null, tint = colors.action, modifier = Modifier.size(18.dp))
                    Text(order.dateStartPreview, style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary)
                }
                if (registration.distances.isNotEmpty()) {
                    Text(registration.distances.joinToString(" · "), style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                }
            }
            TicketDivider()
            Row(
                Modifier.fillMaxWidth().background(colors.actionSoft).padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(if (ongoing) "До завершения" else "До старта",
                        style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
                    AnimatedContent(
                        targetState = countdown,
                        transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(100)) },
                        label = "Start countdown",
                    ) { time ->
                        Text(time, color = colors.action, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
                Text("Регистрация", color = colors.action, style = MaterialTheme.typography.labelLarge)
                ForwardIcon()
            }
        }
    }
}

@Composable
private fun RegistrationRow(
    registration: UpcomingRegistration,
    ongoing: Boolean = false,
    showPaymentStatus: Boolean = false,
    onClick: () -> Unit,
) {
    val order = registration.order
    val colors = SauceTheme.colors
    Surface(
        onClick = onClick,
        shape = SauceShapes.card,
        color = if (registration.isPaid) colors.surface else colors.surfaceSubtle,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(SauceSpacing.medium),
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StartImage(
                order.image,
                Modifier.size(width = 52.dp, height = 64.dp)
                    .clip(SauceShapes.field)
                    .border(1.dp, colors.border, SauceShapes.field),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                Text(order.name, style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
                Text(order.dateStartPreview, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                if (ongoing) {
                    Text("Идёт сейчас", style = MaterialTheme.typography.labelMedium, color = colors.success.content)
                }
                if (showPaymentStatus) {
                    Text("Ожидает оплаты", style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
                }
                if (registration.isPaid && registration.distances.isNotEmpty()) {
                    Text(registration.distances.joinToString(" · "), style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
                }
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = if (registration.isPaid) colors.action else colors.textMuted,
            )
        }
    }
}

@Composable
private fun StartImage(url: String, modifier: Modifier, onCoverColor: ((Color) -> Unit)? = null) {
    val fallback = DefaultImages.EmptyImageStart()
    val imageUrl = url.takeIf(String::isNotBlank)
    val context = LocalPlatformContext.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val model = if (onCoverColor != null) {
        remember(context, imageUrl) {
            ImageRequest.Builder(context).data(imageUrl).crossfade(true).enableImageColorSampling().build()
        }
    } else {
        imageRequestCrossfade(imageUrl)
    }
    AsyncImage(
        modifier = modifier.background(SauceTheme.colors.actionSoft),
        model = model,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        fallback = fallback,
        error = fallback,
        onSuccess = { state ->
            if (onCoverColor != null && state.result.request.data == imageUrl) {
                onCoverColor(state.painter.dominantImageColor(density, layoutDirection))
            }
        },
    )
}

@Composable
private fun NoUpcomingStarts(guest: Boolean, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    Surface(shape = SauceShapes.card, color = colors.surface, modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.fillMaxWidth().height(104.dp).background(Brush.verticalGradient(listOf(colors.actionSoft, colors.surface))),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(Modifier.matchParentSize()) {
                    rotate(-13f) {
                        repeat(4) { lane ->
                            val inset = lane * 9.dp.toPx()
                            drawOval(
                                color = colors.action.copy(alpha = 0.12f),
                                topLeft = Offset(size.width * 0.14f + inset, -4.dp.toPx() + inset),
                                size = Size((size.width * 0.72f - inset * 2).coerceAtLeast(0f), size.height - inset * 2),
                                style = Stroke(width = 1.dp.toPx()),
                            )
                        }
                    }
                }
                Surface(shape = CircleShape, color = colors.surface, modifier = Modifier.size(68.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null, tint = colors.action, modifier = Modifier.size(36.dp))
                    }
                }
            }
            Text(
                if (guest) "Ваши предстоящие старты" else "Пока нет предстоящих стартов",
                Modifier.padding(horizontal = 16.dp).semantics { heading() },
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                if (guest) "Войдите, чтобы увидеть свои регистрации и время до ближайшего старта."
                else "Здесь появятся ваши регистрации на будущие старты.",
                Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp),
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            if (guest) {
                SauceButton("Войти", onClick, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp))
            }
        }
    }
}

@Composable
private fun StartShortcuts(onBrowse: () -> Unit, onHistory: () -> Unit, onPhotos: () -> Unit) {
    val colors = SauceTheme.colors
    val rowPadding = PaddingValues(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium)
    val panelBorder = BorderStroke(1.dp, colors.border)
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = SauceSpacing.small),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.small),
    ) {
        UpcomingHeading("Календарь и архив")
        StartCalendarShortcut(onBrowse)
        Surface(shape = SauceShapes.card, color = colors.surface, border = panelBorder, modifier = Modifier.fillMaxWidth()) {
            Column {
                SauceActionRow(
                    title = "Все регистрации",
                    onClick = onHistory,
                    modifier = Modifier.heightIn(min = SauceSizes.fieldMinHeight),
                    icon = Icons.Outlined.History,
                    contentPadding = rowPadding,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        start = SauceSpacing.large + SauceSizes.icon + SauceSpacing.medium,
                        end = SauceSpacing.large,
                    ),
                    color = colors.border,
                )
                SauceActionRow(
                    title = "Фотоархив",
                    onClick = onPhotos,
                    modifier = Modifier.heightIn(min = SauceSizes.fieldMinHeight),
                    icon = Icons.Outlined.PhotoLibrary,
                    contentPadding = rowPadding,
                )
            }
        }
    }
}

@Composable
private fun StartCalendarShortcut(onClick: () -> Unit) {
    val colors = SauceTheme.colors
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = SauceShapes.card,
        color = colors.actionSoft,
        contentColor = colors.onActionSoft,
    ) {
        BoxWithConstraints {
            val showCover = maxWidth >= 320.dp * LocalDensity.current.fontScale
            val coverPadding = if (showCover) maxWidth * 0.25f else 0.dp
            if (showCover) {
                Box(Modifier.matchParentSize()) {
                    Box(Modifier.align(Alignment.CenterEnd).fillMaxWidth(0.44f).fillMaxHeight()) {
                        Image(
                            DefaultImages.EmptyImageStart(),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop,
                        )
                        Box(Modifier.matchParentSize().background(Brush.horizontalGradient(
                            0f to colors.actionSoft,
                            0.55f to colors.actionSoft.copy(alpha = 0.25f),
                            1f to colors.actionSoft.copy(alpha = 0.1f),
                        )))
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp).padding(SauceSpacing.large),
                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.CalendarToday, contentDescription = null, modifier = Modifier.size(SauceSizes.icon))
                Text(
                    "Смотреть старты",
                    modifier = Modifier.weight(1f).padding(end = coverPadding),
                    style = MaterialTheme.typography.titleSmall,
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(SauceSizes.icon).background(colors.actionSoft, CircleShape),
                )
            }
        }
    }
}

@Composable
private fun UpcomingStartsPlaceholder() {
    val colors = SauceTheme.colors
    Column(Modifier.fillMaxWidth().clip(SauceShapes.card).background(colors.surface).shimmer()) {
        Box(Modifier.fillMaxWidth().height(188.dp).background(colors.actionSoft))
        Box(Modifier.padding(16.dp).fillMaxWidth(0.45f).height(18.dp).background(colors.neutral.container, SauceShapes.badge))
        TicketDivider()
        Box(Modifier.padding(16.dp).fillMaxWidth(0.3f).height(28.dp).background(colors.actionSoft, SauceShapes.badge))
    }
}

@Composable
private fun TicketDivider() {
    val color = SauceTheme.colors.border
    Canvas(Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(color, Offset(0f, size.height / 2), Offset(size.width, size.height / 2),
            strokeWidth = size.height, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 4.dp.toPx())))
    }
}

@Composable
private fun UpcomingHeading(title: String, modifier: Modifier = Modifier) {
    Text(title, modifier.semantics { heading() }, color = SauceTheme.colors.textPrimary, style = MaterialTheme.typography.titleSmall)
}

@Composable
private fun ForwardIcon() {
    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = SauceTheme.colors.action)
}
