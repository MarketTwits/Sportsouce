package com.markettwits.sportsouce.auth.flow.internal.sign_up.domain

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val signUpCredentialSaverModule = module {
    singleOf(::NoOpRegisteredCredentialSaver) bind RegisteredCredentialSaver::class
}
