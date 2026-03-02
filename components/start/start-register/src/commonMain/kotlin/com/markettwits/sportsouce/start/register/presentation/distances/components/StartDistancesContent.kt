package com.markettwits.sportsouce.start.register.presentation.distances.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
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
            .background(color = MaterialTheme.colorScheme.background),
        topBar = {
            TopBarBase(
                title = state.startTitle,
                goBack = onClickGoBack,
            )
        },
    ) { paddingValues ->
        AdaptivePane {
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(14.dp))
                InfoCard()
                Spacer(modifier = Modifier.height(14.dp))
                state.distance.forEach {
                    Column(modifier.clip(Shapes.medium)) {
                        DistanceItem(
                            item = it,
                            distinctDistances = state.distance,
                            distances = state.mapDistance,
                            paymentType = state.paymentType,
                            paymentDisabled = state.paymentDisabled,
                            onClick = { onClickSelectedDistance(it) },
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Выберите желаемую дистанцию для регистрации на старт. Внимательно читайте положение и регламент проведения мероприятий!",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.light(),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = {
                    isShowMessage = false
                }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close message",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

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

    val availableSlots = item.availableSlots()
    val enabled = item.infiniteSlots || availableSlots > 0

    val hasDescription = item.detailedDescription.hasMeaningfulDescription()
    val hasScheme = item.schemeImage?.fullPath?.isNotBlank() == true
    val hasTrack = item.trackLink?.fullPath?.isNotBlank() == true
    val hasAdditionalInfo = hasDescription || hasScheme || hasTrack

    var isExpanded by rememberSaveable(item.id) { mutableStateOf(false) }

    OnBackgroundCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            if (enabled) {
                onClick()
            }
        }
    ) {
        Column {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = title,
                            fontSize = 16.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!item.infiniteSlots && availableSlots > 0) {
                        Text(
                            text = "$availableSlots слота",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    } else {
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
                if (item.stages.size > 1) {
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${item.stages.size} этапа",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Button(
                    onClick = {
                        if (enabled) {
                            onClick()
                        }
                    },
                    enabled = enabled,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                ) {
                    if (enabled) {
                        val price =
                            if (paymentDisabled && paymentType.isNotEmpty()) paymentType
                            else
                                "Цена : " + item.staticPrice.formatPrice() + " ₽"
                        Text(
                            text = price,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    } else {
                        Text(
                            text = "Слоты закончились",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }

            // Additional info section toggle button
            if (hasAdditionalInfo) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isExpanded) "Скрыть детали" else "Показать детали",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.medium(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
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
                                    color = MaterialTheme.colorScheme.outline
                                )
                                HtmlText(
                                    text = item.detailedDescription.orEmpty(),
                                    fontFamily = FontNunito.regular(),
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
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

private fun DistinctDistance.availableSlots(): Int {
    val open = openSlots ?: 0
    if (open > 0) return open

    val total = slots
    val taken = takenSlots ?: 0
    return if (total != null) (total - taken).coerceAtLeast(0) else 0
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
            containerColor = MaterialTheme.colorScheme.primary
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
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = fileName,
                    fontFamily = FontNunito.medium(),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
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
