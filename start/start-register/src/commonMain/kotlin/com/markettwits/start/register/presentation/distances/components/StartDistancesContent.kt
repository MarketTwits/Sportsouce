package com.markettwits.start.register.presentation.distances.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_extensions.formatPrice
import com.markettwits.core_ui.items.base_screen.AdaptivePane
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.presentation.distances.component.StartDistancesInput
import com.markettwits.start.register.presentation.registration.registration.components.StartRegistrationTopBar
import com.markettwits.start_cloud.model.start.fields.Distance
import com.markettwits.start_cloud.model.start.fields.DistinctDistance

@Composable
internal fun StartDistancesContent(
    modifier: Modifier = Modifier,
    state: StartDistancesInput,
    onClickGoBack: () -> Unit,
    onClickSelectedDistance: (DistinctDistance) -> Unit
) {
    Scaffold(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background),
        topBar = {
            StartRegistrationTopBar(
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
                            onClick = { onClickSelectedDistance(it) }
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
) {

    val title = getDistanceTitle(
        distances = distances,
        distinctDistances = distinctDistances,
        selectedDistance = item
    )

    val enabled = if (item.infinite_slots) true else if (item.open_slots!! > 0) true else false

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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                if (!item.infinite_slots && (item.open_slots ?: 0) > 0) {
                    Text(text = "${item.open_slots} слота", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
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
                            "Цена : " + item.static_price.formatPrice() + " ₽"
                    Text(
                        text = price,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                } else {
                    Text(text = "Слоты закончились", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSecondary)
                }
            }
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