package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMemberResult
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderInfoLine
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderSectionHeading

@Composable
fun MemberResultsCard(result: StartOrderMemberResult, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        OrderSectionHeading(result.distance.takeIf(String::isNotBlank) ?: "Результат")
        listOf(
            "Стартовый номер" to result.bodyNumber,
            "Место" to result.place.toString(),
            "Время" to result.result,
            "Отставание от лидера" to result.shift,
        ).forEach { (label, value) ->
            if (value.isNotBlank()) OrderInfoLine(label, value)
        }
        if (result.circles.isNotEmpty()) {
            OrderSectionHeading("Круги")
            result.circles.forEach { (number, time) -> OrderInfoLine("Круг ${number + 1}", time) }
        }
    }
}
