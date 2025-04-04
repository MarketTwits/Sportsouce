package com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.start.cloud.model.register.price.StartRegisterPriceRequest
import com.markettwits.sportsouce.start.cloud.model.register.price.StartRegisterPriceResponse
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterAnswer
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterDistance
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterMember
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationAdditionalField
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

class StartRegisterPriceMapper(private val timeMapper: TimeMapper) {

    fun mapPriceResponse(
        priceResponse: StartRegisterPriceResponse
    ) : StartRegistrationPriceResult {
        return if (priceResponse.isPaymentRequired)
            StartRegistrationPriceResult.Value(
                additionalFieldsPrice = priceResponse.additionalFieldsPrice,
                priceWithoutDiscount = priceResponse.priceWithoutDiscount,
                totalPrice = priceResponse.totalPrice
            )
        else
            StartRegistrationPriceResult.Free
    }

    fun mapPrice(
        comboId : Int?,
        startId : Int,
        promo: String,
        registrationWithoutPayment : Boolean?,
        distances: List<StartRegistrationDistance>,
    ) : StartRegisterPriceRequest =

        StartRegisterPriceRequest(
            comboId = comboId,
            promocode = promo,
            startId = startId,
            registrationWithoutPayment = registrationWithoutPayment,
            distances = mapDistances(distances)
        )

    private fun mapDistances(
        distances: List<StartRegistrationDistance>,
    ) : List<StartRegisterDistance>{
        return distances.map { distance ->
            StartRegisterDistance(
                distanceId = distance.id,
                member = distance.stages.map { stage ->
                    stage.statement.mapToStartRegisterMember(
                        stageId = stage.stage.id,
                        commonAnswers = distance.answers,
                        statementAnswers = stage.stage.additionalFields
                    )
                }
            )
        }
    }
    private fun StartStatement.mapToStartRegisterMember(
        stageId : Int,
        commonAnswers: List<StartRegistrationStatementAnswer>,
        statementAnswers : List<StartRegistrationStatementAnswer>,
    ) : StartRegisterMember {
        val items : MutableList<StartRegistrationStatementAnswer>
                = emptyList<StartRegistrationStatementAnswer>().toMutableList()

        statementAnswers.forEach { value ->
            items.add(value)
        }

        commonAnswers.forEach { value ->
            items.add(value)
        }

        val member = StartRegisterMember(
            answers = mapAnswers(items),
            birthday = timeMapper.mapTimeToCloud(time = birthday),
            city = city,
            email = email,
            gender = sex,
            name = name,
            phone = phone,
            stageId = stageId,
            surname = surname,
            team = team,
            userId = userId.userId
        )

        return member
    }

    private fun mapAnswers(answers: List<StartRegistrationStatementAnswer>) : List<StartRegisterAnswer>{
        return answers.filter { statementAnswer ->

            val field = statementAnswer.field

            val answer = statementAnswer.answer

            // Если поле обязательное, всегда оставляем
            if (!field.isOptional) return@filter true

            // Для опциональных полей проверяем значения на основе типа
            when (field.type) {
                StartRegistrationAdditionalField.Type.TEXT -> !answer.string.isNullOrBlank()
                StartRegistrationAdditionalField.Type.NUMBER -> answer.number != null
                StartRegistrationAdditionalField.Type.CHECKBOX -> answer.bool != null
                StartRegistrationAdditionalField.Type.DATA -> !answer.date.isNullOrBlank()
                StartRegistrationAdditionalField.Type.TIME -> !answer.date.isNullOrBlank()
                StartRegistrationAdditionalField.Type.MULTISELECT -> !answer.multiSelect.isNullOrEmpty()
                StartRegistrationAdditionalField.Type.SINGLE_SELECT -> answer.singleSelect != null
            }
        }.map { it.answer }

    }
}
