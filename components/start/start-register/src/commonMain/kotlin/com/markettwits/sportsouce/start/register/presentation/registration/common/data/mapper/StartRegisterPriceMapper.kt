package com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.start.cloud.model.register.price.StartRegisterPriceRequest
import com.markettwits.sportsouce.start.cloud.model.register.price.StartRegisterPriceResponse
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterDistance
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterMember
import com.markettwits.sportsouce.start.register.domain.StartRegType
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.hasValue
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.visibleAdditionalFieldAnswers

class StartRegisterPriceMapper(private val timeMapper: TimeMapper) {

    fun mapPriceResponse(
        priceResponse: StartRegisterPriceResponse,
    ): StartRegistrationPriceResult {
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
        comboId: Int?,
        startId: Int,
        promo: String,
        regType: StartRegType,
        registrationWithoutPayment: Boolean?,
        distances: List<StartRegistrationDistance>,
    ): StartRegisterPriceRequest =

        StartRegisterPriceRequest(
            comboId = comboId,
            promocode = promo,
            startId = startId,
            registrationWithoutPayment = registrationWithoutPayment,
            isReRegistration = (regType is StartRegType.ReReg),
            previousOrderId = when (regType) {
                is StartRegType.ReReg -> regType.startOrderId
                is StartRegType.Default -> null
            },
            distances = mapDistances(distances)
        )

    private fun mapDistances(
        distances: List<StartRegistrationDistance>,
    ): List<StartRegisterDistance> {
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
        stageId: Int,
        commonAnswers: List<StartRegistrationStatementAnswer>,
        statementAnswers: List<StartRegistrationStatementAnswer>,
    ): StartRegisterMember {
        val visibleAnswers = statementAnswers.visibleAdditionalFieldAnswers() + commonAnswers.visibleAdditionalFieldAnswers()
        return StartRegisterMember(
            answers = visibleAnswers.filter { !it.field.isOptional || it.hasValue() }.map { it.answer },
            birthday = timeMapper.mapTimeToCloud(time = birthday).substringBefore('T'),
            city = city,
            email = email.trim().takeIf { it.isNotEmpty() },
            gender = sex,
            name = name,
            phone = phone.trim().takeIf { it.isNotEmpty() },
            stageId = stageId,
            surname = surname,
            team = team,
            userId = userId.userId.takeIf { participantSource == com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember.Source.Self && userId is StartStatement.UserId.WithId },
            participantUserId = participantUserId?.takeIf { it > 0 },
            patronymic = patronymic?.trim()?.takeIf { it.isNotEmpty() },
        )

    }
}
