package com.markettwits.start.presentation.start.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.base_extensions.openWebPage
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.common.Animation

@Composable
fun StartConditionPanel(modifier: Modifier = Modifier, file: StartItem.ConditionFile) {
    var panelState by rememberSaveable {
        mutableStateOf(true)
    }
    when(file){
        is StartItem.ConditionFile.Base -> {
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
                    text = "Положение",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.bold,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (!panelState) Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            AnimatedVisibility(
                visible = panelState,
                enter = Animation.enterAnimation(Animation.DEFAULT_TOP_BAR_ANIMATION_DURATION),
                exit = Animation.exitAnimation(Animation.DEFAULT_TOP_BAR_ANIMATION_DURATION),
            ) {
                StartConditionPanelContent(modifier, file)
            }
        }

        is StartItem.ConditionFile.Empty -> {}
    }
}

@Composable
private fun StartConditionPanelContent(modifier: Modifier = Modifier, file: StartItem.ConditionFile.Base) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable {
                openWebPage(file.url, context)
            }
            .fillMaxWidth()
    ){
        Text(
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.Center),
            text = "Положение",
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = FontNunito.bold,
            fontSize = 14.sp
        )
    }
}