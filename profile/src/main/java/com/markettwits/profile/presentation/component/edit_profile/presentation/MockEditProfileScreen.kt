package com.markettwits.profile.presentation.component.edit_profile.presentation

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.model.auth.sign_in.response.User

class MockEditProfileScreen : EditProfile {
    val fakeUser = User(
        address = "123 Main St",
        age = "25",
        birthday = "1998-05-15",
        comment_for_address = "This is a sample address",
        createdAt = "2023-01-01T12:00:00",
        email = "user@example.com",
        favor = "Sample favor",
        id = 1,
        instagram = "sample_instagram",
        name = "John",
        number = "+1234567890",
        patronymic = "Middle",
        photo_id = "photo123",
        role = "user",
        sex = "male",
        surname = "Doe",
        team = "Sample Team",
        telegram = "sample_telegram",
        updatedAt = "2023-01-02T14:30:00",
        verification_code = "123456",
        verified = true,
        vk = "sample_vk",
        whatsapp = "+9876543210"
    )
    override val user: Value<User> = MutableValue(fakeUser)

    override fun pop() {

    }

    override fun saveChanges() {

    }

    override fun obtainTextFiled(value: EditProfileUiPage) = Unit

    override val page: Value<List<EditProfileUiPage>> = MutableValue(emptyList())
}