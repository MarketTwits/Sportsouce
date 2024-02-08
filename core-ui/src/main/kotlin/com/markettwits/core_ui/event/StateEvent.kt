package com.markettwits.core_ui.event

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/** This [StateEvent] can only have two primitive states. */
@Serializable
@Immutable
sealed interface StateEvent {
    /** The event is currently in its triggered state */
    @Serializable
    @Immutable
    object Triggered : StateEvent {
        override fun toString(): String = "triggered"
    }

    /** The event is currently in its consumed state */
    @Serializable
    @Immutable
    object Consumed : StateEvent {
        override fun toString(): String = "consumed"
    }
}

/**
 *  Shorter and more readable version of [StateEvent.Triggered]
 */
val triggered = StateEvent.Triggered

/**
 *  Shorter and more readable version of [StateEvent.Consumed]
 */
val consumed = StateEvent.Consumed

fun  StateEvent.isTriggered() : Boolean{
    return this is StateEvent.Triggered
}
