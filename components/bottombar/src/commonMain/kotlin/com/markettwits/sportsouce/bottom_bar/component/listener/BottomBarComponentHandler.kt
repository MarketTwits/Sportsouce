package com.markettwits.sportsouce.bottom_bar.component.listener


import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.markettwits.sportsouce.bottom_bar.component.storage.BottomBarStorageImpl

/**
 * Abstract class for handling BottomBar visibility when navigating back.
 *
 * This class ensures that the BottomBar is shown before navigating back.
 * It provides a back press handler with the highest priority to control
 * navigation behavior.
 *
 * @property bottomBarVisibilityListener A listener responsible for managing BottomBar visibility.
 */
abstract class BottomBarComponentHandler(
    private val bottomBarVisibilityListener: BottomBarVisibilityListener = BottomBarStorageImpl,
) {
    /**
     * Abstract method that subclasses must implement to define
     * the specific back navigation behavior.
     */

    fun ComponentContext.subscribeOnBottomBar(
        bottomBarVisibilityStrategy: BottomBarVisibilityStrategy = BottomBarVisibilityStrategy.AlwaysVisible
    ) {
        lifecycle.doOnStart {
            bottomBarVisibilityStrategy.onStart(bottomBarVisibilityListener)
        }

        lifecycle.doOnStop {
            bottomBarVisibilityStrategy.onStop(bottomBarVisibilityListener)
        }
    }
}

