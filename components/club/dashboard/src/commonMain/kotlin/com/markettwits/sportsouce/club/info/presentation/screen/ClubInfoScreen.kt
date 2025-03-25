package com.markettwits.sportsouce.club.info.presentation.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.sportsouce.club.info.presentation.component.ClubInfoComponent
import com.markettwits.sportsouce.club.info.presentation.components.pager.ClubInfoPager
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore

@Composable
internal fun ClubInfoScreen(
    modifier: Modifier = Modifier,
    component: ClubInfoComponent
) {
    val state by component.state.collectAsState()
    DefaultModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { component.obtainEvent(ClubInfoStore.Intent.Dismiss) }
    ) {
        if (state.info.isNotEmpty()) {
            ClubInfoPager(
                modifier = Modifier
                    .animateContentSize(animationSpec = tween(easing = FastOutSlowInEasing)),
                startIndex = state.currentIndex,
                clubInfo = state.info
            )
        }else {
            component.obtainEvent(ClubInfoStore.Intent.Dismiss)
        }
    }
}