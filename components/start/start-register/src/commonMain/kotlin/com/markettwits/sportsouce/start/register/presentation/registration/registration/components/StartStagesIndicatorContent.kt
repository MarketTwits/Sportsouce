package com.markettwits.sportsouce.start.register.presentation.registration.registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationInfo
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import kotlinx.serialization.Serializable

@Composable
internal fun StartStagesIndicatorContent(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    startRegistrationStage: List<StartRegistrationStagePage>,
) {
    if (startRegistrationStage.isEmpty()) return
    // Page IDs are persisted navigation identities and need not equal their list position.
    val active = startRegistrationStage.indexOfFirst { it.id == currentIndex }
    if (active < 0) return
    val fontScale = LocalDensity.current.fontScale
    BoxWithConstraints(modifier.fillMaxWidth().semantics {
        progressBarRangeInfo = ProgressBarRangeInfo((active + 1).toFloat(), 0f..startRegistrationStage.size.toFloat())
        stateDescription = "Шаг ${active + 1} из ${startRegistrationStage.size}"
    }) {
        val vertical = maxWidth / startRegistrationStage.size < 144.dp * fontScale
        if (vertical) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                startRegistrationStage.forEachIndexed { position, stage ->
                    StageIndicatorItem(Modifier.fillMaxWidth(), position, stage, active, vertical = true)
                }
            }
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
                verticalAlignment = Alignment.Top) {
                startRegistrationStage.forEachIndexed { position, stage ->
                    StageIndicatorItem(Modifier.weight(1f), position, stage, active, vertical = false)
                }
            }
        }
    }
}

@Composable
private fun StageIndicatorItem(
    modifier: Modifier,
    position: Int,
    stage: StartRegistrationStagePage,
    active: Int,
    vertical: Boolean,
) {
    val colors = SauceTheme.colors
    val reached = position <= active
    val marker: @Composable () -> Unit = {
        Box(Modifier.width(40.dp).heightIn(min = 32.dp).clip(CircleShape)
            .background(if (reached) colors.action else colors.surfaceSubtle).padding(6.dp),
            contentAlignment = Alignment.Center) {
            Text((position + 1).toString(), Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelLarge,
                color = if (reached) colors.onAction else colors.textSecondary)
        }
    }
    val labels: @Composable (Modifier) -> Unit = { labelModifier ->
        Column(labelModifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
            Text(stage.title, Modifier.fillMaxWidth(), style = MaterialTheme.typography.labelLarge,
                textAlign = if (vertical) TextAlign.Start else TextAlign.Center, color = colors.textPrimary)
            if (stage.description.isNotBlank()) Text(stage.description, Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall, color = colors.textSecondary,
                textAlign = if (vertical) TextAlign.Start else TextAlign.Center)
        }
    }
    val semantics = modifier.semantics(mergeDescendants = true) {
        stateDescription = when {
            position < active -> "Завершён"
            position == active -> "Текущий шаг"
            else -> "Предстоящий шаг"
        }
    }
    if (vertical) {
        Row(semantics, verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            marker()
            labels(Modifier.weight(1f))
        }
    } else {
        Column(semantics, horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            marker()
            labels(Modifier.fillMaxWidth())
        }
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
        val distance: StartRegistrationDistance,
        val invalidStageIds: Set<Int> = emptySet(),
        val validationAttemptTick: Int = 0,
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
