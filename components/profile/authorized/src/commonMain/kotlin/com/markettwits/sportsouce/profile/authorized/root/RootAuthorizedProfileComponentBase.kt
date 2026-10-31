package com.markettwits.sportsouce.profile.authorized.root

import com.arkivanov.decompose.ComponentContext
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory

class RootAuthorizedProfileComponentBase(
    componentContext: ComponentContext,
    updateStoreFactory: SelfUpdateStoreFactory,
    signOut: () -> Unit
) : RootAuthorizedProfileComponentAbstract(componentContext, signOut, updateStoreFactory)
