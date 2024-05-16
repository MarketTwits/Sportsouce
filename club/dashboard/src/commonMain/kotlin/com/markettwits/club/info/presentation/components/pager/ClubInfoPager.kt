package com.markettwits.club.info.presentation.components.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.info.presentation.components.comands.CommandContent
import com.markettwits.club.info.presentation.components.questions.QuestionsContent
import com.markettwits.club.info.presentation.components.questions.mapToQuestionUi
import com.markettwits.club.info.presentation.components.schedule.ScheduleContent
import com.markettwits.club.info.presentation.components.schedule.mapScheduleToBaseTable
import com.markettwits.club.info.presentation.components.statistics.StatisticsContent
import com.markettwits.club.info.presentation.components.trainings.TrainingsContent


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClubInfoPager(
    modifier: Modifier = Modifier,
    startIndex: Int,
    clubInfo: List<ClubInfo>
) {
    val pagerState = rememberPagerState(startIndex, pageCount = { clubInfo.size })
    Box(modifier = modifier.wrapContentHeight()) {
        HorizontalPager(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pagerState,
        ) { page ->
            when (val item = clubInfo[page]) {
                is ClubInfo.Commands -> CommandContent(trainers = item.trainers)
                is ClubInfo.Questions -> QuestionsContent(questions = item.questions.mapToQuestionUi())
                is ClubInfo.Statistics -> StatisticsContent(statistics = item.statistics)
                is ClubInfo.Trainings -> TrainingsContent(trainings = item.training)
            }
        }
        ClubInfoPageIndicator(
            modifier = Modifier.align(Alignment.BottomCenter),
            pagerState = pagerState
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ClubInfoPageIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState
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
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(6.dp)
            )
        }
    }
}