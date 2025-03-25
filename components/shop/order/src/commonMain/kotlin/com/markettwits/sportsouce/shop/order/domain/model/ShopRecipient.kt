package com.markettwits.sportsouce.shop.order.domain.model

data class ShopRecipient(val name : String, val phone : String)

internal fun ShopRecipient.isValid() : Result<Unit> = runCatching {
    if (name.isEmpty() || name.length < 3)
        throw IllegalArgumentException("Имя заказчика не может быть меньше 3-х символов или пустым")
    if (validateRussianNumber(phone))
        throw IllegalArgumentException("Введеите корректный номер телефона в формате:\n+7 (000) 000-00-00")
}

private fun validateRussianNumber(phoneNumber : String) : Boolean{
    val regexPattern = """\+7 \(\d{3}\) \d{3}-\d{2}-\d{2}"""
    return !Regex(regexPattern).matches(phoneNumber)
}