package com.markettwits.sportsouce.start.register

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.register.domain.*
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.StartRegistrationRepository
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.*
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.*
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.*
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationStateRegressionTest {
    private val info = StartRegistrationInfo(1, "Старт", paymentType = "", isPaymentDisabled = false, regType = StartRegType.Default)
    private val distance = StartRegistrationDistance(1, "", "Одинаковое название", emptyList(), emptyList(), StartRegistrationFieldPrice.Empty, "")
    private val registration = StartRegistrationStagePage.Registration(0, distance.title, "", false, false, distance = distance)
    private val pay = StartRegistrationStagePage.Pay(3, "Оплата", "", price = StartRegistrationPriceResult.Free, startInfo = info, distances = listOf(distance))

    @BeforeTest fun mainDispatcher() { Dispatchers.setMain(StandardTestDispatcher()) }
    @AfterTest fun resetDispatcher() { Dispatchers.resetMain() }

    @Test
    fun changingAStageDoesNotOverwriteAnotherStageWithTheSameTitle() {
        val other = registration.copy(id = 1, distance = distance.copy(id = 2))
        val stages = listOf(registration, other, pay)
        val store = DefaultStoreFactory().create<StartRegistrationPageStore.Intent, StartRegistrationPageStore.Action, StartRegistrationPageStore.Message, StartRegistrationPageStore.State, StartRegistrationPageStore.Label>(
            name = "stage-regression", initialState = StartRegistrationPageStore.State(stages, registrationInfo = info),
            executorFactory = { StartRegistrationPageExecutor() }, reducer = StartRegistrationPageReducer)
        try {
            val updated = registration.copy(description = "Изменён только первый этап")
            store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(updated))
            assertEquals(listOf(updated, other, pay), store.state.stages)
            store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(other.copy(id = 99)))
            assertEquals(listOf(updated, other, pay), store.state.stages)
        } finally { store.dispose() }
    }

    @Test
    fun successfulReloadClearsAnEarlierErrorWithoutLosingRegistrationData() {
        val initial = StartRegistrationPageStore.State(listOf(registration), registrationInfo = info,
            pagesState = StartRegistrationPageStore.PagesState(false, SauceError.Connection(Exception("offline"))))
        with(StartRegistrationPageReducer) {
            val loading = initial.reduce(StartRegistrationPageStore.Message.UpdateStageLoading)
            assertTrue(loading.pagesState.isLoading); assertNull(loading.pagesState.error)
            val ready = initial.reduce(StartRegistrationPageStore.Message.UpdateStages(listOf(registration, pay)))
            assertFalse(ready.pagesState.isLoading); assertNull(ready.pagesState.error)
            assertEquals(info, ready.registrationInfo)
            assertEquals(listOf(registration, pay), ready.stages)
        }
    }

    @Test
    fun rapidPaySaveAndBackEventsCannotStartDuplicateRequests() = runTest {
        val repository = FakeRepository()
        val intent = FakeIntent()
        val store = StartPayStoreFactory(DefaultStoreFactory(), repository, intent, FakeTracker()).create(pay)
        val labels = mutableListOf<StartPayStore.Label>()
        val collect = store.labels.onEach { labels += it }.launchIn(backgroundScope)
        try {
            runCurrent()
            assertTrue(store.state.isLoading)
            store.accept(StartPayStore.Intent.OnClickRegistryWithPay)
            store.accept(StartPayStore.Intent.OnClickPromo)
            runCurrent()
            assertEquals(0, repository.registrations)
            assertTrue(labels.isEmpty())
            repository.price.complete(Result.success(StartRegistrationPriceResult.Free)); runCurrent()
            assertFalse(store.state.isLoading)
            store.accept(StartPayStore.Intent.OnClickRegistryWithPay)
            // These arrive before the coroutine runs or Compose observes the loading state.
            store.accept(StartPayStore.Intent.OnClickRegistryWithPay)
            store.accept(StartPayStore.Intent.OnClickRegistryWithoutPay)
            store.accept(StartPayStore.Intent.OnClickGoBack)
            store.accept(StartPayStore.Intent.OnClickPromo)
            assertTrue(store.state.isLoading)
            runCurrent()
            assertEquals(1, repository.registrations)
            assertEquals(listOf(false), repository.withoutPayment)
            assertTrue(labels.isEmpty())
            repository.registration.complete(Result.success(StartRegistrationResult(true, false, "https://example.test/payment")))
            runCurrent()
            assertFalse(store.state.isLoading)
            assertEquals(listOf("https://example.test/payment"), intent.opened)
            assertEquals(listOf<StartPayStore.Label>(StartPayStore.Label.GoSuccess), labels)
        } finally { collect.cancel(); store.dispose() }
    }

    @Test
    fun failedSubmitReenablesRetryAndPreservesTheCurrentAnswers() = runTest {
        val repository = FakeRepository()
        val store = StartPayStoreFactory(DefaultStoreFactory(), repository, FakeIntent(), FakeTracker()).create(pay)
        try {
            repository.price.complete(Result.success(StartRegistrationPriceResult.Free)); runCurrent()
            store.accept(StartPayStore.Intent.OnClickRegistryWithoutPay); runCurrent()
            repository.registration.complete(Result.failure(SauceError.Connection(Exception("offline")))); runCurrent()
            assertFalse(store.state.isLoading)
            assertEquals(pay.distances, store.state.state.distances)
            repository.registration = CompletableDeferred()
            store.accept(StartPayStore.Intent.OnClickRegistryWithoutPay); runCurrent()
            assertEquals(2, repository.registrations)
            assertEquals(listOf(true, true), repository.withoutPayment)
        } finally { store.dispose() }
    }

    private class FakeRepository : StartRegistrationRepository {
        val price = CompletableDeferred<Result<StartRegistrationPriceResult>>()
        var registration = CompletableDeferred<Result<StartRegistrationResult>>()
        var registrations = 0
        val withoutPayment = mutableListOf<Boolean>()
        override suspend fun getStartPrice(comboId: Int?, startId: Int, promo: String, distances: List<StartRegistrationDistance>, regType: StartRegType) = price.await()
        override suspend fun registerOnStart(comboId: Int?, startId: Int, promo: String, registrationWithoutPayment: Boolean, distances: List<StartRegistrationDistance>, regType: StartRegType): Result<StartRegistrationResult> {
            registrations++; withoutPayment += registrationWithoutPayment; return registration.await()
        }
        override suspend fun isRegisterAvailable() = Result.success(Unit)
        override suspend fun getStartPromo(value: String, startId: Int, distancesId: List<Int>): Result<StartPromo> = error("Unexpected promo request")
        override suspend fun getStartDistances(distances: List<DistinctDistance>): Result<List<StartRegistrationDistance>> = error("Unexpected distance request")
    }
    private class FakeIntent : IntentAction {
        val opened = mutableListOf<String>()
        override fun openWebPage(url: String) { opened += url }
        override fun openPhone(phone: String) = Unit
        override fun shareImage(byteArray: ByteArray, filename: String) = Unit
        override fun saveImage(byteArray: ByteArray, filename: String) = Unit
        override fun copyToSystemBuffer(text: String) = Unit
        override fun sharePlainText(text: String) = Unit
    }
    private class FakeTracker : ExceptionTracker {
        override fun reportException(exception: Throwable, key: String?) = Unit
        override fun setLog(message: String) = Unit
        override fun setUserId(userId: String) = Unit
        override fun setKey(key: Pair<String, String>) = Unit
    }
}
