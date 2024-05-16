package com.markettwits.club.info.presentation.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.club.info.presentation.component.ClubInfoComponent
import com.markettwits.club.info.presentation.components.pager.ClubInfoPager
import com.markettwits.club.info.presentation.store.ClubInfoStore
import com.markettwits.core_ui.items.bottom_sheet.ClosableDragHandle
import com.markettwits.core_ui.items.bottom_sheet.DefaultModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubInfoScreen(
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
                modifier = Modifier.animateContentSize(animationSpec = tween(easing = FastOutSlowInEasing)),
                startIndex = state.currentIndex,
                clubInfo = state.info
            )
        }
    }
}