package com.markettwits

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val intentActionModule: Module = module {
    singleOf(::EmptyIntentAction) bind IntentAction::class
}