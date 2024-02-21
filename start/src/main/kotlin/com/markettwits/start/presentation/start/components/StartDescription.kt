package com.markettwits.start.presentation.start.components

import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.markettwits.core_ui.theme.FontNunito


@Composable
fun StartDescription(modifier: Modifier, description: String) {
    var lines by remember {
        mutableIntStateOf(5)
    }
    Column(modifier) {
        val textColor = MaterialTheme.colorScheme.tertiary
        AndroidView(
            modifier = modifier,
            factory = { context -> TextView(context) },
            update = {
                it.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
                it.setTextColor(textColor.toArgb())
                it.maxLines = lines
                it.textSize = 12f
            }
        )
        if (description.length > 150){
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        lines = if (lines == 5) Int.MAX_VALUE else 5
                    },
                text = "Подробнее",
                fontFamily = FontNunito.bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

    }
}