package dev.pengie.kotaro.events

import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
/**
 * The manager of everything event related.
 */
object EventManager {

    private val listenerMap: MutableMap<KClass<Event>, MutableSet<EventListener<Event>>> = mutableMapOf()

    /**
     * Registers a listener.
     *
     * @param T the type of event the listener receives.
     * @param listener the listener to register.
     */
    inline fun <reified T : Event> registerListener(listener: EventListener<T>) = registerListener(T::class, listener)

    /**
     * Registers a listener, preferably use [registerListener] for cleaner syntax.
     *
     * @param T the type of event the listener receives.
     * @param type the class of the event.
     * @param listener the listener to register.
     */
    fun <T : Event> registerListener(type: KClass<T>, listener: EventListener<T>) {
        if(!listenerMap.containsKey(type as KClass<Event>)) listenerMap[type] = mutableSetOf()
        listenerMap[type]?.add(listener as EventListener<Event>)
    }

    /**
     * Submits an event to be handled by registered listeners.
     *
     * @param T the type of event.
     * @param event the event to submit.
     */
    inline fun <reified T : Event> submitEvent(event: T) = submitEvent(T::class, event)

    /**
     * Submits an event to be handled by registered listeners, preferably use [submitEvent] for cleaner syntax.
     *
     * @param T the type of event.
     * @param type the class of the event.
     * @param event the event to submit.
     */
    fun <T : Event> submitEvent(type: KClass<T>, event: T) = listenerMap[type as KClass<Event>]?.forEach { it.onEvent(event) }
}