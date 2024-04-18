package com.markettwits.start.register.presentation.common

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/** This [StateEvent] can only have two primitive states. */
@Serializable
sealed interface StateEventTest {
    /** The event is currently in its triggered state */
    @Serializable
    @Immutable
    object Triggered : StateEventTest {
        override fun toString(): String = "triggered"
    }

    /** The event is currently in its consumed state */
    @Serializable
    object Consumed : StateEventTest {
        override fun toString(): String = "consumed"
    }
}

/** Shorter and more readable version of [StateEvent.Triggered] */
val triggered = StateEventTest.Triggered

/** Shorter and more readable version of [StateEvent.Consumed] */
val consumed = StateEventTest.Consumed

fun StateEventTest.isTriggered(): Boolean {
    return this is StateEventTest.Triggered
}
