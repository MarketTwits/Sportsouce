package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartResult(
    modifier: Modifier = Modifier,
    results: List<StartItem.Result>,
    title: String,
    onClickResult: (String) -> Unit
) {
    if (results.isNotEmpty()) {
        StartContentBasePanel(modifier = modifier, label = title) {
            StartResultContent(modifier, results, onClickResult)
        }
    }
}

@Composable
internal fun StartResultContent(
    modifier: Modifier = Modifier,
    results: List<StartItem.Result>,
    onClickResult: (String) -> Unit
) {
    Column(modifier = Modifier.wrapContentSize()) {
        results.forEach {
            StartFileContent(
                modifier = modifier,
                onClick = {
                    onClickResult(it.url)
                },
                fileName = it.name
            )
        }
    }
}