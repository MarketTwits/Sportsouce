package com.markettwits.sportsouce.profile.upcoming

import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.Content
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.Intent
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class UpcomingStartsStoreTest {
    @Test
    fun guestHistoryRequiresSignInAndPaidOrdersExpireWhileTheScreenIsOpen() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val first = order(1)
        val next = order(2, "2027-02-02T10:00:00Z")
        val origin = Instant.parse("2027-02-01T09:59:55Z")
        val clock = object : Clock {
            override fun now() = origin + testScheduler.currentTime.milliseconds
        }
        val repository = TestRepository()
        val store = upcomingStartsStore(repository, clock)
        val labels = mutableListOf<Label>()
        val subscription = launch { store.labels.collect { labels += it } }
        try {
            store.accept(Intent.Resume)
            runCurrent()
            assertIs<Content.Guest>(store.state.content)
            store.accept(Intent.OpenHistory)
            runCurrent()
            assertEquals(listOf<Label>(Label.SignIn(openHistoryAfter = true)), labels)
            labels.clear()

            repository.orders = listOf(first, next)
            store.accept(Intent.Resume)
            runCurrent()
            val content = assertIs<Content.Ready>(store.state.content)
            assertEquals(listOf(1, 2), content.registrations.map { it.order.id })
            store.accept(Intent.OpenRegistration(first.id))
            runCurrent()
            assertEquals(listOf<Label>(Label.Details(first)), labels)
            labels.clear()

            advanceTimeBy(5_000)
            runCurrent()
            assertEquals(listOf(2), content.registrations.filter { it.expires > store.state.now }.map { it.order.id })
            store.accept(Intent.OpenRegistration(first.id))
            store.accept(Intent.OpenHistory)
            runCurrent()
            assertEquals(listOf<Label>(Label.History), labels)

            store.accept(Intent.ClearSession)
            runCurrent()
            assertIs<Content.Guest>(store.state.content)
        } finally {
            subscription.cancel()
            store.dispose()
            Dispatchers.resetMain()
        }
    }

    private class TestRepository : StartOrderRegistrationRepository {
        var orders: List<StartOrderInfo>? = null
        override fun registrations(forced: Boolean, allowCachedOnError: Boolean): Flow<List<StartOrderInfo>> = flow {
            emit(orders ?: throw AuthException("Требуется вход"))
        }

        override suspend fun currentUser() = error("Unexpected user lookup")
        override suspend fun pay(id: Int) = error("Unexpected payment")
        override suspend fun getPrice(orderId: Int, orderDistancesId: List<String>, startId: Int) = error("Unexpected price lookup")
    }
}
