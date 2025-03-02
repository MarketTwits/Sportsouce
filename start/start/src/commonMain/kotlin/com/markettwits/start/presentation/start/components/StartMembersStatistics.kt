package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun StartMembersStatistics(
    modifier: Modifier = Modifier,
    membersUi: List<StartMembersUi>
) {
    if (membersUi.isNotEmpty()) {
        StartContentBasePanel(
            modifier = modifier,
            label = "Регистрации по дистанциям",
        ) {
            val distances = membersUi.mapToRegistrationDistance()
            Column(modifier = modifier) {
                ColorPalette(members = distances)
                Spacer(Modifier.padding(4.dp))
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    distances.forEach { member ->
                        BarChartItem(
                            modifier = Modifier.padding(4.dp),
                            member = member
                        )
                    }
                }

            }
        }
    }
}


@Composable
private fun ColorPalette(
    modifier: Modifier = Modifier,
    members: List<StartRegistrationDistanceStatistics>
) {
    Row(
        modifier = modifier
            .border(0.3.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), shape = Shapes.small)
            .fillMaxWidth()
            .clip(Shapes.small),
        horizontalArrangement = Arrangement.End
    ) {
        members.forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(it.color)
                    .weight(it.count.toFloat())
            )
        }
    }
}

@Composable
private fun BarChartItem(
    modifier: Modifier = Modifier,
    member: StartRegistrationDistanceStatistics,
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .border(0.2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), shape = Shapes.small)
                .clip(Shapes.small)
                .size(20.dp)
                .background(member.color)
        )
        Text(
            text = member.distance,
            color = MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.semiBoldBold(),
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
        Text(
            text = member.count.toString(),
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
private fun List<StartMembersUi>.mapToRegistrationDistance(): List<StartRegistrationDistanceStatistics> {
    val baseColor = MaterialTheme.colorScheme.secondary
    val availableColors = List(10) { index ->
        val factor = index / 9f
        lerp(Color.White, baseColor, factor)
    }.shuffled()

    return this
        .groupBy { it.distance }
        .toList()
        .mapIndexed { index, pair ->
            StartRegistrationDistanceStatistics(
                count = pair.second.size,
                distance = pair.first,
                color = availableColors.getOrNull(index) ?: baseColor
            )
        }
}

private fun Color.Companion.random(): Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue)
}

@Stable
@Immutable
private data class StartRegistrationDistanceStatistics(
    val count: Int,
    val distance: String,
    val color: Color
)

