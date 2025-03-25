package com.markettwits.sportsauce.app.browser

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import web.dom.DocumentVisibilityState
import web.events.EventType

internal fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (web.dom.document.visibilityState == DocumentVisibilityState.visible) {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    web.dom.document.addEventListener(
        type = EventType("visibilitychange"),
        callback = { onVisibilityChanged() })
}