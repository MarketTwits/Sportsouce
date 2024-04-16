package com.markettwits.start.presentation.start.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import de.charlex.compose.material3.HtmlText


@Composable
fun StartDescription(modifier: Modifier, description: String) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = if (expanded) description else description.take(150)

    Column(modifier) {
        val textColor = MaterialTheme.colorScheme.tertiary
        HtmlText(
            text = displayText,
            fontSize = 12.sp,
            fontFamily = FontNunito.medium(),
            lineHeight = 12.sp,
            color = textColor,
            colorMapping = mapOf(
                Pair(Color.Black, textColor),
                Pair(Color.Gray, textColor),
                Pair(Color.LightGray, textColor)
            )
        )

        AnimatedVisibility(
            visible = description.length > 150,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(Shapes.medium)
                        .clickable { expanded = !expanded },
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(10.dp),
                        text = if (expanded) "Скрыть" else "Подробнее",
                        fontFamily = FontNunito.bold(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }

        }
    }
}