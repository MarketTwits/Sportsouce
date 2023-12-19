package com.markettwits.start.presentation.start.component

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.openWebPage
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.common.Animation
import com.markettwits.start.presentation.start.StartItemUi

@Composable
fun StartResult(
    modifier: Modifier = Modifier,
    results: List<StartItemUi.StartItemUiSuccess.Result>,
    title : String
) {
    var panelState by rememberSaveable {
        mutableStateOf(true)
    }
    if (results.isNotEmpty()) {
        HorizontalDivider()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    panelState = !panelState
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                color = SportSouceColor.SportSouceBlue,
                fontFamily = FontNunito.bold,
                fontSize = 16.sp
            )
            Icon(
                imageVector = if (!panelState) Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                contentDescription = "",
                tint = SportSouceColor.SportSouceBlue
            )
        }
        AnimatedVisibility(
            visible = panelState,
            enter = Animation.enterAnimation(Animation.DEFAULT_TOP_BAR_ANIMATION_DURATION),
            exit = Animation.exitAnimation(Animation.DEFAULT_TOP_BAR_ANIMATION_DURATION),
        ) {
            StartResultContent(modifier, results)
        }
    }
}

@Composable
fun StartResultContent(
    modifier: Modifier = Modifier,
    results: List<StartItemUi.StartItemUiSuccess.Result>
) {
    val context = LocalContext.current
    results.forEach {
        Box(
            modifier = modifier
                .clip(Shapes.medium)
                .background(SportSouceColor.SportSouceBlue)
                .clickable {
                    openWebPage(it.url, context)
                }
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.Center),
                text = it.name,
                color = Color.White,
                fontFamily = FontNunito.bold,
                fontSize = 14.sp
            )
        }
    }
}