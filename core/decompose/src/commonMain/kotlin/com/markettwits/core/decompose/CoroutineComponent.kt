package com.markettwits.core.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

private const val INSTANCE_KEY =
    "com.arkivanov.decompose.lifecycle.ComponentContextCoroutineScope.INSTANCE_KEY"

val ComponentContext.componentScope: CoroutineScope
    get() {
        val scope = instanceKeeper.get(INSTANCE_KEY)
        if (scope is CoroutineScope) return scope

        fun destroy() {
            try {
                scope?.onDestroy()
            } catch (e: Exception) {
                throw RuntimeException(e)
            } finally {
                instanceKeeper.remove(INSTANCE_KEY)
            }
        }

        if (lifecycle.state == Lifecycle.State.DESTROYED) {
            destroy()
        } else {
            lifecycle.doOnDestroy {
                destroy()
            }
        }

        return DestroyableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).also {
            instanceKeeper.put(INSTANCE_KEY, it)
        }
    }

class DestroyableCoroutineScope(
    context: CoroutineContext
) : CoroutineScope, InstanceKeeper.Instance {

    override val coroutineContext: CoroutineContext = context

    override fun onDestroy() {
        coroutineContext.cancel()
    }

}
