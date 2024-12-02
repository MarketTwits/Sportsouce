package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.core_ui.items.base_extensions.formatPrice
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.common.OnClick
import com.markettwits.start_cloud.model.start.fields.Distance
import com.markettwits.start_cloud.model.start.fields.DistinctDistance

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun StartDistancesNew(
    modifier: Modifier = Modifier,
    distance: List<DistinctDistance>,
    mapDistance : List<Distance>,
    startStatus: StartItem.StartStatus,
    paymentDisabled: Boolean,
    paymentType: String,
    regLink: String,
    onClick: (DistinctDistance) -> Unit,
    onClickUrl: (String) -> Unit
) {
    if (distance.isNotEmpty() && startStatus.code == 3) {
        StartContentBasePanel(modifier = modifier, label = "Дистанции") {
            FlowRow(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                maxItemsInEachRow = 4,
                horizontalArrangement = Arrangement.Start,
                verticalArrangement = Arrangement.Center
            ) {
                distance.forEach {
                    Column(modifier.clip(Shapes.medium)) {
                        DistanceItem(
                            item = it,
                            distinctDistances = distance,
                            distances = mapDistance,
                            paymentType = paymentType,
                            paymentDisabled = paymentDisabled,
                            onClick = { onClick(it) }
                        )
                    }
                }
            }

        }
    }
    if (regLink.isNotEmpty()) {
        StartContentBasePanel(modifier = modifier, label = "Дистанции") {
            ButtonContentBase(
                modifier = modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                title = "Перейти на сайт регистрации",
                onClick = {
                    onClickUrl(regLink)
                }
            )
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
    onClick: OnClick
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = Shapes.medium
            )
            .clip(Shapes.medium)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        ) {

            val title = getDistanceTitle(
                distances = distances,
                distinctDistances = distinctDistances,
                selectedDistance = item
            )

            Text(
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            if (item.description != null){
                Text(
                    text = item.description ?: "",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            HorizontalDivider()
            val infinity = item.infinite_slots
            val slots = item.open_slots
            if (!infinity) {
                Text(
                    text = "Осталось слотов : $slots",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            if (paymentDisabled && paymentType.isNotEmpty()) {
                Text(
                    text = paymentType,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            } else {
                Text(
                    text = "Цена : " + item.static_price.formatPrice() + " ₽",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            val enabled = if (item.infinite_slots) true else if (item.open_slots!! > 0) true else false
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            ) {
                val title = if (enabled) "Зарегистрироваться" else "Слоты закончились"
                val titleColor =
                    if (enabled) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.outline
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    ),
                    onClick = { onClick() },
                ) {
                    Text(title, color = titleColor)
                }
            }
        }
    }
}

private fun getDistanceTitle(
    distances: List<Distance>,
    distinctDistances: List<DistinctDistance>,
    selectedDistance : DistinctDistance,
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

