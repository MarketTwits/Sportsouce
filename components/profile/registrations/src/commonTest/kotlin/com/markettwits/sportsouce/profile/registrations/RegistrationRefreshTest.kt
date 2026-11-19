package com.markettwits.sportsouce.profile.registrations

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPrice
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderType
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponentBase
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStoreFactory
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponentBase
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsDataStoreFactory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationRefreshTest {
    @Test
    fun returningToRegistrationsFetchesNewOrdersInTheBackgroundAndKeepsTheFilter() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        val original = order(1)
        val added = order(2)
        val paid = order(3).copy(payment = StartOrderPaymentStatus.Success())
        val repository = TestRepository(listOf(original, paid))
        try {
            val component = RegistrationsComponentBase(
                DefaultComponentContext(lifecycle),
                RegistrationsDataStoreFactory(DefaultStoreFactory(), repository, testTracker),
                pop = {},
                onItemClick = {},
            )
            lifecycle.resume()
            runCurrent()
            component.toggleFilter(component.value.value.filter.single { it.value == original.payment.title })
            assertEquals(listOf(original), component.value.value.filtered)

            lifecycle.pause()
            val network = CompletableDeferred<Unit>()
            repository.network = network
            repository.orders = listOf(added, original, paid)
            lifecycle.resume()
            runCurrent()
            assertEquals(listOf(original), component.value.value.filtered)
            assertFalse(component.value.value.isRefreshing)
            assertFalse(component.value.value.isLoading)

            network.complete(Unit)
            runCurrent()
            assertEquals(listOf(false, false), repository.requests)
            assertEquals(listOf(added, original, paid), component.value.value.base)
            assertEquals(listOf(added, original), component.value.value.filtered)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }

    @Test
    fun returningFromEditingFollowsOnlyTheLinkedRegistrationAndRepricesTheNewOrder() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        val original = order(10)
        val replacement = order(12).copy(orderType = StartOrderType.ReRegistration(original.id))
        val unrelated = order(13).copy(orderType = StartOrderType.ReRegistration(11))
        val differentStart = order(14).copy(startId = 2, orderType = StartOrderType.ReRegistration(original.id))
        val repository = TestRepository(listOf(unrelated, original))
        try {
            val component = StartOrderComponentBase(
                DefaultComponentContext(lifecycle),
                original,
                StartOrderStoreFactory(DefaultStoreFactory(), repository, testIntentAction),
                dismiss = {},
                openStart = {},
                onReRegistration = { _, _ -> lifecycle.pause() },
            )
            lifecycle.resume()
            runCurrent()
            assertEquals(original, component.state.value.startOrderInfo)

            component.obtainEvent(StartOrderStore.Intent.OnClickReRegistration(original.startId, original.id))
            runCurrent()
            repository.orders = listOf(differentStart, unrelated, replacement, original)
            lifecycle.resume()
            runCurrent()

            assertEquals(replacement, component.state.value.startOrderInfo)
            assertEquals(StartOrderStore.StartPriceResult.Success(replacement.cost), component.state.value.startPriceResult)
            assertEquals(listOf(original.id, replacement.id), repository.pricedOrders)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }

    @Test
    fun pullingToRefreshUpdatesTheOrderAndPreservesItOnNetworkFailure() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val original = order(10)
        val updated = original.copy(cost = "1800")
        val repository = TestRepository(listOf(original))
        val store = StartOrderStoreFactory(DefaultStoreFactory(), repository, testIntentAction).create(original)
        try {
            runCurrent()
            val network = CompletableDeferred<Unit>()
            repository.network = network
            repository.orders = listOf(updated)
            store.accept(StartOrderStore.Intent.RefreshOrder(userInitiated = true))
            runCurrent()
            store.accept(StartOrderStore.Intent.RefreshOrder(userInitiated = true))
            assertTrue(store.state.isRefreshing)
            assertEquals(original, store.state.startOrderInfo)
            assertEquals(listOf(false), repository.cacheFallbacks)

            network.complete(Unit)
            runCurrent()
            assertEquals(updated, store.state.startOrderInfo)
            assertFalse(store.state.isRefreshing)
            assertNull(store.state.refreshError)

            repository.network = null
            repository.error = IllegalStateException("Test network failure")
            store.accept(StartOrderStore.Intent.RefreshOrder(userInitiated = true))
            runCurrent()
            assertEquals(updated, store.state.startOrderInfo)
            assertFalse(store.state.isRefreshing)
            assertNotNull(store.state.refreshError)
        } finally {
            store.dispose()
            Dispatchers.resetMain()
        }
    }

    private class TestRepository(var orders: List<StartOrderInfo>) : StartOrderRegistrationRepository {
        var network: CompletableDeferred<Unit>? = null
        var error: Exception? = null
        val requests = mutableListOf<Boolean>()
        val cacheFallbacks = mutableListOf<Boolean>()
        val pricedOrders = mutableListOf<Int>()

        override fun registrations(forced: Boolean, allowCachedOnError: Boolean) = flow {
            requests += forced
            cacheFallbacks += allowCachedOnError
            network?.await()
            error?.let { throw it }
            emit(orders)
        }

        override suspend fun currentUser(): Result<SharedUser> = Result.failure(IllegalStateException())
        override suspend fun pay(id: Int): Result<String> = error("Unexpected payment")
        override suspend fun getPrice(orderId: Int, orderDistancesId: List<String>, startId: Int): Result<StartOrderPrice> {
            pricedOrders += orderId
            return Result.success(StartOrderPrice("0", orders.single { it.id == orderId }.cost, true))
        }
    }

    private fun order(id: Int) = StartOrderInfo(
        id = id,
        startId = 1,
        name = "Тестовый старт",
        image = "",
        dateStartPreview = "1 февраля 2027",
        dateStartCloud = "2027-02-01T10:00:00Z",
        promo = "",
        payment = StartOrderPaymentStatus.NotPaid(),
        members = listOf(StartOrderMember("Участник $id", "", "", "", "5 км", "", "", emptyList())),
        orderType = StartOrderType.Default,
        startTitle = "Тестовый старт",
        cost = (id * 100).toString(),
    )

    private val testTracker = object : ExceptionTracker {
        override fun reportException(exception: Throwable, key: String?) = throw exception
        override fun setLog(message: String) = Unit
        override fun setUserId(userId: String) = Unit
        override fun setKey(key: Pair<String, String>) = Unit
    }

    private val testIntentAction = object : IntentAction {
        override fun openWebPage(url: String) = error("Unexpected URL")
        override fun openPhone(phone: String) = Unit
        override fun shareImage(byteArray: ByteArray, filename: String) = Unit
        override fun saveImage(byteArray: ByteArray, filename: String) = Unit
        override fun copyToSystemBuffer(text: String) = Unit
        override fun sharePlainText(text: String) = Unit
    }
}
