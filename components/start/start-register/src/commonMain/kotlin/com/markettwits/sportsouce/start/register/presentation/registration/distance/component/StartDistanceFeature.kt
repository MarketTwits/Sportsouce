package com.markettwits.sportsouce.start.register.presentation.registration.distance.component

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationAdditionalField
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStageWithStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
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
            val needsUpdate = stageWithStatement.stage.additionalFields.any {
                it.field.id == startRegisterAnswer.field.id
            }
            if (needsUpdate) {
                val updatedStage = stageWithStatement.stage.copy(
                    additionalFields = updateAnswers(stageWithStatement.stage.additionalFields, startRegisterAnswer)
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
        val answerExists = answers.any { it.field.id == fieldId }

        return if (answerExists) {
            answers.map { if (it.field.id == fieldId) startRegisterAnswer else it }
        } else {
            val isAnswerEmpty = startRegisterAnswer.answer.let { answer ->
                answer.string.isNullOrEmpty() &&
                        answer.number == null &&
                        answer.bool == null &&
                        answer.date.isNullOrEmpty() &&
                        answer.multiSelect.isNullOrEmpty() &&
                        answer.singleSelect == null
            }

            if (isAnswerEmpty) answers else answers + startRegisterAnswer
        }
    }

    private fun validateAnswers(
        page: StartRegistrationStagePage.Registration,
        onSuccess: () -> Unit,
        onMessage: (EventContent) -> Unit,
    ) {
        val requiredFieldIdsFromDistance = page.distance.answers
            .map { it.field }
            .filter { !it.isOptional }
            .map { it.id }

        val requiredFieldIdsFromStages = page.distance.stages
            .flatMap { it.stage.additionalFields }
            .map { it.field }
            .filter { !it.isOptional }
            .map { it.id }

        val requiredFieldIds = requiredFieldIdsFromDistance + requiredFieldIdsFromStages

        val providedAnswerIdsFromDistance = page.distance.answers
            .filter { answer ->
                val field = answer.field
                when (field.type) {
                    StartRegistrationAdditionalField.Type.TEXT -> !answer.answer.string.isNullOrBlank()
                    StartRegistrationAdditionalField.Type.NUMBER -> answer.answer.number != null
                    StartRegistrationAdditionalField.Type.CHECKBOX -> answer.answer.bool != null
                    StartRegistrationAdditionalField.Type.DATA -> !answer.answer.date.isNullOrBlank()
                    StartRegistrationAdditionalField.Type.TIME -> !answer.answer.string.isNullOrBlank()
                    StartRegistrationAdditionalField.Type.MULTISELECT -> !answer.answer.multiSelect.isNullOrEmpty()
                    StartRegistrationAdditionalField.Type.SINGLE_SELECT -> answer.answer.singleSelect != null
                }
            }
            .map { it.field.id }

        val providedAnswerIdsFromStages = page.distance.stages
            .flatMap { it.stage.additionalFields }
            .filter { answer ->
                val field = answer.field
                when (field.type) {
                    StartRegistrationAdditionalField.Type.TEXT -> !answer.answer.string.isNullOrBlank()
                    StartRegistrationAdditionalField.Type.NUMBER -> answer.answer.number != null
                    StartRegistrationAdditionalField.Type.CHECKBOX -> answer.answer.bool != null
                    StartRegistrationAdditionalField.Type.DATA -> !answer.answer.date.isNullOrBlank()
                    StartRegistrationAdditionalField.Type.TIME -> !answer.answer.string.isNullOrBlank()
                    StartRegistrationAdditionalField.Type.MULTISELECT -> !answer.answer.multiSelect.isNullOrEmpty()
                    StartRegistrationAdditionalField.Type.SINGLE_SELECT -> answer.answer.singleSelect != null
                }
            }
            .map { it.field.id }

        val providedAnswerIds = providedAnswerIdsFromDistance + providedAnswerIdsFromStages

        if (requiredFieldIds.all { it in providedAnswerIds }) {
            onSuccess()
        } else {
            onMessage(EventContent(false, "Заполните обязательные дополнительные поля"))
        }
    }

}
