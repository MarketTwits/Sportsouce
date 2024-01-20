package com.markettwits.change_password.domain

interface ChangePasswordValidation {
    fun changePassword(oldPassword: String, newPassword1: String, newPassword2: String): Result<String>

    class Base : ChangePasswordValidation {
        override fun changePassword(
            oldPassword: String,
            newPassword1: String,
            newPassword2: String
        ): Result<String> {
            if (oldPassword.isEmpty() || newPassword1.isEmpty() || newPassword2.isEmpty()) {
                return Result.failure(Exception("Все поля должны быть заполнены"))
            }
            if (oldPassword == newPassword1 || oldPassword == newPassword2) {
                return Result.failure(Exception("Старый пароль не может совпадать с новым паролем"))
            }
            if (newPassword1 != newPassword2) {
                return Result.failure(Exception("Проверьте введенный новый пароль"))
            }
            return Result.success("")
        }
    }
}