package com.markettwits.sportsouce.edit_profile.root

import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.edit_profile.about.presentation.component.EditProfileAboutComponent
import com.markettwits.sportsouce.edit_profile.image.presentation.component.EditProfileImageComponent
import com.markettwits.sportsouce.edit_profile.info.presentation.component.EditProfileInfoComponent
import com.markettwits.sportsouce.edit_profile.menu.presentation.component.EditProfileMenuComponentComponent
import com.markettwits.sportsouce.edit_profile.sign_out.presentation.component.EditProfileSignOutComponent
import com.markettwits.sportsouce.edit_profile.social_network.presentation.component.EditProfileSocialNetworkComponent
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

        @Serializable
        data object EditProfileSignOut : ConfigSlot
    }

    sealed interface ChildSlot {
        data class EditProfileAbout(val component: EditProfileAboutComponent) : ChildSlot
        data class EditProfileImage(val component: EditProfileImageComponent) : ChildSlot
        data class EditProfileSignOut(val component: EditProfileSignOutComponent) : ChildSlot
    }


    sealed interface ChildStack {
        data class EditProfileMenu(val component: EditProfileMenuComponentComponent) :
            ChildStack

        data class SocialNetwork(val component: EditProfileSocialNetworkComponent) :
            ChildStack

        data class ChangePassword(val component: com.markettwits.sportsouce.edit_profile.change_password.presentation.screen.ChangePassword) :
            ChildStack

        data class EditProfileInfo(val component: EditProfileInfoComponent) :
            ChildStack
    }
}