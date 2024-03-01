package com.markettwits.start.presentation.start.components

import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito


@Composable
fun StartDescription(modifier: Modifier, description: String) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = if (expanded) description else description.take(150)

    Column(modifier) {
        val textColor = MaterialTheme.colorScheme.tertiary

        AndroidView(
            modifier = Modifier.animateContentSize(),
            factory = { context -> TextView(context) },
            update = {
                it.text = HtmlCompat.fromHtml(displayText, HtmlCompat.FROM_HTML_MODE_COMPACT)
                it.setTextColor(textColor.toArgb())
                it.maxLines =
                    Int.MAX_VALUE  // Always show full text, AnimatedVisibility will handle visibility
                it.textSize = 12f
            }
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
                        fontFamily = FontNunito.bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }

        }
    }
}
//    var lines by remember {
//        mutableIntStateOf(5)
//    }
//    Column(modifier) {
//        val textColor = MaterialTheme.colorScheme.tertiary
//
//        AndroidView(
//            modifier = modifier,
//            factory = { context -> TextView(context) },
//            update = {
//                it.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
//                it.setTextColor(textColor.toArgb())
//                it.maxLines = lines
//                it.textSize = 12f
//            }
//        )
//
//        if (description.length > 150){
//            Box(modifier = Modifier
//                .padding(10.dp)
//                .clip(Shapes.medium)
//                .align(Alignment.CenterHorizontally)
//                .clickable {
//                    lines = if (lines == 5) Int.MAX_VALUE else 5
//                },){
//                Text(
//                    modifier = Modifier.align(Alignment.Center),
//                    text = "Подробнее",
//                    fontFamily = FontNunito.bold,
//                    fontSize = 14.sp,
//                    color = MaterialTheme.colorScheme.tertiary,
//                )
//            }
//        }
//    }
