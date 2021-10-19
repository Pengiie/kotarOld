package dev.pengie.kotaro.events

/**
 * A listener for the specified event.
 *
 * @param T the type of the event to listen for.
 */
interface EventListener<in T : Event> {
    /**
     * Gets called when an event is received.
     *
     * @param event the event received.
     */
    fun onEvent(event: T)

    companion object {
        /**
         * Creates an event listener.
         *
         * @param T the type of event to create a listener for.
         * @param onEvent the code to run when receiving an event.
         */
        inline fun <reified T : Event> create(crossinline onEvent: (T) -> Unit): EventListener<T> = object : EventListener<T> {
            override fun onEvent(event: T) {
                onEvent.invoke(event)
            }
        }
    }
}