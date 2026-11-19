package com.markettwits.sportsouce.club.registration.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.sportsouce.club.registration.presentation.component.WorkoutRegistrationComponent
import com.markettwits.sportsouce.club.registration.presentation.components.WorkoutRegistrationContent
import com.markettwits.sportsouce.club.registration.presentation.components.WorkoutRegistrationSuccessContent

@Composable
fun WorkoutRegistrationScreen(component: WorkoutRegistrationComponent) {
    val state by component.state.collectAsState()
    DefaultModalBottomSheet(
        edgeToEdgeContent = true,
        title = if (state.isSuccess) "Заявка отправлена" else "Запись на тренировку",
        onDismissRequest = {
            component.onClickDismiss()
        }
    ) {
        AnimatedContent(
            targetState = state.isSuccess,
            transitionSpec = {
                fadeIn(tween(durationMillis = 300)) togetherWith
                        fadeOut(tween(durationMillis = 300))
            },
            label = "Content Animation"
        ) { success ->
            if (!success) {
                WorkoutRegistrationContent(
                    form = state.form,
                    isLoading = state.isLoading,
                    errorMessage = if (state.isError) state.message else "",
                    onValueChanged = component::onFormChanged,
                    onClickRegister = component::onClickRegistration,
                    onPhoneClick = component::onClickPhone,
                    onLinkClick = component::onClickLink
                )
            } else {
                WorkoutRegistrationSuccessContent(onClickNext = component::onClickFinish)
            }
        }
    }
}
