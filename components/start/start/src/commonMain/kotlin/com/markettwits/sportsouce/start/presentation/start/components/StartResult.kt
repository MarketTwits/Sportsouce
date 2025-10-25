package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartResult(
    modifier: Modifier = Modifier,
    results: List<StartItem.Result>,
    title: String,
    onClickResult: (String) -> Unit
) {
    if (results.isNotEmpty()) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }

        StartContentBasePanel(modifier = modifier, label = title) {
            val displayedResults = if (isExpanded) results else results.take(3)

            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                displayedResults.forEach {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                    ) {
                        StartFileContent(
                            modifier = modifier,
                            onClick = {
                                onClickResult(it.url)
                            },
                            fileName = it.name
                        )
                    }
                }

                AnimatedVisibility(
                    visible = results.size > 3,
                    enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                ) {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier
                    ) {
                        Text(
                            text = if (isExpanded) "Свернуть" else "Ещё ${results.size - 3}",
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
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
