package com.markettwits.sportsouce.start.register

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.start.cloud.model.start.fields.AdditionalField
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.cloud.model.start.fields.Stage
import com.markettwits.sportsouce.start.register.domain.StartRegType
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper.StartRegisterPriceMapper
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper.StartRegistrationPageMapper
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.visibleAdditionalFieldAnswers
import com.markettwits.sportsouce.start.register.presentation.registration.distance.component.StartDistanceFeature
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationAdditionalFieldsTest {
    private val timeMapper = BaseTimeMapper()
    private val fields = Json { explicitNulls = false }.decodeFromString<List<AdditionalField>>(
        """[
           {"id":284,"uuid":"plan","name":"План на гонку","type":"Текст","description":"Пример: 24 круга или 100 км",
            "is_optional":false,"is_public":false,"options":[]},
           {"id":285,"uuid":"merch","name":"Мерч стартового пакета","type":"Синглселект",
            "description":"На фото показан пример мерча 2026 года","is_optional":false,"is_public":false,
            "options":[{"id":372,"uuid":"tank","title":"Майка танк"},{"id":373,"uuid":"shirt","title":"Футболка"}],
            "dependent_fields":[{"id":286,"uuid":"size","name":"Размер","type":"Синглселект","parent_id":285,
              "is_optional":false,"is_public":false,"options":[{"id":377,"title":"M"}]}]}
        ]""",
    )

    @Test
    fun merchSelectionRequiresSizeAndIncludesItInPriceAndRegistrationRequests() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val distance = distance(distanceFields = fields)
            assertEquals("Пример: 24 круга или 100 км", distance.answers.first().field.description)
            val feature = StartDistanceFeature(page(distance), onMessage = {})
            val (plan, merch) = distance.answers
            assertEquals(listOf(284, 285), distance.answers.visibleIds())
            feature.onChangeAnswer(plan.copy(answer = plan.answer.copy(string = "24 круга")), isDistanceAnswer = true)
            feature.onChangeAnswer(merch.copy(answer = merch.answer.copy(singleSelect = 372)), isDistanceAnswer = true)
            runCurrent()
            val visible = feature.state.value.distance.answers.visibleAdditionalFieldAnswers()
            assertEquals(listOf(284, 285, 286), visible.map { it.field.id })

            var completed: StartRegistrationStagePage.Registration? = null
            feature.onClickGoNext { completed = it }
            assertNull(completed)

            val size = visible.last()
            feature.onChangeAnswer(size.copy(answer = size.answer.copy(singleSelect = 377)), isDistanceAnswer = true)
            runCurrent()
            feature.onClickGoNext { completed = it }
            val updatedDistance = assertNotNull(completed).distance
            for (withoutPayment in listOf(null, false)) {
                val answers = outgoingAnswers(updatedDistance, withoutPayment)
                assertEquals(listOf(284, 285, 286), answers.map { it.additionalFieldId })
                assertEquals("24 круга", answers[0].string)
                assertEquals(listOf(372, 377), answers.mapNotNull { it.singleSelect })
            }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun aHiddenStageFieldIsExcludedEvenAfterItHasBeenAnswered() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val parent = fields.last()
            val child = parent.dependent_fields!!.single().copy(expected_answer_uuid = "tank")
            val distance = distance(stageFields = listOf(parent.copy(dependent_fields = listOf(child))))
            val feature = StartDistanceFeature(page(distance), onMessage = {})
            val merch = distance.stages.single().stage.additionalFields.single()
            feature.onChangeAnswer(merch.copy(answer = merch.answer.copy(singleSelect = 372)), isDistanceAnswer = false)
            runCurrent()
            val size = feature.state.value.distance.stages.single().stage.additionalFields.visibleAdditionalFieldAnswers().last()
            feature.onChangeAnswer(size.copy(answer = size.answer.copy(singleSelect = 377)), isDistanceAnswer = false)
            runCurrent()
            assertEquals(listOf(372, 377), outgoingAnswers(feature.state.value.distance).mapNotNull { it.singleSelect })

            feature.onChangeAnswer(merch.copy(answer = merch.answer.copy(singleSelect = 373)), isDistanceAnswer = false)
            runCurrent()
            var completed: StartRegistrationStagePage.Registration? = null
            feature.onClickGoNext { completed = it }
            assertEquals(listOf(285), outgoingAnswers(assertNotNull(completed).distance).map { it.additionalFieldId })
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun List<StartRegistrationStatementAnswer>.visibleIds() = visibleAdditionalFieldAnswers().map { it.field.id }

    private fun outgoingAnswers(distance: StartRegistrationDistance, withoutPayment: Boolean? = null) =
        StartRegisterPriceMapper(timeMapper).mapPrice(null, 329, "", StartRegType.Default, withoutPayment, listOf(distance))
            .distances.single().member.single().answers

    private fun page(distance: StartRegistrationDistance) =
        StartRegistrationStagePage.Registration(0, distance.title, "", false, true, distance = distance)

    private fun distance(distanceFields: List<AdditionalField> = emptyList(), stageFields: List<AdditionalField> = emptyList()): StartRegistrationDistance {
        val runner = User(
            address = "Томск", age = null, birthday = "2000-01-01", commentForAddress = null, createdAt = "2026-01-01",
            email = "runner@example.test", id = 1, instagram = null, name = "Иван", number = "79000000000",
            patronymic = null, photoId = null, sex = "Мужской", surname = "Иванов", team = "Тестовая команда", telegram = null,
            vk = null, whatsapp = null,
        )
        val stage = Stage(stageFields, 487, 519, name = "Бег", sex = null, stageLength = null, uuid = "stage")
        val remote = DistinctDistance(distanceFields, null, null, null, format = null, id = 487, infiniteSlots = true,
            name = "Тестовый старт", openSlots = null, orderNumber = 0, slots = null, stages = listOf(stage), staticPrice = 700000)
        return StartRegistrationPageMapper(timeMapper)
            .mapToStartRegistrationDistance(runner, listOf(remote), emptyList(), emptyList(), emptyList()).single()
    }
}
