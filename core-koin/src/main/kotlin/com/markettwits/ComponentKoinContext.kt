package com.markettwits

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

class ComponentKoinContext(private val retain: Boolean = true) : InstanceKeeper.Instance {
    private var koinApp: KoinApplication? = null

    @OptIn(KoinInternalApi::class)
    fun getOrCreateKoinScope(modules: List<Module>): Scope {
        if (koinApp == null) {
            koinApp = koinApplication { modules(modules) }
        }
        return requireNotNull(koinApp).koin.scopeRegistry.rootScope
    }

    override fun onDestroy() {
        if (retain)
            koinApp?.close()
    }
}

fun ComponentContext.getOrCreateKoinScope(modules: List<Module>): Scope {
    instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }.also {
        return it.getOrCreateKoinScope(modules)
    }
}
fun KoinApplication.decomposeComponentContext(
    componentContext: ComponentContext
): KoinApplication {
    koin.loadModules(listOf(module {
        single { componentContext } bind ComponentContext::class
    }))
    return this
}

fun initKoin(
    modules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(modules)
}


val Scope.injectComponentContext: ComponentContext by KoinJavaComponent.inject(ComponentContext::class.java)