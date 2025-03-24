package com.markettwits

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val intentActionModule = module {
    singleOf(::IntentActionBase) bind IntentAction::class
}