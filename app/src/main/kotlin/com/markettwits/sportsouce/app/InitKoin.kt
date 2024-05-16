package com.markettwits.sportsouce.app

import android.app.Application
import android.content.Context
import com.arkivanov.decompose.ComponentContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(
    modules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(modules)
}

internal fun KoinApplication.decomposeComponentContext(
    componentContext: ComponentContext
): KoinApplication {
    koin.loadModules(listOf(module {
        single { componentContext } bind ComponentContext::class
    }))
    return this
}