package com.markettwits.start.register.presentation.registration.registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationInfo
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import kotlinx.serialization.Serializable

@Composable
internal fun StartStagesIndicatorContent(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    startRegistrationStage: List<StartRegistrationStagePage>
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        startRegistrationStage.forEachIndexed { index, it ->
            StartStageItem(
                index = (index + 1).toString(),
                title = it.title,
                description = it.description,
                isPasted = currentIndex > index - 1
            )
        }
    }
}

@Composable
internal fun StartStageItem(
    modifier: Modifier = Modifier,
    index: String,
    title: String,
    description: String,
    isPasted: Boolean
) {
    Column(
        modifier = modifier.sizeIn(
            maxWidth = 200.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
                .background(
                    if (isPasted)
                        MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.outline
                )
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = index,
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold(),
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.bold(),
            softWrap = true,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = description,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.semiBoldBold(),
            softWrap = true,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Serializable
sealed interface StartRegistrationStagePage {

    val id: Int

    val title: String

    val description: String

    val isNextPayPage: Boolean

    val isGoBackAvailable: Boolean

    val isGoNextAvailable: Boolean

    @Serializable
    data class Registration(
        override val id: Int,
        override val title: String,
        override val description: String,
        override val isGoBackAvailable: Boolean,
        override val isNextPayPage: Boolean,
        override val isGoNextAvailable: Boolean = true,
        val distance: StartRegistrationDistance
    ) : StartRegistrationStagePage

    @Serializable
    data class Pay(
        override val id: Int,
        override val title: String,
        override val description: String,
        override val isNextPayPage: Boolean = false,
        override val isGoBackAvailable: Boolean = true,
        override val isGoNextAvailable: Boolean = false,
        val price: StartRegistrationPriceResult,
        val startInfo: StartRegistrationInfo,
        val distances: List<StartRegistrationDistance>
    ) : StartRegistrationStagePage

    @Serializable
    data object Empty : StartRegistrationStagePage {
        override val id: Int = 0
        override val title: String = ""
        override val description: String = ""
        override val isNextPayPage: Boolean = false
        override val isGoBackAvailable: Boolean = false
        override val isGoNextAvailable: Boolean = false
    }
}

internal fun List<StartRegistrationDistance>.createStartStagesContent(
    startInfo: StartRegistrationInfo
): List<StartRegistrationStagePage> {
    val items: MutableList<StartRegistrationStagePage> = mapIndexed { index, item ->
        StartRegistrationStagePage.Registration(
            id = index,
            distance = item,
            title = item.title,
            description = item.description,
            isNextPayPage = this.size == index + 1,
            isGoBackAvailable = index != 0
        )
    }.toMutableList()
    val payStage = StartRegistrationStagePage.Pay(
        id = this.size + 1,
        title = "Оплата",
        description = "",
        price = StartRegistrationPriceResult.Empty,
        startInfo = startInfo,
        distances = this
    )
    items.add(payStage)
    return items
}

internal fun List<StartRegistrationStagePage>.getRegistrationsStage(): List<StartRegistrationStagePage.Registration> =
    filterIsInstance<StartRegistrationStagePage.Registration>()

internal fun List<StartRegistrationDistance>.getDistancesId(): List<Int> = flatMap {
    it.stages.map {
        it.stage.distanceId
    }
}