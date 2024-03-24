package com.markettwits.schedule.schedule.presentation.components.list.common.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CalendarSimpleTitle(
    modifier: Modifier,
    title: String,
    eventsCount: Int,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
    ) {
        CalendarNavigationIcon(
            modifier = Modifier.align(Alignment.CenterStart),
            icon = Icons.Default.ArrowBackIosNew,
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = title,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )
            Text(
                modifier = Modifier,
                text = "$eventsCount стартов",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )
        }
        CalendarNavigationIcon(
            modifier = Modifier.align(Alignment.CenterEnd),
            icon = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    tint: Color = MaterialTheme.colorScheme.outline,
    onClick: () -> Unit,
) {
    IconButton(modifier = modifier.padding(4.dp),
        onClick = { onClick() }) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = icon,
            tint = tint,
            contentDescription = contentDescription,
        )
    }
}

