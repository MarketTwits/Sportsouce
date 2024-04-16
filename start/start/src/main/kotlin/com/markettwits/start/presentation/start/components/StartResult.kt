package com.markettwits.start.presentation.start.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.common.StartContentBasePanel

@Composable
fun StartResult(
    modifier: Modifier = Modifier,
    results: List<StartItem.Result>,
    title: String
) {
    if (results.isNotEmpty()) {
        StartContentBasePanel(modifier = modifier, label = title) {
            StartResultContent(modifier, results)
        }
    }
}

@Composable
fun StartResultContent(
    modifier: Modifier = Modifier,
    results: List<StartItem.Result>
) {
    val context = LocalContext.current
    Log.d("mt05", "size " + results.size.toString())
    Column(modifier = Modifier.wrapContentSize()) {
        results.forEach {
            Box(
                modifier = modifier
                    .clip(Shapes.medium)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        //TODO open web page
                        //openWebPage(it.url, context)
                    }
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    text = it.name,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = FontNunito.bold(),
                    fontSize = 14.sp
                )
            }
        }
    }
}