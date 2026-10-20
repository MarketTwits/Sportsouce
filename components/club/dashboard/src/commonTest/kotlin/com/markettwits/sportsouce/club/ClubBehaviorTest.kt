package com.markettwits.sportsouce.club

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.EmptyExceptionTracker
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.common.data.mapper.subscription.SubscriptionMapperBase
import com.markettwits.sportsouce.club.common.data.mapper.club_info.ClubInfoMapperBase
import com.markettwits.sportsouce.club.cloud.models.club_settings.ClubSettingsRemoteRow
import com.markettwits.sportsouce.club.cloud.models.subscription.*
import com.markettwits.sportsouce.club.cloud.models.schedule.ScheduleRemoteRow
import com.markettwits.sportsouce.club.dashboard.domain.*
import com.markettwits.sportsouce.club.dashboard.presentation.store.*
import com.markettwits.sportsouce.club.info.domain.models.*
import com.markettwits.sportsouce.club.registration.domain.*
import com.markettwits.sportsouce.club.registration.presentation.store.*
import com.markettwits.sportsouce.club.schedule.store.*
import com.markettwits.sportsouce.club.subscription.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ClubBehaviorTest {
    @Test fun namesRequireCyrillicAndRejectLatinLookalikesBeforeSending() = runTest {
        val repo = FakeClubRepository()
        val useCase = WorkoutRegistrationUseCase(repo)
        val form = WorkoutRegistrationForm(RegistrationType.Workout(9, "Тренировка"), "Фёдор", "Сёмин", "79130037675")
        for (invalid in listOf("Daniil", "Дaниил", "Кулaков", "Иван2", "Иван!")) {
            for (request in listOf(form.copy(name = invalid), form.copy(surname = invalid))) {
                val failure = useCase.register(request).exceptionOrNull()
                assertIs<WorkoutRegistrationValidationException>(failure)
                assertEquals("Имя и фамилия должны содержать только кириллицу", failure.message)
            }
        }
        assertEquals(0, repo.registrationRequests)
        useCase.register(form).getOrThrow()
        assertEquals(1, repo.registrationRequests)
        assertEquals("Фёдор", repo.submittedForm?.name)
        assertEquals("Сёмин", repo.submittedForm?.surname)
    }

    @Test fun validationExplainsTheFieldErrorAndRetryWithProfilePhoneSucceedsOnlyOnce() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repo = FakeClubRepository()
        try {
            val actions = object : IntentAction {
                override fun openWebPage(url: String) = error("Unexpected external action")
                override fun openPhone(phone: String) = error("Unexpected external action")
                override fun shareImage(byteArray: ByteArray, filename: String) = error("Unexpected external action")
                override fun saveImage(byteArray: ByteArray, filename: String) = error("Unexpected external action")
                override fun copyToSystemBuffer(text: String) = error("Unexpected external action")
                override fun sharePlainText(text: String) = error("Unexpected external action")
            }
            val store = WorkoutRegistrationStoreFactory(DefaultStoreFactory(), WorkoutRegistrationUseCase(repo), actions)
                .create(RegistrationType.Subscription(2, 16, "Абонемент"))
            try {
                val form = store.state.form.copy(name = "Даниил", surname = "Кулаков", phone = "7913")
                store.accept(WorkoutRegistrationStore.Intent.OnValueChanged(form))
                store.accept(WorkoutRegistrationStore.Intent.OnClickRegistration)
                assertTrue(store.state.isError)
                assertTrue(store.state.message.startsWith("Введите корректный номер телефона"))
                assertFalse(store.state.isLoading)
                assertEquals(0, repo.registrationRequests)
                store.accept(WorkoutRegistrationStore.Intent.OnValueChanged(form.copy(phone = "79130037675")))
                store.accept(WorkoutRegistrationStore.Intent.OnClickRegistration)
                assertTrue(store.state.isSuccess); assertFalse(store.state.isError)
                assertEquals(RegistrationType.Subscription(2, 16, "Абонемент"), repo.submittedForm?.type)
                store.accept(WorkoutRegistrationStore.Intent.OnClickRegistration)
                assertEquals(1, repo.registrationRequests)
            } finally { store.dispose() }
        } finally { Dispatchers.resetMain() }
    }

    @Test fun profilePhoneAndFormattedPhoneSubmitTheSamePayloadForEachRequestType() = runTest {
        val repo = FakeClubRepository()
        val useCase = WorkoutRegistrationUseCase(repo)
        val mapper = SubscriptionMapperBase()
        for (type in listOf(RegistrationType.Subscription(2, 16, "Абонемент"),
            RegistrationType.Workout(9, "Тренировка"), RegistrationType.Trainer(10, "Тренер"))) {
            for (phone in listOf("79130037675", "+7 (913) 003-76-75", "8 (913) 003-76-75", "9130037675")) {
                val form = WorkoutRegistrationForm(type, "Даниил", "Кулаков", phone)
                useCase.register(form).getOrThrow()
                val request = mapper.map(assertNotNull(repo.submittedForm))
                assertEquals(type.id, request.id)
                assertEquals("79130037675", request.phone)
                when (type) {
                    is RegistrationType.Subscription -> { assertEquals("subscription", request.type); assertEquals(2, request.count) }
                    is RegistrationType.Workout -> { assertEquals("workout", request.type); assertNull(request.count) }
                    is RegistrationType.Trainer -> { assertEquals("trainer", request.type); assertNull(request.count) }
                    else -> error("Unexpected test type")
                }
            }
        }
        assertEquals(12, repo.registrationRequests)
    }

    @Test fun invalidPhonesNeverReachTheRegistrationApi() = runTest {
        val repo = FakeClubRepository()
        val useCase = WorkoutRegistrationUseCase(repo)
        for (phone in listOf("", "+7 (913) 003-76-7", "791300376750", "69130037675", "79130037675abc")) {
            assertTrue(useCase.register(WorkoutRegistrationForm(RegistrationType.Workout(9, "Тренировка"),
                "Даниил", "Кулаков", phone)).isFailure, "Invalid phone accepted: $phone")
        }
        assertEquals(0, repo.registrationRequests)
    }

    @Test fun failedDashboardStopsLoadingAndRetryKeepsPreviouslyLoadedData() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repo = FakeClubRepository()
        try {
            val store = ClubDashboardStoreFactory(DefaultStoreFactory(), repo, EmptyExceptionTracker()).create()
            assertTrue(store.state.isLoading)
            repeat(3) { store.accept(ClubDashboardStore.Intent.RetryRequest) }
            assertEquals(1, repo.infoRequests)
            repo.info.complete(Result.failure(IllegalStateException("schema error"))); runCurrent()
            assertFalse(store.state.isLoading); assertNotNull(store.state.error)
            repo.info = CompletableDeferred()
            store.accept(ClubDashboardStore.Intent.RetryRequest)
            val training = Training(13, "Бег", "Техника бега", "")
            repo.info.complete(Result.success(listOf(ClubInfo.Trainings(listOf(training))))); runCurrent()
            assertTrue(store.state.hasLoaded); assertNull(store.state.error)
            assertEquals(listOf(training), store.state.bottomSheetData.trainings)
            repo.info = CompletableDeferred()
            store.accept(ClubDashboardStore.Intent.RetryRequest)
            repo.info.complete(Result.failure(IllegalStateException("offline"))); runCurrent()
            assertFalse(store.state.isLoading)
            assertEquals(listOf(training), store.state.bottomSheetData.trainings)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test fun scheduleFiltersUseNonSequentialServerIdsAndUnavailableSessionsCannotBeRegistered() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repo = FakeClubRepository()
        val running = session(49, 13, "Бег")
        val skiing = session(47, 12, "Лыжи")
        repo.sessions = listOf(running, skiing, skiing.copy(id = 50, isTempDisabled = true))
        try {
            val store = ScheduleStoreFactory(DefaultStoreFactory(), repo).create()
            val outputs = mutableListOf<ScheduleStore.Label>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { store.labels.collect { outputs += it } }
            assertEquals(3, store.state.schedules.size)
            store.accept(ScheduleStore.Intent.OnClickWorkoutType(13))
            assertEquals(listOf(49), store.state.schedules.map { it.id })
            store.accept(ScheduleStore.Intent.OnClickWorkoutType(12))
            assertEquals(listOf(47, 50), store.state.schedules.map { it.id })
            assertEquals(1, repo.scheduleRequests, "Filtering the loaded week must not refetch it")
            store.accept(ScheduleStore.Intent.OnClickScheduleItem("50", "Лыжи"))
            store.accept(ScheduleStore.Intent.OnClickScheduleItem("bad-id", "Лыжи"))
            assertTrue(outputs.isEmpty())
            store.accept(ScheduleStore.Intent.OnClickScheduleItem("47", "Неверное название"))
            assertEquals(ScheduleStore.Label.Registration(RegistrationType.Schedule(47, "Лыжи")), outputs.single())
            job.cancel(); store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test fun subscriptionCalculationGuardsBoundsAndCannotSubmitAnUnconfirmedCount() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repo = FakeClubRepository()
        try {
            val store = SubscriptionPricingStoreFactory(DefaultStoreFactory(), repo).create()
            val outputs = mutableListOf<SubscriptionPricingStore.Label>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { store.labels.collect { outputs += it } }
            store.accept(SubscriptionPricingStore.Intent.UpdateMonthCount(11, 0))
            store.accept(SubscriptionPricingStore.Intent.UpdateMonthCount(11, 3))
            assertEquals(0, repo.priceRequests)
            store.accept(SubscriptionPricingStore.Intent.UpdateMonthCount(11, 2))
            store.accept(SubscriptionPricingStore.Intent.UpdateMonthCount(11, 2))
            store.accept(SubscriptionPricingStore.Intent.OnClickSubscribe(11, "Неверное название"))
            assertEquals(1, repo.priceRequests); assertTrue(outputs.isEmpty())
            repo.price.complete(Result.success(WorkoutPrice(6000, 4980))); runCurrent()
            store.accept(SubscriptionPricingStore.Intent.OnClickSubscribe(11, "Неверное название"))
            assertEquals(SubscriptionPricingStore.Label.Registration(RegistrationType.Subscription(2, 11, "Беговой на месяц")), outputs.single())
            job.cancel(); store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test fun settingsWithNullTitlesAndBothGroupedSubscriptionSchemasMapWithoutDroppingData() {
        val settings = listOf(ClubSettingsRemoteRow("", "<p>Шаг</p>", 14, null, null, "process", null))
        assertTrue(ClubInfoMapperBase().mapFeatures(settings).isEmpty())
        assertNull(ClubInfoMapperBase().mapMainImage(settings))
        val remote = SubscriptionRemote("", "Тренировки", null, 11, 9, true, 2, SubscriptionColor.GREEN, "Беговой", 3000, "Группа", "")
        val mapper = SubscriptionMapperBase()
        for (group in listOf(SubscriptionItemsRemote(type = "Бег", subscriptions = listOf(remote)),
            SubscriptionItemsRemote(name = "Бег", subscription = listOf(remote)))) {
            val mapped = mapper.map(listOf(group)).single()
            assertEquals("Бег", mapped.groupName); assertEquals(3000, mapped.subscription.single().price)
        }
        assertTrue(mapper.map(listOf(SubscriptionItemsRemote(type = "Скрыто", subscriptions = listOf(remote), isHidden = true))).isEmpty())
    }
}

internal fun session(id: Int, workoutId: Int, name: String) = Schedule(id, workoutId, "Манеж", "Техника", "бег", "19:00", "Анна Иванова", "Вторник", name)
internal val subscriptionFixture = Subscription("<p>Выберите количество тренировок</p>", 0, 11, "Беговой на месяц", 3000, true, 2, "Группа", SubscriptionColor.GREEN)

private class FakeClubRepository : ClubRepository {
    var submittedForm: WorkoutRegistrationForm? = null
    var registrationRequests = 0
    var info = CompletableDeferred<Result<List<ClubInfo>>>()
    var infoRequests = 0
    var sessions = emptyList<Schedule>()
    var scheduleRequests = 0
    var price = CompletableDeferred<Result<WorkoutPrice>>()
    var priceRequests = 0
    override suspend fun clubInfo(): Result<List<ClubInfo>> { infoRequests++; return info.await() }
    override suspend fun schedule(workoutId: Int?): Result<List<Schedule>> { scheduleRequests++; return Result.success(sessions) }
    override suspend fun subscriptions() = flowOf(listOf(SubscriptionItems("Бег", listOf(subscriptionFixture))))
    override suspend fun workoutRegistrationUserData() = WorkoutRegistrationForm.EMPTY
    override suspend fun workoutRegistration(form: WorkoutRegistrationForm): Result<Unit> {
        registrationRequests++; submittedForm = form; return Result.success(Unit)
    }
    override suspend fun workoutRegistrationPrice(form: WorkoutPriceForm): Result<WorkoutPrice> { priceRequests++; return price.await() }
}
