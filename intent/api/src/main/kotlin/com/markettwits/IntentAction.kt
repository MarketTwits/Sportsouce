package com.markettwits

/**
 * The IntentAction interface defines a set of actions for common system-level tasks
 * such as opening a webpage, dialing a phone number, copying text to the clipboard,
 * and sharing plain text. These methods allow for platform-agnostic implementations.
 */
interface IntentAction {

    /**
     * Opens the specified webpage in the default web browser.
     *
     * @param url the URL of the webpage to open. The URL should be a valid string
     *            and may require formatting for proper interpretation.
     */
    fun openWebPage(url: String)

    /**
     * Initiates an action to dial a phone number.
     *
     * @param phone the phone number to dial. It should contain numeric characters,
     *              and non-numeric characters may need to be filtered out for compatibility.
     */
    fun openPhone(phone: String)

    /**
     * Copies the provided text to the system clipboard.
     *
     * @param text the text to copy to the clipboard. The clipboard content will be
     *             replaced with this text.
     */
    fun copyToSystemBuffer(text: String)

    /**
     * Shares the given text as plain text through available sharing options.
     *
     * @param text the text content to share. The mechanism of sharing depends
     *             on the platform's available options and user preferences.
     */
    fun sharePlainText(text: String)
}