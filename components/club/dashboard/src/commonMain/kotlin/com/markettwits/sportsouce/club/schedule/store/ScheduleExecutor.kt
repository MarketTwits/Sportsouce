package com.markettwits.sportsouce.club.schedule.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class ScheduleExecutor(private val repository: ClubRepository) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.OnClickBack -> publish(Label.Dismiss)
            is Intent.OnClickScheduleItem -> {
                val id = intent.scheduleId.toIntOrNull() ?: return
                val schedule = state().allSchedules.firstOrNull { it.id == id && !it.isTempDisabled } ?: return
                publish(Label.Registration(RegistrationType.Schedule(schedule.id, schedule.workoutTitle)))
            }
            is Intent.OnClickWorkoutType -> dispatch(Message.UpdateSelectedWorkoutType(
                intent.workoutId?.takeUnless { it == state().selectedWorkoutId }))
            Intent.RetryRequest -> load()
        }
    }
    override fun executeAction(action: Unit) = load()
    private fun load() {
        if (state().isLoading) return
        dispatch(Message.Loading)
        scope.launch {
            repository.schedule().fold(onSuccess = { dispatch(Message.Loaded(it)) }, onFailure = {
                if (it is CancellationException) throw it
                dispatch(Message.Failed(SauceError.General(it)))
            })
        }
    }
}
