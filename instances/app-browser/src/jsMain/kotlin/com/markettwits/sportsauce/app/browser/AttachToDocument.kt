package com.markettwits.sportsauce.app.browser

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import kotlinx.browser.document
import web.dom.DocumentVisibilityState

internal fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (web.dom.document.visibilityState == DocumentVisibilityState.visible) {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(
        type = "visibilitychange",
        callback = {
            onVisibilityChanged()
        }
    )
}