package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import kotlin.random.Random

@Composable
internal fun StartMembersStatistics(
    modifier: Modifier = Modifier,
    membersUi: List<StartMembersUi>
) {
    if (membersUi.isNotEmpty()) {
        StartContentBasePanel(
            modifier = modifier,
            label = "Регистрации по дистанциям",
            openByDefault = true
        ) {
            val distances = membersUi.mapToRegistrationDistance()
            Column(modifier = modifier) {
                ColorPalette(members = distances)
                Spacer(Modifier.padding(4.dp))
                distances.forEach { members ->
                    BarChartItem(member = members)
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
            ) {}
        }
    }
}

@Composable
private fun BarChartItem(
    modifier: Modifier = Modifier,
    member: StartRegistrationDistanceStatistics,
) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(Shapes.small)
                .size(20.dp)
                .background(member.color)
        )
        Text(
            text = member.distance,
            color = MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.semiBoldBold(),
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 6.dp)
        )
        Text(
            text = member.count.toString(),
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
private fun List<StartMembersUi>.mapToRegistrationDistance(): List<StartRegistrationDistanceStatistics> {
    val b = this.groupBy { it.distance }.toList().mapIndexed { index, pair ->
        StartRegistrationDistanceStatistics(
            count = pair.second.size,
            distance = pair.first,
            color = mapColor(index)
        )
    }
    return b
}

@Composable
private fun mapColor(index: Int): Color =
    when (index) {
        0 -> SportSouceColor.SportSouceLighBlue
        1 -> SportSouceColor.SportSouceBlue
        2 -> SportSouceColor.SportSouceStartEndedPink
        3 -> SportSouceColor.OnSecondaryContainer
        4 -> SportSouceColor.VkIcon
        5 -> SportSouceColor.WhatsappIcon
        6 -> SportSouceColor.SportSouceRegistryCommingSoonYellow
        7 -> SportSouceColor.SportSouceLightRed
        8 -> SportSouceColor.VeryLighBlue
        9 -> SportSouceColor.TelegramIcon.copy(0.3f)
        10 -> Color.Gray
        11 -> SportSouceColor.SportSouceStartEndedPink.copy(0.3f)
        else -> Color.random()
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

