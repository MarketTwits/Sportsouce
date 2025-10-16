package com.markettwits.sportsouce.club.registration.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.sportsouce.club.registration.domain.RegistrationType

@Composable
fun WorkoutRegistrationType(
    modifier: Modifier = Modifier,
    registrationType: RegistrationType,
) {
    val text = when (registrationType) {
        is RegistrationType.Empty -> ""
        is RegistrationType.Schedule -> registrationType.scheduleName
        is RegistrationType.Subscription -> registrationType.workoutName
        is RegistrationType.Trainer -> registrationType.trainerName
        is RegistrationType.Workout -> registrationType.workoutName
    }
    FilterChipBase(
        modifier = modifier,
        enabled = true,
        selected = true,
        label = text,
        onClick = {}
    )
}