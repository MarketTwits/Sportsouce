package com.markettwits.sportsouce.club.schedule.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore.*
import kotlinx.coroutines.launch

class ScheduleExecutor(
    private val repository: ClubRepository,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickBack -> {
                publish(Label.Dismiss)
            }

            is Intent.OnClickScheduleItem -> {
                publish(
                    Label.Registration(
                        RegistrationType.Schedule(
                            intent.scheduleId.toInt(),
                            intent.scheduleName,
                        )
                    )
                )
            }

            is Intent.OnClickWorkoutType -> {
                val newWorkoutId = if (state().selectedWorkoutId == intent.workoutId) {
                    null
                } else {
                    intent.workoutId
                }
                dispatch(Message.UpdateSelectedWorkoutType(newWorkoutId))
                launch()
            }

            is Intent.RetryRequest -> launch()
        }
    }

    override fun executeAction(action: Unit) {
        loadAllWorkoutTypes()
        launch()
    }

    private fun loadAllWorkoutTypes() {
        scope.launch {
            repository.schedule(null).fold(
                onSuccess = { allSchedules ->
                    val allWorkoutTypes = allSchedules.map { it.workoutTitle }.distinct()
                    dispatch(Message.LoadedAllWorkoutTypes(allWorkoutTypes))
                },
                onFailure = {
                    // Игнорируем ошибки для загрузки всех типов тренировок
                }
            )
        }
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.Loading)
            // Получаем workoutTitle по selectedWorkoutId из allWorkoutTypes
            val selectedWorkoutTitle = state().selectedWorkoutId?.let { selectedId ->
                state().allWorkoutTypes.getOrNull(selectedId - 1)
            }

            // Передаем workoutTitle в repository вместо workoutId
            repository.schedule(null).fold( // Загружаем все расписания
                onSuccess = { allSchedules ->
                    val filteredSchedules = if (selectedWorkoutTitle != null) {
                        allSchedules.filter { it.workoutTitle == selectedWorkoutTitle }
                    } else {
                        allSchedules
                    }
                    val workoutTypes = filteredSchedules.map { it.workoutTitle }.distinct()
                    dispatch(Message.Loaded(filteredSchedules, workoutTypes))
                },
                onFailure = {
                    dispatch(Message.Failed(it.mapToSauceError()))
                }
            )
        }
    }
}