package com.markettwits.sportsouce.start.support

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.IntentAction
import com.markettwits.sportsouce.start.support.domain.StartDonationCheck
import com.markettwits.sportsouce.start.support.domain.StartSupportUseCase
import com.markettwits.sportsouce.start.support.domain.SupportAuthorizationRequired
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore.*
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStoreFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class DonationCheckStoreTest {
    private val none = StartDonationCheck("none", null, null)
    private val unpaid = StartDonationCheck("registered_unpaid", StartDonationCheck.Registration(52803, 0, 50000, true), null)
    @BeforeTest fun setup() { Dispatchers.setMain(StandardTestDispatcher()) }
    @AfterTest fun cleanup() { Dispatchers.resetMain() }

    @Test fun noRequestUntilOpenAndEachReopenChecksAgainWithoutSubmitting() = runTest {
        val useCase = FakeUseCase()
        val urls = mutableListOf<String>()
        val store = store(useCase, urls)
        try {
            store.accept(Intent.ApplyStartId(326)); runCurrent()
            assertTrue(useCase.checks.isEmpty())
            store.accept(Intent.OnOpen); runCurrent()
            assertEquals(listOf(326), useCase.checks)
            assertEquals(CheckState.Loading, store.state.checkState)
            store.accept(Intent.OnChangeValue("500")); store.accept(Intent.OnClickSupport); runCurrent()
            assertEquals(0, useCase.donations)
            useCase.response.complete(Result.success(none)); advanceUntilIdle()
            assertEquals(CheckState.Ready(none), store.state.checkState)
            store.accept(Intent.OnClose)
            useCase.response = CompletableDeferred()
            store.accept(Intent.OnOpen); runCurrent()
            assertEquals(listOf(326, 326), useCase.checks)
            assertEquals(CheckState.Loading, store.state.checkState)
            assertFalse(store.state.donationConfirmed)
            assertTrue(urls.isEmpty())
            useCase.response.complete(Result.success(none)); advanceUntilIdle()
        } finally { store.dispose() }
    }

    @Test fun unpaidRequiresSeparateConsentAndPaymentUsesTheExistingRegistrationOnly() = runTest {
        val useCase = FakeUseCase().apply { response.complete(Result.success(unpaid)) }
        val urls = mutableListOf<String>()
        val store = store(useCase, urls)
        try {
            store.accept(Intent.ApplyStartId(326)); store.accept(Intent.OnOpen); advanceUntilIdle()
            store.accept(Intent.OnChangeValue("500")); store.accept(Intent.OnClickSupport); advanceUntilIdle()
            assertEquals(0, useCase.donations)
            store.accept(Intent.OnPayRegistration); store.accept(Intent.OnPayRegistration); runCurrent()
            assertEquals(listOf(52803), useCase.payments)
            assertEquals(0, useCase.donations)
            useCase.payment.complete(Result.success("https://payments.example.test/registration")); advanceUntilIdle()
            assertEquals(listOf("https://payments.example.test/registration"), urls)
            store.accept(Intent.OnClose); store.accept(Intent.OnOpen); advanceUntilIdle()
            store.accept(Intent.OnConfirmDonation); store.accept(Intent.OnClickSupport); store.accept(Intent.OnClickSupport); runCurrent()
            assertEquals(1, useCase.donations)
            useCase.donation.complete(Result.success("https://payments.example.test/donation")); advanceUntilIdle()
            assertEquals("https://payments.example.test/donation", urls.last())
        } finally { store.dispose() }
    }

    @Test fun failedCheckMustRetryAndClosingOrChangingStartDiscardsLateResponses() = runTest {
        val useCase = FakeUseCase().apply { response.complete(Result.failure(Exception("offline"))) }
        val urls = mutableListOf<String>()
        val store = store(useCase, urls)
        try {
            store.accept(Intent.ApplyStartId(315)); store.accept(Intent.OnOpen); advanceUntilIdle()
            assertEquals(CheckState.Failed, store.state.checkState)
            store.accept(Intent.OnConfirmDonation); store.accept(Intent.OnChangeValue("500")); store.accept(Intent.OnClickSupport)
            assertEquals(0, useCase.donations)
            useCase.response = CompletableDeferred()
            store.accept(Intent.OnRetryCheck); runCurrent()
            val old = useCase.response
            useCase.response = CompletableDeferred()
            store.accept(Intent.ApplyStartId(326)); runCurrent()
            old.complete(Result.success(none)); runCurrent()
            assertEquals(CheckState.Loading, store.state.checkState)
            useCase.response.complete(Result.success(unpaid)); advanceUntilIdle()
            assertEquals(CheckState.Ready(unpaid), store.state.checkState)
            store.accept(Intent.OnConfirmDonation); store.accept(Intent.OnChangeValue("500")); store.accept(Intent.OnClickSupport); runCurrent()
            store.accept(Intent.OnClose)
            useCase.donation.complete(Result.success("https://payments.example.test/stale")); advanceUntilIdle()
            assertTrue(urls.isEmpty())
            assertEquals(CheckState.Idle, store.state.checkState)
        } finally { store.dispose() }
    }

    @Test fun unavailablePaymentAndInvalidAmountsCannotStartPaymentRequests() = runTest {
        val useCase = FakeUseCase().apply { response.complete(Result.success(unpaid.copy(registration = unpaid.registration!!.copy(canPay = false)))) }
        val store = store(useCase, mutableListOf())
        try {
            store.accept(Intent.ApplyStartId(326)); store.accept(Intent.OnOpen); advanceUntilIdle()
            store.accept(Intent.OnPayRegistration); assertTrue(useCase.payments.isEmpty())
            store.accept(Intent.OnConfirmDonation); store.accept(Intent.OnChangeValue("0")); store.accept(Intent.OnClickSupport); runCurrent()
            assertNotNull(store.state.amountError)
            assertEquals(0, useCase.donations)
        } finally { store.dispose() }
    }

    @Test fun guestCanOpenLoginAndReturningChecksTheNewSessionAgain() = runTest {
        val useCase = FakeUseCase().apply { response.complete(Result.failure(SupportAuthorizationRequired())) }
        val store = store(useCase, mutableListOf())
        val labels = mutableListOf<Label>()
        val collector = store.labels.onEach { labels += it }.launchIn(backgroundScope)
        try {
            runCurrent()
            store.accept(Intent.ApplyStartId(326)); store.accept(Intent.OnOpen); advanceUntilIdle()
            assertEquals(CheckState.AuthorizationRequired, store.state.checkState)
            store.accept(Intent.OnChangeValue("500")); store.accept(Intent.OnConfirmDonation)
            store.accept(Intent.OnClickSupport); store.accept(Intent.OnPayRegistration); runCurrent()
            assertEquals(0, useCase.donations); assertTrue(useCase.payments.isEmpty())
            store.accept(Intent.OnSignIn); runCurrent()
            assertEquals(listOf<Label>(Label.SignIn), labels)
            val firstSession = store.state.sheetSession
            assertFalse(store.state.isOpen, "The sheet must be removed before navigation to login")
            assertTrue(store.state.awaitingAuth)
            useCase.response = CompletableDeferred<Result<StartDonationCheck>>().apply { complete(Result.success(unpaid)) }
            store.accept(Intent.OnAuthReturned); advanceUntilIdle()
            assertTrue(store.state.isOpen)
            assertTrue(store.state.sheetSession > firstSession, "Return must create a fresh sheet animation state")
            store.accept(Intent.OnAuthReturned); advanceUntilIdle()
            assertEquals(CheckState.Ready(unpaid), store.state.checkState)
            assertFalse(store.state.donationConfirmed)
            assertEquals(listOf(326, 326), useCase.checks)
            store.accept(Intent.OnSignIn); runCurrent()
            assertEquals(1, labels.size, "A valid session must not trigger login")
            store.accept(Intent.OnPayRegistration); runCurrent()
            useCase.payment.complete(Result.failure(SupportAuthorizationRequired())); advanceUntilIdle()
            assertEquals(CheckState.AuthorizationRequired, store.state.checkState)
            assertFalse(store.state.isLoading)
        } finally { collector.cancel(); store.dispose() }
    }

    private fun store(useCase: StartSupportUseCase, urls: MutableList<String>): StartSupportStore =
        StartSupportStoreFactory(DefaultStoreFactory(), useCase, object : IntentAction {
            override fun openWebPage(url: String) { urls += url }
            override fun openPhone(phone: String) = Unit
            override fun shareImage(byteArray: ByteArray, filename: String) = Unit
            override fun saveImage(byteArray: ByteArray, filename: String) = Unit
            override fun copyToSystemBuffer(text: String) = Unit
            override fun sharePlainText(text: String) = Unit
        }).create()

    private class FakeUseCase : StartSupportUseCase {
        var response = CompletableDeferred<Result<StartDonationCheck>>()
        var payment = CompletableDeferred<Result<String>>()
        var donation = CompletableDeferred<Result<String>>()
        val checks = mutableListOf<Int>()
        val payments = mutableListOf<Int>()
        var donations = 0
        override suspend fun checkDonation(startId: Int): Result<StartDonationCheck> {
            checks += startId
            val captured = response
            // Simulate an HTTP transport completing despite cancellation.
            return withContext(NonCancellable) { captured.await() }
        }
        override suspend fun payRegistration(groupId: Int): Result<String> { payments += groupId; return payment.await() }
        override suspend fun donation(startId: Int, price: String): Result<String> { donations++; return donation.await() }
        override fun validate(price: String): Result<Int> = price.toIntOrNull()?.takeIf { it in 1..99999 }?.let { Result.success(it) }
            ?: Result.failure(IllegalArgumentException("Проверьте сумму пожертвования"))
    }
}
