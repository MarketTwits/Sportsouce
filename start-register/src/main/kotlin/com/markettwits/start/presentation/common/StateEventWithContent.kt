package com.markettwits.start.presentation.common

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * This [StateEventWithContent] can have exactly 2 states like the
 * [StateEvent] but the triggered state holds a value of type [T].
 */
@Serializable
@Immutable
sealed interface StateEventWithContentTest

/**
 * The event in its triggered state holding a value of [T]. See [triggered]
 * to create an instance of this.
 *
 * @param content A value that is needed on the event consumer side.
 */
@Serializable
data class StateEventWithContentTriggered(val content: EventContentTest) :
    StateEventWithContentTest {
    override fun toString(): String = "triggered($content)"
}

/**
 * The event in its consumed state not holding any value. See [consumed] to
 * create an instance of this.
 */
@Serializable
object StateEventWithContentConsumed : StateEventWithContentTest {
    override fun toString(): String = "consumed"
}

/**
 * A shorter and more readable way to create an [StateEventWithContent] in
 * its triggered state holding a value of [T].
 *
 * @param content A value that is needed on the event consumer side.
 */
fun triggered(content: EventContentTest) = StateEventWithContentTriggered(content)

/**
 * A shorter and more readable way to create an [StateEventWithContent] in
 * its consumed state.
 */
fun consumed() = StateEventWithContentConsumed

@Serializable
data class EventContentTest(val success: Boolean, val message: String)