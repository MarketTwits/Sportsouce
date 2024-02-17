package com.markettwits.edit_profile.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.markettwits.edit_profile.edit_menu.component.EditProfileMenuComponentComponent
import com.markettwits.edit_profile.edit_profile_Image.presentation.component.EditProfileImageComponent
import com.markettwits.edit_profile.edit_profile_about.presentation.component.EditProfileAboutComponent
import com.markettwits.edit_profile.edit_profile_info.presentation.component.EditProfileInfoComponent
import com.markettwits.edit_profile.social_network.presentation.component.EditProfileSocialNetworkComponent
import kotlinx.serialization.Serializable

interface RootEditProfileComponent {
    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<*, ChildSlot>>

    @Serializable
    sealed interface ConfigStack {
        @Serializable
        data object EditProfileMenu : ConfigStack

        @Serializable
        data object ChangePassword : ConfigStack

        @Serializable
        data object SocialNetwork : ConfigStack

        @Serializable
        data object EditProfileInfo : ConfigStack
    }

    @Serializable
    sealed interface ConfigSlot {
        @Serializable
        data object EditProfileAbout : ConfigSlot

        @Serializable
        data object EditProfileImage : ConfigSlot
    }

    sealed interface ChildSlot {
        data class EditProfileAbout(val component: EditProfileAboutComponent) : ChildSlot
        data class EditProfileImage(val component: EditProfileImageComponent) : ChildSlot
    }


    sealed interface ChildStack {
        data class EditProfileMenu(val component: EditProfileMenuComponentComponent) :
            ChildStack

        data class SocialNetwork(val component: EditProfileSocialNetworkComponent) :
            ChildStack

        data class ChangePassword(val component: com.markettwits.change_password.presentation.screen.ChangePassword) :
            ChildStack

        data class EditProfileInfo(val component: EditProfileInfoComponent) :
            ChildStack
    }
}