package com.markettwits.bottom_bar.component.component


import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.bottom_bar.component.storage.BottomBarStorageImpl

/**
 * Abstract class for handling BottomBar visibility when navigating back.
 *
 * This class ensures that the BottomBar is shown before navigating back.
 * It provides a back press handler with the highest priority to control
 * navigation behavior.
 *
 * @property bottomBarVisibilityListener A listener responsible for managing BottomBar visibility.
 */
abstract class BottomBarHandler(
    private val bottomBarVisibilityListener: BottomBarVisibilityListener = BottomBarStorageImpl,
    private val bottomBarVisibilityStrategy: BottomBarVisibilityStrategy = BottomBarVisibilityStrategy.AlwaysVisible,
) {
    /**
     * Back press handler that:
     * - Shows the BottomBar before navigating back.
     * - Calls `goBackInner()`, which must be implemented by subclasses.
     *
     * The priority is set to `Int.MAX_VALUE` to ensure that this handler
     * has the highest precedence in the back press handling system.
     */
    private val bottomBarBackHandler: BackCallback = BackCallback(
        onBack = {
            bottomBarVisibilityStrategy.onStop(bottomBarVisibilityListener)
            popInner()
        }, priority = Int.MAX_VALUE
    )

    /**
     * Abstract method that subclasses must implement to define
     * the specific back navigation behavior.
     */

    abstract fun popInner()

    fun ComponentContext.subscribeOnBottomBar() {
        lifecycle.doOnStart {
            bottomBarVisibilityStrategy.onStart(bottomBarVisibilityListener)
        }

        lifecycle.doOnStop {
            bottomBarVisibilityStrategy.onStop(bottomBarVisibilityListener)
        }

        backHandler.register(bottomBarBackHandler)
    }
}

enum class BottomBarVisibilityStrategy {
    AlwaysVisible {
        override fun onStart(listener: BottomBarVisibilityListener) {
            listener.show()
        }

        override fun onStop(listener: BottomBarVisibilityListener) {
            listener.show()
        }
    },

    AlwaysInvisible {
        override fun onStart(listener: BottomBarVisibilityListener) {
            listener.hide()
        }

        override fun onStop(listener: BottomBarVisibilityListener) {
            listener.show()
        }
    };

    internal abstract fun onStart(
        listener: BottomBarVisibilityListener,
    )

    internal abstract fun onStop(
        listener: BottomBarVisibilityListener,
    )
}