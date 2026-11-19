package com.markettwits.sportsouce.start.register.presentation.registration.distance.component

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStageWithStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.hasValue
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.visibleAdditionalFieldAnswers
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.RegistrationMemberValidatorBase
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartDistanceFeature(
    innerState : StartRegistrationStagePage.Registration,
    private val onMessage: (EventContent) -> Unit,
) : InstanceKeeper.Instance {

    val state: MutableStateFlow<StartRegistrationStagePage.Registration> = MutableStateFlow(innerState)

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    fun onChangeStartStatement(startStatement: StartStatement) {
        coroutineScope.launch {
            val newState = updateStartStatement(state.value, startStatement)
            val updatedState = startStatement.stageId?.let { stageId ->
                newState.copy(invalidStageIds = newState.invalidStageIds - stageId)
            } ?: newState
            state.emit(updatedState)
        }
    }

    fun onChangeAnswer(
        startRegisterAnswer: StartRegistrationStatementAnswer,
        isDistanceAnswer : Boolean
    ) {
        coroutineScope.launch {
            val newState = if (isDistanceAnswer)
                    updateDistanceAnswers(startRegisterAnswer)
                else
                    updateStageAdditionalFields(startRegisterAnswer)
            state.emit(newState)
        }
    }

    fun onClickGoNext(
        onGoNext : (StartRegistrationStagePage.Registration) -> Unit,
    ) {
        validateStartStatements(
            page = state.value,
            onSuccess = {
                coroutineScope.launch {
                    state.emit(state.value.copy(invalidStageIds = emptySet()))
                }
            validateAnswers(
                page = state.value,
                onSuccess = {
                    onGoNext(state.value)
                },
                onMessage = {
                    onMessage(it)
                }
            )
            },
        )
    }

    private fun emitValidationErrorState(page: StartRegistrationStagePage.Registration, invalidStageIds: Set<Int>) {
        coroutineScope.launch {
            state.emit(
                page.copy(
                    invalidStageIds = invalidStageIds,
                    validationAttemptTick = page.validationAttemptTick + 1
                )
            )
        }
    }

    private fun validateStartStatements(
        page: StartRegistrationStagePage.Registration,
        onSuccess: () -> Unit,
    ) {
        val stageResults = page.distance.stages.map { stageWithStatement ->
            stageWithStatement.stage.id to RegistrationMemberValidatorBase().validateFields(stageWithStatement.statement)
        }
        val invalidStageIds = stageResults
            .filter { it.second.isFailure }
            .map { it.first }
            .toSet()

        if (invalidStageIds.isEmpty()) {
            onSuccess()
        } else {
            emitValidationErrorState(page = page, invalidStageIds = invalidStageIds)
            stageResults.firstOrNull { it.second.isFailure }
                ?.second
                ?.exceptionOrNull()
                ?.message
                ?.let { onMessage(EventContent(false, it)) }
        }
    }

    private fun updateStartStatement(
        distance: StartRegistrationStagePage.Registration,
        updatedStatement: StartStatement
    ): StartRegistrationStagePage.Registration {
        val updatedStages = distance.distance.stages.map {
            if (it.stage.id == updatedStatement.stageId) {
                StartRegistrationStageWithStatement(
                    statement = updatedStatement,
                    stage = it.stage
                )
            } else {
                StartRegistrationStageWithStatement(
                    statement = it.statement,
                    stage = it.stage
                )
            }
        }
        return distance.copy(distance = distance.distance.copy(stages = updatedStages))
    }

    fun updateStageAdditionalFields(
        startRegisterAnswer: StartRegistrationStatementAnswer
    ): StartRegistrationStagePage.Registration {
        val registration = state.value
        val updatedStages = registration.distance.stages.map { stageWithStatement ->
            val fields = stageWithStatement.stage.additionalFields
            val updatedFields = updateAnswers(fields, startRegisterAnswer)
            if (updatedFields !== fields) {
                val updatedStage = stageWithStatement.stage.copy(
                    additionalFields = updatedFields,
                )
                stageWithStatement.copy(stage = updatedStage)
            } else {
                stageWithStatement
            }
        }
        return registration.copy(
            distance = registration.distance.copy(
                stages = updatedStages
            )
        )
    }

    fun updateDistanceAnswers(
        startRegisterAnswer: StartRegistrationStatementAnswer
    ): StartRegistrationStagePage.Registration {
        val registration = state.value
        val updatedAnswers = updateAnswers(registration.distance.answers, startRegisterAnswer)
        return registration.copy(
            distance = registration.distance.copy(
                answers = updatedAnswers
            )
        )
    }

    private fun updateAnswers(
        answers: List<StartRegistrationStatementAnswer>,
        startRegisterAnswer: StartRegistrationStatementAnswer
    ): List<StartRegistrationStatementAnswer> {
        val fieldId = startRegisterAnswer.field.id
        if (answers.visibleAdditionalFieldAnswers().none { it.field.id == fieldId }) return answers

        return when {
            answers.any { it.field.id == fieldId } -> answers.map { if (it.field.id == fieldId) startRegisterAnswer else it }
            startRegisterAnswer.hasValue() -> answers + startRegisterAnswer
            else -> answers
        }
    }

    private fun validateAnswers(
        page: StartRegistrationStagePage.Registration,
        onSuccess: () -> Unit,
        onMessage: (EventContent) -> Unit,
    ) {
        val visibleAnswers = page.distance.answers.visibleAdditionalFieldAnswers() +
            page.distance.stages.flatMap { it.stage.additionalFields.visibleAdditionalFieldAnswers() }
        if (visibleAnswers.all { it.field.isOptional || it.hasValue() }) {
            onSuccess()
        } else {
            onMessage(EventContent(false, "Заполните обязательные дополнительные поля"))
        }
    }

}
