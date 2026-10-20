package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.start.presentation.common.components.StartListRowShimmer

@Composable
fun ResultCardShimmer(modifier: Modifier = Modifier) {
    StartListRowShimmer(modifier, result = true)
}
