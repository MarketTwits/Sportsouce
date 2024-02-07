package com.markettwits.core_ui.event

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * This [StateEventWithContent] can have exactly 2 states like the
 * [StateEvent] but the triggered state holds a value of type [T].
 */
@Serializable
@Immutable
sealed interface StateEventWithContent<out T>

/**
 * The event in its triggered state holding a value of [T]. See [triggered]
 * to create an instance of this.
 *
 * @param content A value that is needed on the event consumer side.
 */
@Serializable
@Immutable
data class StateEventWithContentTriggered<T>(val content: T) : StateEventWithContent<T> {
    override fun toString(): String = "triggered($content)"
}

/**
 * The event in its consumed state not holding any value. See [consumed] to
 * create an instance of this.
 */
@Serializable
@Immutable
object StateEventWithContentConsumed : StateEventWithContent<Nothing> {
    override fun toString(): String = "consumed"
}

/**
 * A shorter and more readable way to create an [StateEventWithContent] in its triggered state holding a value of [T].
 * @param content A value that is needed on the event consumer side.
 */
fun <T> triggered(content: T) = StateEventWithContentTriggered(content)

/**
 * A shorter and more readable way to create an [StateEventWithContent] in its consumed state.
 */
fun consumed() = StateEventWithContentConsumed

fun <T> StateEventWithContent<T>.isTriggered() : Boolean{
    return this is StateEventWithContentTriggered
}

@Serializable
@Immutable
data class EventContent(val success : Boolean, val message : String)
