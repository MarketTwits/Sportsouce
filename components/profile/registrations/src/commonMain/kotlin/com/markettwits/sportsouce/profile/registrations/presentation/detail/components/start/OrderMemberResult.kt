package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMemberResult


@Composable
fun MemberResultsCard(
    result: StartOrderMemberResult,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Результаты",
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        ResultInfoRow("Стартовый номер:", result.bodyNumber)
        ResultInfoRow("Место:", result.place.toString())
        ResultInfoRow("Результат:", result.result)
        ResultInfoRow("Отставание от лидера:", result.shift)

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Круги",
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        result.circles.forEach { (lapNumber, time) ->
            ResultInfoRow("Круг ${lapNumber + 1}:", time)
        }

    }
}

@Composable
private fun ResultInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = FontNunito.bold(),
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = value,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.outline,
        )
    }
}
