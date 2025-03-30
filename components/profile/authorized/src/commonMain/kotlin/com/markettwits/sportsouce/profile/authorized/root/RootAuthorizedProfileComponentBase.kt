package com.markettwits.sportsouce.profile.authorized.root

import com.arkivanov.decompose.ComponentContext

class RootAuthorizedProfileComponentBase(
    componentContext: ComponentContext,
    signOut: () -> Unit
) : RootAuthorizedProfileComponentAbstract(componentContext, signOut)