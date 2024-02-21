package com.markettwits.profile.presentation.deprecated

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.model.auth.sign_in.response.User

object AuthorizedProfileComponentMock : AuthorizedProfile {

    override fun goEditScreen() {

    }

    override fun goChangePasswordScreen() {

    }

    override fun goMyRegistryScreen() {

    }

    override fun goMyMembersScreen() {

    }

    override fun signOut() {

    }

    private val fakeUser = User(
        address = "123 Main St",
        age = "30",
        birthday = "1994-05-21",
        comment_for_address = "This is my home address",
        createdAt = "2024-02-20T10:00:00",
        email = "example@example.com",
        favor = "StackOverflow",
        id = 1,
        instagram = "example_instagram",
        name = "John",
        number = "+1234567890",
        patronymic = "Middle",
        photo_id = 1,
        photo = User.Photo(id = 1, name = "profile_pic", path = "/images"),
        role = "user",
        sex = "male",
        surname = "Doe",
        team = "Team A",
        telegram = "example_telegram",
        updatedAt = "2024-02-20T10:30:00",
        verification_code = "123456",
        verified = true,
        vk = "example_vk",
        whatsapp = "+1234567890"
    )
    override val profileName: Value<ProfileUiState> = MutableValue(
        ProfileUiState.Base(
            fakeUser
        )
    )
}