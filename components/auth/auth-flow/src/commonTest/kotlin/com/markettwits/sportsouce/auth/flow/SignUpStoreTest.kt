package com.markettwits.sportsouce.auth.flow

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core_ui.items.event.isTriggered
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.use_case.SignUpUseCase
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.validation.SignUpValidationBase
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStore
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStoreFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpStoreTest {
    @BeforeTest fun installMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    private fun store(useCase: SignUpUseCase = object : SignUpUseCase {
        override suspend fun registry(signUpStatement: SignUpStatement): Result<Unit> = error("Invalid form reached API")
    }) = SignUpStoreFactory(DefaultStoreFactory(), object : ExceptionTracker {
        override fun reportException(exception: Throwable, key: String?) = Unit
        override fun setLog(message: String) = Unit
        override fun setUserId(userId: String) = Unit
        override fun setKey(key: Pair<String, String>) = Unit
    }, useCase, SignUpValidationBase()).create()

    @Test fun submissionShowsAllContactErrorsWithoutSnackbarAndClearsOnlyCorrectedFields() {
        val store = store()
        try {
            assertNull(store.state.errors.phone)
            store.accept(SignUpStore.Intent.OnClickNext)
            assertEquals(SignUpStage.FIRST, store.state.currentStage)
            assertNotNull(store.state.errors.phone); assertNotNull(store.state.errors.email)
            assertFalse(store.state.event.isTriggered())
            store.accept(SignUpStore.Intent.ChangeValue(store.state.statement.copy(phone = validSignUp.phone)))
            assertNull(store.state.errors.phone); assertNotNull(store.state.errors.email)
            assertNull(store.state.errors.name)
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp))
            store.accept(SignUpStore.Intent.OnClickNext)
            assertEquals(SignUpStage.SECOND, store.state.currentStage)
        } finally { store.dispose() }
    }

    @Test fun errorsSurviveBackNavigationAndDoNotWaitForAnotherSnackbar() {
        val store = store()
        try {
            store.accept(SignUpStore.Intent.ChangeValue(SignUpStatement(phone = validSignUp.phone, email = validSignUp.email)))
            store.accept(SignUpStore.Intent.OnClickNext)
            store.accept(SignUpStore.Intent.OnClickNext)
            assertNotNull(store.state.errors.name); assertNotNull(store.state.errors.surname)
            assertNotNull(store.state.errors.sex); assertNotNull(store.state.errors.birthday)
            assertFalse(store.state.event.isTriggered())
            store.accept(SignUpStore.Intent.OnClickBack)
            store.accept(SignUpStore.Intent.OnClickNext)
            assertNotNull(store.state.errors.name)
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp))
            assertNull(store.state.errors.firstInvalidStage())
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp.copy(name = " ")))
            assertNotNull(store.state.errors.name)
        } finally { store.dispose() }
    }

    @Test fun latinNamesBlockThePersonalStepAndErrorsClearIndependently() {
        val store = store()
        try {
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp.copy(name = "Ivan", surname = "Иван0в")))
            store.accept(SignUpStore.Intent.OnClickNext)
            store.accept(SignUpStore.Intent.OnClickNext)
            assertEquals(SignUpStage.SECOND, store.state.currentStage)
            assertNotNull(store.state.errors.name); assertNotNull(store.state.errors.surname)
            assertFalse(store.state.event.isTriggered())
            store.accept(SignUpStore.Intent.ChangeValue(store.state.statement.copy(name = "Иван")))
            assertNull(store.state.errors.name); assertNotNull(store.state.errors.surname)
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp))
            assertNull(store.state.errors.surname)
            store.accept(SignUpStore.Intent.OnClickNext)
            assertEquals(SignUpStage.THIRD, store.state.currentStage)
        } finally { store.dispose() }
    }

    @Test fun passwordAndConfirmationAreRevalidatedTogether() {
        val store = store()
        try {
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp.copy(password = "abc", repeatPassword = "different")))
            store.accept(SignUpStore.Intent.OnClickNext); store.accept(SignUpStore.Intent.OnClickNext)
            store.accept(SignUpStore.Intent.OnClickRegister)
            assertEquals(SignUpStage.THIRD, store.state.currentStage)
            assertNotNull(store.state.errors.password); assertNotNull(store.state.errors.repeatPassword)
            assertFalse(store.state.isLoading); assertFalse(store.state.event.isTriggered())
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp))
            assertNull(store.state.errors.password); assertNull(store.state.errors.repeatPassword)
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp.copy(password = "changed")))
            assertNotNull(store.state.errors.repeatPassword)
        } finally { store.dispose() }
    }

    @Test fun finalSubmissionReturnsToAnEarlierInvalidStepWithoutCallingApi() {
        val store = store()
        try {
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp))
            store.accept(SignUpStore.Intent.OnClickNext); store.accept(SignUpStore.Intent.OnClickNext)
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp.copy(email = "invalid")))
            store.accept(SignUpStore.Intent.OnClickRegister)
            assertEquals(SignUpStage.FIRST, store.state.currentStage)
            assertNotNull(store.state.errors.email)
            assertFalse(store.state.isLoading); assertFalse(store.state.event.isTriggered())
        } finally { store.dispose() }
    }

    @Test fun duplicateSubmissionAndEditsAreBlockedWhileRequestIsRunning() = runTest {
        val pending = CompletableDeferred<Result<Unit>>()
        var calls = 0
        val store = store(object : SignUpUseCase {
            override suspend fun registry(signUpStatement: SignUpStatement): Result<Unit> {
                assertEquals(validSignUp, signUpStatement)
                calls++
                return pending.await()
            }
        })
        try {
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp))
            store.accept(SignUpStore.Intent.OnClickNext); store.accept(SignUpStore.Intent.OnClickNext)
            store.accept(SignUpStore.Intent.OnClickRegister); store.accept(SignUpStore.Intent.OnClickRegister)
            assertEquals(1, calls); assertTrue(store.state.isLoading)
            store.accept(SignUpStore.Intent.ChangeValue(validSignUp.copy(email = "changed")))
            assertEquals(validSignUp, store.state.statement)
            pending.complete(Result.failure(IllegalStateException("offline")))
            assertFalse(store.state.isLoading)
            assertTrue(store.state.event.isTriggered())
            assertNull(store.state.errors.firstInvalidStage())
        } finally { store.dispose() }
    }
}
