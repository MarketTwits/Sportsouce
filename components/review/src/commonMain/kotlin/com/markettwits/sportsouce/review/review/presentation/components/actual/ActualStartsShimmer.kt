package com.markettwits.sportsouce.review.review.presentation.components.actual

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.theme.SauceShapes

@Composable
internal fun ActualStartsShimmer() {
    val textHeight = with(LocalDensity.current) { 20.sp.toDp() }
    Column(Modifier.fillMaxWidth().semantics {
        contentDescription = "Загрузка актуальных стартов"
        progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
    }) {
        SauceSkeleton(Modifier.padding(horizontal = 10.dp).width(132.dp).height(textHeight))
        BoxWithConstraints(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp)) {
            val gap = 12.dp
            val columns = ((maxWidth + gap) / (320.dp + gap)).toInt().coerceIn(1, 3)
            val cardWidth = (maxWidth - gap * (columns - 1)) / columns
            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                repeat(columns) {
                    Column(Modifier.width(cardWidth).padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SauceSkeleton(Modifier.fillMaxWidth().aspectRatio(2f), SauceShapes.field)
                        SauceSkeleton(Modifier.fillMaxWidth(0.8f).height(textHeight))
                        SauceSkeleton(Modifier.fillMaxWidth(0.5f).height(textHeight * 0.7f))
                        SauceSkeleton(Modifier.fillMaxWidth(0.65f).height(textHeight * 0.7f))
                    }
                }
            }
        }
    }
}
