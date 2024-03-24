package com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.schedule.schedule.presentation.components.list.common.starts_list_info.StartsListInfoItemLabel
import com.markettwits.starts_common.domain.StartsListItem

@Composable
fun KindOfSportsListInfo(modifier: Modifier = Modifier, startsListItem: List<StartsListItem>) {
    var items by remember {
        mutableStateOf(emptyList<KindOfSportsInfo>())
    }
    val defaultColor = MaterialTheme.colorScheme.tertiary
    LaunchedEffect(key1 = startsListItem) {
        items = KindOfSportsInfoMapperBase.map(
            startsListItem = startsListItem,
            defaultColor = defaultColor
        )
    }
    Column(modifier = modifier) {
        items.forEach {
            KindOfSportsInfoItem(
                modifier = Modifier.padding(10.dp),
                count = it.count,
                title = it.title,
                color = it.color,
                icon = it.icon
            )
        }
    }
}

@Composable
private fun KindOfSportsInfoItem(
    modifier: Modifier = Modifier,
    count: Int,
    title: String,
    color: Color,
    icon: ImageVector
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KindOfSportsInfoIcon(
            modifier = Modifier.padding(5.dp),
            icon = icon,
            containerColor = color
        )
        KindOfSportsInfoValue(
            modifier = Modifier,
            count = count,
            title = title,
            color = color
        )
    }
}

@Composable
private fun KindOfSportsInfoValue(modifier: Modifier, count: Int, title: String, color: Color) {
    Column(modifier = modifier) {
        Text(
            modifier = modifier,
            text = title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontNunito.regular,
            fontSize = 14.sp
        )
        Row(
            modifier = modifier
                .clip(Shapes.extraSmall)
                .background(color)
                .height(5.dp)
                .fillMaxWidth()

        ) {}
        StartsListInfoItemLabel(
            title = "Количество",
            value = count.toString(),
            horizontalAlignment = Alignment.Start
        )
    }

}

@Composable
private fun KindOfSportsInfoIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    containerColor: Color
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .clip(RoundedCornerShape(100))
            .aspectRatio(1f)
            .background(containerColor)
    ) {
        Icon(
            modifier = modifier.align(Alignment.Center),
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}