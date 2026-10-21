package com.markettwits.sportsouce.auth.flow

import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.validation.SignUpValidationBase
import kotlin.test.*

internal val validSignUp = SignUpStatement(
    name = "Иван", surname = "Иванов", sex = "Мужской", birthday = "17.05.2005",
    phone = "+7 (913) 123-45-67", email = "ivan@example.ru", password = "secret123", repeatPassword = "secret123",
)

class SignUpValidationTest {
    private val validator = SignUpValidationBase()
    private val all = SignUpStage.entries.toSet()

    @Test fun allRequiredFieldsHaveTheirOwnMessage() {
        val errors = validator.errors(SignUpStatement(), all)
        assertEquals(8, listOf(errors.phone, errors.email, errors.name, errors.surname,
            errors.sex, errors.birthday, errors.password, errors.repeatPassword).count { it != null })
        assertEquals(SignUpStage.FIRST, errors.firstInvalidStage())
        assertTrue(validator.validate(SignUpStatement()).isFailure)
    }

    @Test fun validationChecksOnlyTheRequestedSteps() {
        val contacts = validator.errors(SignUpStatement(), setOf(SignUpStage.FIRST))
        assertNotNull(contacts.phone); assertNotNull(contacts.email)
        assertNull(contacts.name); assertNull(contacts.password)
        assertNull(validator.errors(validSignUp, all).firstInvalidStage())
        assertEquals(validSignUp, validator.validate(validSignUp).getOrThrow())
    }

    @Test fun contactValidationCannotPassIncompletePhoneOrMalformedEmail() {
        for (phone in listOf("1234567890", "+7 (913) 123", "+7 (913) 123-45-678")) {
            assertNotNull(validator.errors(validSignUp.copy(phone = phone), all).phone)
        }
        for (email in listOf("a@.ru", "a@mail..ru", "name mail@site.ru", "a@b", "a@@b.ru")) {
            assertNotNull(validator.errors(validSignUp.copy(email = email), all).email, email)
        }
        assertNull(validator.errors(validSignUp.copy(email = "ivan+sport@example.technology"), all).email)
    }

    @Test fun personalNamesAcceptCyrillicIncludingYoAndCompoundNames() {
        for (value in listOf("Иван", "Ёлкин", "Алёна", "Анна-Мария", "Ван Дер Бек")) {
            val statement = validSignUp.copy(name = value, surname = value)
            val errors = validator.errors(statement, all)
            assertNull(errors.name, value); assertNull(errors.surname, value)
            assertTrue(validator.validate(statement).isSuccess, value)
        }
    }

    @Test fun personalNamesRejectLatinMixedAlphabetsDigitsAndSymbols() {
        for (value in listOf("Ivan", "Ивaн", "Иван1", "Иван_Иванов", "Иван!", "🙂", "-", " Иван", "Иван-")) {
            val statement = validSignUp.copy(name = value, surname = value)
            val errors = validator.errors(statement, all)
            assertTrue(errors.name!!.contains("кириллицу"), value)
            assertTrue(errors.surname!!.contains("кириллицу"), value)
            assertTrue(validator.validate(statement).isFailure, value)
        }
    }

    @Test fun passwordsHaveSeparateLengthWhitespaceAndConfirmationErrors() {
        val short = validator.errors(validSignUp.copy(password = "abc", repeatPassword = "abc"), all)
        assertTrue(short.password!!.contains("6")); assertNull(short.repeatPassword)
        val space = validator.errors(validSignUp.copy(password = "abc def", repeatPassword = "abc def"), all)
        assertTrue(space.password!!.contains("пробелов"))
        val mismatch = validator.errors(validSignUp.copy(repeatPassword = "different"), all)
        assertNull(mismatch.password); assertEquals("Пароли не совпадают", mismatch.repeatPassword)
        assertNotNull(validator.errors(validSignUp.copy(name = "  ", surname = "  "), all).name)
    }
}
