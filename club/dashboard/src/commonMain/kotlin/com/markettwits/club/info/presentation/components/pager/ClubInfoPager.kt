package com.markettwits.club.info.presentation.components.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.info.presentation.components.comands.CommandContent
import com.markettwits.club.info.presentation.components.questions.QuestionsContent
import com.markettwits.club.info.presentation.components.questions.mapToQuestionUi
import com.markettwits.club.info.presentation.components.statistics.StatisticsContent
import com.markettwits.club.info.presentation.components.trainings.TrainingsContent
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun ClubInfoPager(
    modifier: Modifier = Modifier,
    startIndex: Int,
    clubInfo: List<ClubInfo>
) {
    val pagerState = rememberPagerState(startIndex, pageCount = { clubInfo.size - 1 })
    val coroutineScope = rememberCoroutineScope()
    HorizontalPager(
        modifier = modifier
            .padding(bottom = 10.dp),
        state = pagerState,
    ) { page ->
        Box(modifier = Modifier.graphicsLayer {
            val pageOffset = (
                    (pagerState.currentPage - page) + pagerState
                        .currentPageOffsetFraction
                    ).absoluteValue
            alpha = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        }) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(bottom = 30.dp)
            ) {
                when (val item = clubInfo[page]) {
                    is ClubInfo.Commands -> CommandContent(trainers = item.trainers)
                    is ClubInfo.Questions -> QuestionsContent(questions = item.questions.mapToQuestionUi())
                    is ClubInfo.Statistics -> StatisticsContent(statistics = item.statistics)
                    is ClubInfo.Trainings -> TrainingsContent(trainings = item.training)
                    is ClubInfo.Schedules -> {}
                }
            }
            ClubInfoPageIndicator(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomCenter),
                pagerState = pagerState,
                onClick = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ClubInfoPageIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)
                    .clickable(onClick = { onClick(iteration) })
            )
        }
    }
}