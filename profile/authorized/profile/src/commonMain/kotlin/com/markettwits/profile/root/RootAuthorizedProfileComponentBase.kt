package com.markettwits.profile.root

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
import com.markettwits.edit_profile.edit_social_network.presentation.component.EditProfileSocialNetworkComponentBase
import com.markettwits.edit_profile.root.RootEditProfileComponentBase
import com.markettwits.getOrCreateKoinScope
import com.markettwits.members.member_root.component.RootMembersComponentBase
import com.markettwits.profile.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.profile.authorized.presentation.component.AuthorizedProfileComponentBase
import com.markettwits.registrations.detail.component.StartOrderComponentBase
import com.markettwits.registrations.list.presentation.component.RegistrationsComponentBase
import com.markettwits.registrations.list.presentation.store.RegistrationsDataStoreFactory
import com.markettwits.registrations.root.RootRegistrationsComponentBase
import com.markettwits.settings.root.RootSettingsComponentBase
import com.markettwits.start.root.RootStartScreenComponentBase

class RootAuthorizedProfileComponentBase(
    componentContext: ComponentContext,
    private val signOut: () -> Unit
) : RootAuthorizedProfileComponentAbstract(componentContext, signOut)