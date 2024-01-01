package com.markettwits.profile.presentation.component.edit_profile.presentation.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow


interface Event


@Composable
fun <E : Event> OnEvent(event: Flow<E>, onEvent: (E) -> Unit) {
    LaunchedEffect(Unit) {
        event.collect(onEvent)
    }
}