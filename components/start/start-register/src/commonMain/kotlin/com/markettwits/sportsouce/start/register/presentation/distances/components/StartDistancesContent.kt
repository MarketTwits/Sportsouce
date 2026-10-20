package com.markettwits.sportsouce.start.register.presentation.distances.components

import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.cloud.model.start.fields.Distance
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesInput

@Composable
internal fun StartDistancesContent(
    modifier: Modifier = Modifier,
    state: StartDistancesInput,
    onClickGoBack: () -> Unit,
    onClickSelectedDistance: (DistinctDistance) -> Unit,
    onClickUrl: (String) -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .background(color = SauceTheme.colors.background),
        topBar = {
            TopBarBase(
                title = state.startTitle,
                goBack = onClickGoBack,
            )
        },
    ) { paddingValues ->
        AdaptivePane(modifier = Modifier.padding(paddingValues).consumeWindowInsets(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(14.dp))
                InfoCard()
                Spacer(modifier = Modifier.height(14.dp))
                state.distance.forEach { distance ->
                    androidx.compose.runtime.key(distance.id) {
                        DistanceItem(
                            item = distance,
                            distinctDistances = state.distance,
                            distances = state.mapDistance,
                            paymentType = state.paymentType,
                            paymentDisabled = state.paymentDisabled,
                            onClick = { onClickSelectedDistance(distance) },
                            onClickUrl = onClickUrl
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard() {

    var isShowMessage by rememberSaveable { mutableStateOf(true) }

    AnimatedVisibility(isShowMessage) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SauceTheme.colors.surfaceRaised),
            border = BorderStroke(1.dp, SauceTheme.colors.border)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Выберите желаемую дистанцию для регистрации на старт. Внимательно читайте положение и регламент проведения мероприятий!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SauceTheme.colors.textSecondary
                    )
                }
                IconButton(onClick = {
                    isShowMessage = false
                }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Скрыть подсказку",
                        tint = SauceTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DistanceItem(
    item: DistinctDistance,
    distinctDistances: List<DistinctDistance>,
    distances: List<Distance>,
    paymentDisabled: Boolean,
    paymentType: String,
    onClick: () -> Unit,
    onClickUrl: (String) -> Unit = {},
) {

    val title = getDistanceTitle(
        distances = distances,
        distinctDistances = distinctDistances,
        selectedDistance = item
    )

    val availability = item.availability()
    val enabled = availability.canSelect

    val hasDescription = item.detailedDescription.hasMeaningfulDescription()
    val hasScheme = item.schemeImage?.fullPath?.isNotBlank() == true
    val hasTrack = item.trackLink?.fullPath?.isNotBlank() == true
    val hasAdditionalInfo = hasDescription || hasScheme || hasTrack

    var isExpanded by rememberSaveable(item.id) { mutableStateOf(false) }

    OnBackgroundCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = SauceTheme.colors.surfaceRaised),
        shape = RoundedCornerShape(8.dp),
        onClick = if (enabled) onClick else null,
    ) {
        Column {
            Column(
                Modifier.fillMaxWidth().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SauceStatusBadge(label = availability.label, tone = availability.tone,
                    modifier = Modifier.heightIn(min = 26.dp))
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(title, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium,
                        color = SauceTheme.colors.textPrimary)
                    if (item.stages.size > 1) Text("Этапов: ${item.stages.size}", Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                }
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.width(IntrinsicSize.Max), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(if (paymentDisabled && paymentType.isNotEmpty()) "Оплата" else "Цена",
                            Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall,
                            color = SauceTheme.colors.textSecondary)
                        Text(if (paymentDisabled && paymentType.isNotEmpty()) paymentType
                            else "${item.staticPrice.formatPrice()} ₽",
                            Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium,
                            color = if (enabled) SauceTheme.colors.textPrimary else SauceTheme.colors.textMuted)
                    }
                    SauceButton(
                        title = if (enabled) "Выбрать" else "Мест нет",
                        enabled = enabled,
                        fillWidth = false,
                        onClick = onClick,
                    )
                }
            }

            // Additional info section toggle button
            if (hasAdditionalInfo) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = SauceTheme.colors.border
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .semantics { stateDescription = if (isExpanded) "Развёрнуто" else "Свёрнуто" }
                        .clickable(role = Role.Button, onClickLabel = if (isExpanded) "Скрыть детали" else "Показать детали") { isExpanded = !isExpanded }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = if (isExpanded) "Скрыть детали" else "Показать детали",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.medium(),
                        color = SauceTheme.colors.action
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = SauceTheme.colors.action,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(if (isExpanded) 180f else 0f)
                    )
                }

                // Expandable additional info section
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(300)) +
                            expandVertically(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) +
                            shrinkVertically(animationSpec = tween(300))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Detailed description
                        if (hasDescription) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Описание",
                                    fontFamily = FontNunito.bold(),
                                    fontSize = 12.sp,
                                    color = SauceTheme.colors.textSecondary
                                )
                                HtmlText(
                                    text = item.detailedDescription.orEmpty(),
                                    fontFamily = FontNunito.regular(),
                                    fontSize = 13.sp,
                                    color = SauceTheme.colors.textPrimary
                                )
                            }
                        }

                        // Scheme image file
                        item.schemeImage?.takeIf { it.fullPath.isNotBlank() }?.let { file ->
                            DistanceFileCard(
                                title = "Схема трассы",
                                fileName = file.name,
                                onClick = { onClickUrl(file.fullPath) }
                            )
                        }

                        // Track link file
                        item.trackLink?.takeIf { it.fullPath.isNotBlank() }?.let { file ->
                            DistanceFileCard(
                                title = "Файл трека",
                                fileName = file.name,
                                onClick = { onClickUrl(file.fullPath) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun String?.hasMeaningfulDescription(): Boolean {
    if (this.isNullOrBlank()) return false
    return this
        .replace(Regex("<[^>]*>"), "")
        .replace("&nbsp;", " ")
        .trim()
        .isNotEmpty()
}

@Composable
private fun DistanceFileCard(
    title: String,
    fileName: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = SauceTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 12.sp,
                    color = SauceTheme.colors.textSecondary
                )
                Text(
                    text = fileName,
                    fontFamily = FontNunito.medium(),
                    fontSize = 13.sp,
                    color = SauceTheme.colors.textPrimary
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = SauceTheme.colors.action,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(-90f) // Point to the right
            )
        }
    }
}

private fun getDistanceTitle(
    distances: List<Distance>,
    distinctDistances: List<DistinctDistance>,
    selectedDistance: DistinctDistance,
): String {
    val matchingDistance = distances.find { it.id == selectedDistance.id }

    return if (matchingDistance != null && !matchingDistance.combo.isNullOrEmpty()) {
        val comboNames = matchingDistance.combo!!.mapNotNull { comboId ->
            distinctDistances.find { it.id == comboId }?.name
        }
        selectedDistance.name + " ( ${comboNames.joinToString(separator = " + ")} )"
    } else {
        selectedDistance.name
    }
}
