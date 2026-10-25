package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun StartInsurancePanel(
    modifier: Modifier = Modifier,
    onClickUrl: (String) -> Unit,
) {
    StartServiceCard(
        modifier = modifier,
        title = "Спортивная страховка",
        description = "Полис для соревнований и сборов. От 1 дня до 1 года.",
        action = "Оформить страховку",
        icon = Icons.Outlined.VerifiedUser,
        onClick = { onClickUrl("https://sportsauce.ru/insurance") },
    )
}
