package com.markettwits.sportsouce.auth.flow.internal.sign_in.domain

object ValidationUtils {

    // Email validation regex
    private val emailRegex = Regex(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    )

    // Phone number regex patterns
    private val phoneDigitsRegex = Regex("[^0-9]")

    /**
     * Determines input type based on content
     */
    fun detectInputType(input: String): InputType {
        if (input.isBlank()) return InputType.UNKNOWN

        return when {
            input.contains("@") -> InputType.EMAIL
            input.any { it.isDigit() } || input.startsWith("+") -> InputType.PHONE
            else -> InputType.UNKNOWN
        }
    }

    /**
     * Validates email format
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "Введите email или номер телефона")
            !emailRegex.matches(email) -> ValidationResult(false, "Введите корректный email")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates phone number and returns formatted result
     */
    fun validatePhone(phone: String): ValidationResult {
        val cleanPhone = cleanPhoneNumber(phone)

        return when {
            phone.isBlank() -> ValidationResult(false, "Введите email или номер телефона")
            cleanPhone.length < 10 -> ValidationResult(false, "Введите корректный номер телефона")
            cleanPhone.length > 11 -> ValidationResult(false, "Введите корректный номер телефона")
            cleanPhone.length == 10 -> ValidationResult(true) // Will be formatted to 7XXXXXXXXXX
            cleanPhone.length == 11 && (cleanPhone.startsWith("7") || cleanPhone.startsWith("8")) -> ValidationResult(
                true
            )

            else -> ValidationResult(false, "Введите корректный номер телефона")
        }
    }

    /**
     * Validates password
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "Пароль не может быть пустым")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates SMS code
     */
    fun validateSmsCode(smsCode: String): ValidationResult {
        val cleanCode = smsCode.replace(Regex("[^0-9]"), "")
        return when {
            smsCode.isBlank() -> ValidationResult(false, "Введите код из СМС")
            cleanCode.length < 4 -> ValidationResult(false, "Код должен содержать минимум 4 цифры")
            cleanCode.length > 6 -> ValidationResult(false, "Код должен содержать максимум 6 цифр")
            else -> ValidationResult(true)
        }
    }

    /**
     * Formats phone number for display during input
     */
    fun formatPhoneForDisplay(phone: String): String {
        val cleanPhone = cleanPhoneNumber(phone)

        return when {
            cleanPhone.isEmpty() -> ""
            cleanPhone.length <= 1 -> "+7 ($cleanPhone"
            cleanPhone.length <= 4 -> "+7 (${
                cleanPhone.substring(
                    0,
                    minOf(3, cleanPhone.length)
                )
            }${if (cleanPhone.length > 3) ") ${cleanPhone.substring(3)}" else ""}"

            cleanPhone.length <= 7 -> "+7 (${cleanPhone.substring(0, 3)}) ${cleanPhone.substring(3)}"
            cleanPhone.length <= 9 -> "+7 (${cleanPhone.substring(0, 3)}) ${
                cleanPhone.substring(
                    3,
                    6
                )
            }-${cleanPhone.substring(6)}"

            else -> "+7 (${cleanPhone.substring(0, 3)}) ${cleanPhone.substring(3, 6)}-${
                cleanPhone.substring(
                    6,
                    8
                )
            }-${cleanPhone.substring(8, minOf(10, cleanPhone.length))}"
        }
    }

    /**
     * Formats phone number for API (79130312312 format)
     */
    fun formatPhoneForApi(phone: String): String {
        val cleanPhone = cleanPhoneNumber(phone)

        return when {
            cleanPhone.length == 10 -> "7$cleanPhone"
            cleanPhone.length == 11 && cleanPhone.startsWith("8") -> "7${cleanPhone.substring(1)}"
            cleanPhone.length == 11 && cleanPhone.startsWith("7") -> cleanPhone
            else -> cleanPhone
        }
    }

    /**
     * Removes all non-digit characters from phone number
     */
    private fun cleanPhoneNumber(phone: String): String {
        return phone.replace(phoneDigitsRegex, "").let { cleaned ->
            when {
                cleaned.startsWith("8") && cleaned.length == 11 -> cleaned.substring(1)
                cleaned.startsWith("7") && cleaned.length == 11 -> cleaned.substring(1)
                cleaned.startsWith("7") && cleaned.length == 10 -> cleaned
                else -> cleaned
            }
        }
    }

    /**
     * Validates input based on detected type
     */
    fun validateInput(input: String, inputType: InputType): ValidationResult {
        return when (inputType) {
            InputType.EMAIL -> validateEmail(input)
            InputType.PHONE -> validatePhone(input)
            InputType.UNKNOWN -> {
                if (input.isBlank()) {
                    ValidationResult(false, "Введите email или номер телефона")
                } else {
                    ValidationResult(false, "Введите корректный email или номер телефона")
                }
            }
        }
    }
}