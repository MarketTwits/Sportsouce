package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.markettwits.start.presentation.common.StartContentBasePanel
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

@Composable
fun StartMembersStatistics(
    modifier: Modifier = Modifier,
    membersUi: ImmutableList<StartMembersUi>
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
fun ColorPalette(
    modifier: Modifier = Modifier,
    members: ImmutableList<StartRegistrationDistanceStatistics>
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
fun BarChartItem(
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
            fontFamily = FontNunito.medium(),
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
private fun ImmutableList<StartMembersUi>.mapToRegistrationDistance(): ImmutableList<StartRegistrationDistanceStatistics> {
    val b = this.groupBy { it.distance }.toList().mapIndexed { index, pair ->
        StartRegistrationDistanceStatistics(
            count = pair.second.size,
            distance = pair.first,
            color = mapColor(index)
        )
    }
    return b.toImmutableList()
}

@Composable
private fun mapColor(index: Int): Color =
    when (index) {
        0 -> SportSouceColor.SportSouceLightRed
        1 -> SportSouceColor.SportSouceStartEndedPink
        2 -> SportSouceColor.SportSouceBlue
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

fun Color.Companion.random(): Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue)
}


@Stable
@Immutable
data class StartRegistrationDistanceStatistics(
    val count: Int,
    val distance: String,
    val color: Color
)

