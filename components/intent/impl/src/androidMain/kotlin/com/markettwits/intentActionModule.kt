package com.markettwits

import android.content.Context
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

actual val intentActionModule = module {
    val context: Context by KoinJavaComponent.inject(Context::class.java)
    single<Context> { context }
    singleOf(::IntentActionBase) bind IntentAction::class
}