package com.markettwits.profile.presentation.component.authorized.presentation.components.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceTheme
import kotlinx.coroutines.launch

private val listPreview = listOf(
    BarchartInput(0, "09.2024", Color.Blue),
    BarchartInput(0, "10.2024", Color.Blue),
    BarchartInput(0, "11.2024", Color.Blue),
    BarchartInput(0, "12.2024", Color.Blue),
    BarchartInput(0, "12.2024", Color.Blue),
    BarchartInput(0, "12.2024", Color.Blue),
    BarchartInput(0, "12.2024", Color.Blue),
    BarchartInput(20, "12.2024", Color.Blue),
)

@Preview
@Composable
private fun UserStartStatisticPreview() {
    SportSouceTheme {
        UserStartStatisticContent(items = listPreview) {}
    }
}

@Composable
internal fun UserStartStatisticContent(
    modifier: Modifier = Modifier,
    items: List<BarchartInput>,
    onClickInfo: () -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier.padding(top = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BarChartTitle(onClickInfo = {
                onClickInfo()
            })
            BarChart(
                inputList = items,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ColumnScope.BarChartTitle(modifier: Modifier = Modifier, onClickInfo: () -> Unit) {
    Row(
        modifier = modifier.align(Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Динамика участия в стартах",
            fontFamily = FontNunito.bold,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { onClickInfo() }) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}


@Composable
private fun BarChart(
    inputList: List<BarchartInput>,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    SideEffect {
        coroutineScope.launch {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.horizontalScroll(scrollState)
    ) {
        val maxValue = calculateMaxValue(inputList)
        inputList.forEach { input ->
            val top = calculateColumnHeight(input, maxValue)
            Bar(
                description = input.description,
                value = input.value.toString(),
                barHeight = top,
                barWidth = 20.dp
            )
        }
    }
}

private fun calculateColumnHeight(input: BarchartInput, maxValue: Int): Dp {
    val minHeight = 45.dp // Minimum height of the column
    val maxHeight = 150.dp // Maximum height of the column

    // Calculate coefficient based on the maximum value
    val coefficient = if (maxValue > 0) maxHeight.value.toFloat() / maxValue.toFloat() else 0f

    // Calculate the height based on the value and coefficient
    val height =
        (input.value * coefficient).coerceIn(minHeight.value.toFloat(), maxHeight.value.toFloat())

    // Return the height as a Dp
    return height.dp
}

fun calculateMaxValue(inputs: List<BarchartInput>): Int {
    return inputs.maxByOrNull { it.value }?.value ?: 0
}

@Composable
private fun Bar(
    modifier: Modifier = Modifier,
    barHeight: Dp,
    barWidth: Dp,
    value: String,
    description: String,
) {
    Box(
        modifier = Modifier
            .height(150.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 14.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                text = value,
                fontFamily = FontNunito.bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
            Box(
                modifier = modifier
                    .height(barHeight)
                    .width(barWidth)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .padding(bottom = 18.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            )
        }
        Text(
            modifier = Modifier
                .rotate(-35f),
            text = description,
            fontFamily = FontNunito.medium,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Immutable
data class BarchartInput(
    val value: Int,
    val description: String,
    val color: Color
)
