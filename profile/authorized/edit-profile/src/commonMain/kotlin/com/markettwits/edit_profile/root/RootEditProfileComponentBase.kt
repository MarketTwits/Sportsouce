package com.markettwits.edit_profile.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.edit_profile.edit_menu.presentation.component.EditProfileMenuComponentComponent
import com.markettwits.edit_profile.edit_menu.presentation.component.EditProfileMenuComponentComponentBase
import com.markettwits.edit_profile.edit_menu.presentation.component.EditProfileMenuOutputProvide
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStoreFactory
import com.markettwits.edit_profile.edit_profile_Image.di.editProfileImageModule
import com.markettwits.edit_profile.edit_profile_Image.presentation.component.EditProfileImageComponentBase
import com.markettwits.edit_profile.edit_profile_about.di.editProfileAboutModule
import com.markettwits.edit_profile.edit_profile_about.presentation.component.EditProfileAboutComponentBase
import com.markettwits.edit_profile.edit_profile_change_password.di.changePasswordModule
import com.markettwits.edit_profile.edit_profile_change_password.presentation.screen.ChangePasswordComponent
import com.markettwits.edit_profile.edit_profile_info.di.editProfileInfoModule
import com.markettwits.edit_profile.edit_profile_info.presentation.component.EditProfileInfoComponentBase
import com.markettwits.edit_profile.edit_profile_sign_out.di.editProfileSignOutModule
import com.markettwits.edit_profile.edit_profile_sign_out.presentation.component.EditProfileSignOutComponentBase
import com.markettwits.edit_profile.edit_social_network.di.editProfileSocialNetworkModule
import com.markettwits.edit_profile.edit_social_network.presentation.component.EditProfileSocialNetworkComponentBase

class RootEditProfileComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
    private val signOut: () -> Unit
) : RootEditProfileComponent,
    EditProfileMenuOutputProvide, ComponentContext by componentContext {
    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(
            sportSouceNetworkModule,
            changePasswordModule,
            editProfileSocialNetworkModule,
            editProfileInfoModule,
            editProfileImageModule,
            editProfileAboutModule,
            editProfileSignOutModule
        )
    )
    private val navigation = StackNavigation<RootEditProfileComponent.ConfigStack>()
    private val slotNavigation = SlotNavigation<RootEditProfileComponent.ConfigSlot>()

    override val childSlot: Value<ChildSlot<*, RootEditProfileComponent.ChildSlot>> = childSlot(
        source = slotNavigation,
        serializer = RootEditProfileComponent.ConfigSlot.serializer(),
        handleBackButton = true,
        childFactory = ::childSlot
    )
    override val childStack: Value<ChildStack<*, RootEditProfileComponent.ChildStack>> = childStack(
        source = navigation,
        serializer = RootEditProfileComponent.ConfigStack.serializer(),
        initialConfiguration = RootEditProfileComponent.ConfigStack.EditProfileMenu,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun childSlot(
        configStack: RootEditProfileComponent.ConfigSlot,
        componentContext: ComponentContext
    ): RootEditProfileComponent.ChildSlot = when (configStack) {
        is RootEditProfileComponent.ConfigSlot.EditProfileAbout -> RootEditProfileComponent.ChildSlot.EditProfileAbout(
            EditProfileAboutComponentBase(
                componentContext = componentContext,
                storeFactory = scope.get(),
                pop = slotNavigation::dismiss,
            )
        )

        is RootEditProfileComponent.ConfigSlot.EditProfileImage -> RootEditProfileComponent.ChildSlot.EditProfileImage(
            EditProfileImageComponentBase(
                componentContext = componentContext,
                storeFactory = scope.get(),
                pop = slotNavigation::dismiss
            )
        )

        RootEditProfileComponent.ConfigSlot.EditProfileSignOut -> RootEditProfileComponent.ChildSlot.EditProfileSignOut(
            EditProfileSignOutComponentBase(
                componentContext = componentContext,
                storeFactory = scope.get(),
                dismiss = {
                    slotNavigation.dismiss()
                },
                signOut = {
                    slotNavigation.dismiss()
                    signOut()
                }
            ))
    }

    private fun child(
        configStack: RootEditProfileComponent.ConfigStack,
        componentContext: ComponentContext,
    ): RootEditProfileComponent.ChildStack =
        when (configStack) {
            is RootEditProfileComponent.ConfigStack.ChangePassword -> RootEditProfileComponent.ChildStack.ChangePassword(
                ChangePasswordComponent(
                    context = componentContext,
                    pop = navigation::pop,
                    storeFactory = scope.get()
                )
            )

            is RootEditProfileComponent.ConfigStack.EditProfileMenu -> RootEditProfileComponent.ChildStack.EditProfileMenu(
                EditProfileMenuComponentComponentBase(
                    componentContext = componentContext,
                    storeFactory = EditProfileMenuStoreFactory(DefaultStoreFactory()),
                    output = ::obtainOutPut
                )
            )

            is RootEditProfileComponent.ConfigStack.SocialNetwork -> RootEditProfileComponent.ChildStack.SocialNetwork(
                EditProfileSocialNetworkComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    pop = navigation::pop
                )
            )

            RootEditProfileComponent.ConfigStack.EditProfileInfo -> RootEditProfileComponent.ChildStack.EditProfileInfo(
                EditProfileInfoComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    pop = navigation::pop
                )
            )
        }

    override fun obtainOutPut(output: EditProfileMenuComponentComponent.OutPut) {
        when (output) {
            is EditProfileMenuComponentComponent.OutPut.GoBack -> pop()
            is EditProfileMenuComponentComponent.OutPut.GoChangePassword -> navigation.push(
                RootEditProfileComponent.ConfigStack.ChangePassword
            )

            is EditProfileMenuComponentComponent.OutPut.GoSocialNetwork -> navigation.push(
                RootEditProfileComponent.ConfigStack.SocialNetwork
            )

            is EditProfileMenuComponentComponent.OutPut.GoChangeProfileInfo -> navigation.push(
                RootEditProfileComponent.ConfigStack.EditProfileInfo
            )

            is EditProfileMenuComponentComponent.OutPut.GoProfileAbout -> slotNavigation.activate(
                RootEditProfileComponent.ConfigSlot.EditProfileAbout
            )

            is EditProfileMenuComponentComponent.OutPut.GoProfileImage -> slotNavigation.activate(
                RootEditProfileComponent.ConfigSlot.EditProfileImage
            )

            is EditProfileMenuComponentComponent.OutPut.GoSignOut -> slotNavigation.activate(
                RootEditProfileComponent.ConfigSlot.EditProfileSignOut
            )
        }
    }
}