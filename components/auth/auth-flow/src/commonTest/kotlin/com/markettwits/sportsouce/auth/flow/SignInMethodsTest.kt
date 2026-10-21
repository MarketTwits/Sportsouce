package com.markettwits.sportsouce.auth.flow

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.SignInUseCase
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore.Intent
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStoreFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class SignInMethodsTest {
    @BeforeTest fun installMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    private val user = User(address = null, age = null, birthday = "2005-05-17", commentForAddress = null,
        createdAt = "", email = "ivan@example.ru", id = 1, instagram = null, name = "Иван", number = "79131234567",
        patronymic = null, photoId = null, sex = "Мужской", surname = "Иванов", team = null,
        telegram = null, vk = null, whatsapp = null)
    private val tracker = object : ExceptionTracker {
        override fun reportException(exception: Throwable, key: String?) = Unit
        override fun setLog(message: String) = Unit
        override fun setUserId(userId: String) = Unit
        override fun setKey(key: Pair<String, String>) = Unit
    }
    private fun store(authenticate: suspend (String, String) -> Result<User>) =
        SignInStoreFactory(DefaultStoreFactory(), object : SignInUseCase {
            override suspend fun signIn(email: String, password: String) = authenticate(email, password)
        }, tracker).create()

    @Test fun vkNeverAuthenticatesOrCallsThePasswordEndpointEvenWithFilledFields() {
        var calls = 0
        val store = store { _, _ -> calls++; Result.success(user) }
        try {
            store.accept(Intent.UpdateEmailOrPhone("ivan@example.ru"))
            store.accept(Intent.UpdatePassword("secret123"))
            assertTrue(store.state.enabled)
            store.accept(Intent.SetLoginMethod(LoginMethod.VK))
            assertFalse(store.state.enabled)
            repeat(3) { store.accept(Intent.Login) }
            assertEquals(0, calls); assertFalse(store.state.isLoading)
            assertNull(store.state.emailOrPhoneError); assertNull(store.state.passwordError)
        } finally { store.dispose() }
    }

    @Test fun switchingBackRestoresThePasswordDraftAndLoginAvailability() {
        val store = store { _, _ -> error("Only switching tabs") }
        try {
            store.accept(Intent.UpdateEmailOrPhone("ivan@example.ru"))
            store.accept(Intent.UpdatePassword("secret123"))
            store.accept(Intent.SetLoginMethod(LoginMethod.VK))
            store.accept(Intent.SetLoginMethod(LoginMethod.PASSWORD))
            assertEquals("ivan@example.ru", store.state.emailOrPhone)
            assertEquals("secret123", store.state.password)
            assertTrue(store.state.enabled)
        } finally { store.dispose() }
    }

    @Test fun passwordLoginStillUsesTheEnteredEmailOrNormalizedPhone() {
        for ((input, expected) in listOf("ivan@example.ru" to "ivan@example.ru", "+7 (913) 123-45-67" to "79131234567")) {
            var submitted: Pair<String, String>? = null
            val store = store { login, password -> submitted = login to password; Result.success(user) }
            try {
                store.accept(Intent.UpdateEmailOrPhone(input)); store.accept(Intent.UpdatePassword("secret123"))
                store.accept(Intent.Login)
                assertEquals(expected to "secret123", submitted)
                assertFalse(store.state.isLoading)
            } finally { store.dispose() }
        }
    }

    @Test fun invalidPasswordFormStaysInPlaceAndShowsFieldErrors() {
        val store = store { _, _ -> error("Invalid form reached API") }
        try {
            store.accept(Intent.UpdateEmailOrPhone("invalid"))
            store.accept(Intent.Login)
            assertNotNull(store.state.emailOrPhoneError); assertNotNull(store.state.passwordError)
            assertFalse(store.state.enabled); assertFalse(store.state.isLoading)
        } finally { store.dispose() }
    }

    @Test fun loginInFlightCannotBeResubmittedOrSwitchedToVk() = runTest {
        val pending = CompletableDeferred<Result<User>>()
        var calls = 0
        val store = store { _, _ -> calls++; pending.await() }
        try {
            store.accept(Intent.UpdateEmailOrPhone("ivan@example.ru")); store.accept(Intent.UpdatePassword("secret123"))
            store.accept(Intent.Login); store.accept(Intent.Login)
            store.accept(Intent.SetLoginMethod(LoginMethod.VK))
            assertEquals(1, calls); assertTrue(store.state.isLoading)
            assertEquals(LoginMethod.PASSWORD, store.state.loginMethod)
            pending.complete(Result.success(user))
            assertFalse(store.state.isLoading)
        } finally { store.dispose() }
    }
}
