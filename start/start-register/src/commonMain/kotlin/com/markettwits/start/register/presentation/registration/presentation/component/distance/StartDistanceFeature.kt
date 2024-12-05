package com.markettwits.start.register.presentation.registration.presentation.component.distance

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidatorBase
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationAdditionalField
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStageWithStatement
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartDistanceFeature(
    private val innerState : StartRegistrationStagePage.Registration,
    private val onMessage: (EventContent) -> Unit,
) : InstanceKeeper.Instance {

    val state: MutableStateFlow<StartRegistrationStagePage.Registration> = MutableStateFlow(innerState)

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    fun onChangeStartStatement(startStatement: StartStatement) {
        coroutineScope.launch {
            val newState = updateStartStatement(state.value, startStatement)
            state.emit(newState)
        }
    }

    fun onChangeAnswer(startRegisterAnswer: StartRegistrationStatementAnswer) {
        coroutineScope.launch {
            val newState = updateAnswer(state.value, startRegisterAnswer)
            state.emit(newState)
        }
    }

    fun onClickGoNext(
        onGoNext : (StartRegistrationStagePage.Registration) -> Unit,
    ) {
        validateStartStatements(state.value){
            validateAnswers(
                page = state.value,
                onSuccess = {
                    onGoNext(state.value)
                },
                onMessage = {
                    onMessage(it)
                }
            )
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

    private fun updateAnswer(
        distance: StartRegistrationStagePage.Registration,
        startRegisterAnswer: StartRegistrationStatementAnswer
    ): StartRegistrationStagePage.Registration {
        val isAnswerEmpty = startRegisterAnswer.answer.let { answer ->
            answer.string.isNullOrEmpty() &&
                    answer.number == null &&
                    answer.bool == null &&
                    answer.date.isNullOrEmpty() &&
                    answer.multiSelect.isNullOrEmpty() &&
                    answer.singleSelect == null
        } ?: true // Если answer == null, то он считается пустым

        val updatedAnswers = distance.distance.answers.map { existingAnswer ->
            if (existingAnswer.field.id == startRegisterAnswer.field.id) {
                if (isAnswerEmpty) {
                    // Удаляем только answer, оставляя StartRegistrationStatementAnswer
                    existingAnswer.copy()
                } else {
                    // Обновляем answer
                    startRegisterAnswer
                }
            } else {
                existingAnswer // Оставляем без изменений
            }
        }.let { answers ->
            // Если ответ с таким ID не найден и он не пустой, добавляем новый StartRegistrationStatementAnswer
            if (!isAnswerEmpty && answers.none { it.field .id == startRegisterAnswer.field.id }) {
                answers + startRegisterAnswer
            } else {
                answers
            }
        }

        // Возвращаем новый объект Registration с обновленным distance
        return distance.copy(
            distance = distance.distance.copy(
                answers = updatedAnswers
            )
        )
    }

    private fun validateAnswers(
        page: StartRegistrationStagePage.Registration,
        onSuccess: () -> Unit,
        onMessage: (EventContent) -> Unit // Добавлено для вызова сообщения
    ) {
        val requiredFieldIds = page.distance.answers
            .map { it.field }
            .filter { !it.isOptional } // Отфильтровываем обязательные поля
            .map { it.id }

        // Получаем ID полей, на которые есть ответы
        val providedAnswerIds = page.distance.answers
            .filter { answer ->
                val field = answer.field
                when (field.type) {
                    StartRegistrationAdditionalField.Type.TEXT -> !answer.answer.string.isNullOrBlank()
                    StartRegistrationAdditionalField.Type.NUMBER -> answer.answer.number != null
                    StartRegistrationAdditionalField.Type.CHECKBOX -> answer.answer.bool != null
                    StartRegistrationAdditionalField.Type.DATA -> !answer.answer.date.isNullOrBlank()
                    StartRegistrationAdditionalField.Type.TIME -> !answer.answer.date.isNullOrBlank() // Возможно нужно отдельное поле для времени
                    StartRegistrationAdditionalField.Type.MULTISELECT -> !answer.answer.multiSelect.isNullOrEmpty()
                    StartRegistrationAdditionalField.Type.SINGLE_SELECT -> answer.answer.singleSelect != null
                }
            }
            .map { it.field.id }

        // Проверяем, что все обязательные поля есть в списке заполненных
        if (requiredFieldIds.all { it in providedAnswerIds }) {
            onSuccess()
        } else {
            onMessage(EventContent(false, "Заполните обязательные дополнительные поля"))
        }
    }

    private fun validateStartStatements(
        page : StartRegistrationStagePage.Registration,
        onSuccess : () -> Unit,
    ){
        val result = page.distance.stages.map {
            RegistrationMemberValidatorBase().validateFields(it.statement)
        }
        if (result.all { it.isSuccess }) {
            onSuccess()
        } else {
            result.map {
                it.onFailure { failure ->
                    onMessage(EventContent(false, failure.message.toString()))
                }
            }
        }
    }

}