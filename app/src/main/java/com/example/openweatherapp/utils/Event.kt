package com.example.openweatherapp.utils

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
class Event<T>(private val content: T?) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun getContent(): T? = content

    fun equals(event: Event<T>?): Boolean {
        return event != null && this.content == event.content
    }
}