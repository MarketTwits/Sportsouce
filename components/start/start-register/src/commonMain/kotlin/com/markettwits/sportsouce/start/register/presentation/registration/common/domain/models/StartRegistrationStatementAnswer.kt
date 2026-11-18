package com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models

import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterAnswer
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationStatementAnswer(
    val field: StartRegistrationAdditionalField,
    val answer: StartRegisterAnswer
)

internal fun StartRegistrationAdditionalField.emptyAnswer() = StartRegistrationStatementAnswer(
    field = this,
    answer = StartRegisterAnswer(additionalFieldId = id, isOptional = isOptional, type = type.value),
)

internal fun StartRegistrationStatementAnswer.hasValue(): Boolean = when (field.type) {
    StartRegistrationAdditionalField.Type.TEXT, StartRegistrationAdditionalField.Type.TIME -> !answer.string.isNullOrBlank()
    StartRegistrationAdditionalField.Type.NUMBER -> answer.number != null
    StartRegistrationAdditionalField.Type.CHECKBOX -> answer.bool != null
    StartRegistrationAdditionalField.Type.DATA -> !answer.date.isNullOrBlank()
    StartRegistrationAdditionalField.Type.MULTISELECT -> !answer.multiSelect.isNullOrEmpty()
    StartRegistrationAdditionalField.Type.SINGLE_SELECT -> answer.singleSelect != null
}

/** The form keeps entered values by field ID. A dependent field participates only while its parent matches. */
internal fun List<StartRegistrationStatementAnswer>.visibleAdditionalFieldAnswers(): List<StartRegistrationStatementAnswer> {
    val answersById = associateBy { it.field.id }
    val dependentIds = mutableSetOf<Int>()
    fun collectDependentIds(field: StartRegistrationAdditionalField) {
        for (child in field.dependentFields) {
            if (dependentIds.add(child.id)) collectDependentIds(child)
        }
    }
    forEach { collectDependentIds(it.field) }

    return buildList {
        fun addVisible(field: StartRegistrationAdditionalField) {
            val entry = answersById[field.id] ?: field.emptyAnswer()
            add(entry)
            if (entry.hasValue()) {
                for (child in field.dependentFields) {
                    if (entry.matchesDependency(child)) addVisible(child)
                }
            }
        }
        for (entry in this@visibleAdditionalFieldAnswers) {
            if (entry.field.id !in dependentIds) addVisible(entry.field)
        }
    }
}

private fun StartRegistrationStatementAnswer.matchesDependency(child: StartRegistrationAdditionalField): Boolean {
    if (child.expectedAnswerId == null && child.expectedAnswerUuid == null) return true
    val expectedId = child.expectedAnswerId
        ?: field.options.firstOrNull { it.uuid == child.expectedAnswerUuid }?.id
        ?: return false
    return answer.singleSelect == expectedId || answer.multiSelect.orEmpty().contains(expectedId)
}
