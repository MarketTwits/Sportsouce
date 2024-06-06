package com.markettwits.version

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

actual val applicationVersionModule: Module = module {
    val context: Context by KoinJavaComponent.inject(Context::class.java)
    single<ApplicationVersionManager> {
        ApplicationVersionManagerAndroid(context)
    }
}