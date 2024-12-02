package com.markettwits.start.register.presentation.registration.data.mapper

import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationAdditionalField
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationDistance
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationPriceResult
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStageWithStatement
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start_cloud.model.register.price.StartRegisterPriceRequest
import com.markettwits.start_cloud.model.register.price.StartRegisterPriceResponse
import com.markettwits.start_cloud.model.register.price.fields.StartRegisterAnswer
import com.markettwits.start_cloud.model.register.price.fields.StartRegisterDistance
import com.markettwits.start_cloud.model.register.price.fields.StartRegisterMember
import com.markettwits.time.TimeMapper

class StartRegisterPriceMapper(private val timeMapper: TimeMapper) {

    fun mapPriceResponse(
        priceResponse: StartRegisterPriceResponse
    ) : StartRegistrationPriceResult{
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
            combo_id = comboId,
            promocode = promo,
            start_id = startId,
            registration_without_payment = registrationWithoutPayment,
            distances = mapDistances(distances)
        )

    private fun mapDistances(
        distances: List<StartRegistrationDistance>,
    ) : List<StartRegisterDistance>{
        return distances.map {
            StartRegisterDistance(
                distance_id = it.id,
                member = it.stages.map { stage ->
                    stage.statement.mapToStartRegisterMember(
                        stageId = stage.stage.id,
                        answer = it.answers
                    )
                }
            )
        }
    }
    private fun StartStatement.mapToStartRegisterMember(
        stageId : Int,
        answer: List<StartRegistrationStatementAnswer>
    ) : StartRegisterMember = StartRegisterMember(
        answers = mapAnswers(answer),
        birthday = timeMapper.mapTimeToCloud(time = birthday),
        city = city,
        email = email,
        gender = sex,
        name = name,
        phone = phone,
        stage_id = stageId,
        surname = surname,
        team = team,
        user_id = userId.userId
    )
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
