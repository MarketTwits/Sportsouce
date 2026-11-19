package com.markettwits.sportsouce.start.presentation.membres.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.start.presentation.common.components.StartListRowShimmer

@Composable
fun StartMemberCardShimmer(modifier: Modifier = Modifier) {
    StartListRowShimmer(modifier, result = false)
}
