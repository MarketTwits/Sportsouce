package com.markettwits.profile.root

import com.arkivanov.decompose.ComponentContext

class RootAuthorizedProfileComponentBase(
    componentContext: ComponentContext,
    private val signOut: () -> Unit
) : RootAuthorizedProfileComponentAbstract(componentContext, signOut)