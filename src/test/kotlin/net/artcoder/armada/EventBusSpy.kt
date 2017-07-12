package net.artcoder.armada

import com.google.common.eventbus.EventBus
import kotlin.reflect.KClass

class EventBusSpy : EventBus() {

    private val eventCount = mutableMapOf<KClass<out Any>, Int>()
    private val eventsDispatched = mutableListOf<Any>()

    override fun post(event: Any?) {
        val count = eventCount.getOrDefault(event!!::class, 0)
        eventCount.put(event::class, count + 1)
        eventsDispatched.add(event)
        super.post(event)
    }

    fun postCount(eventClass: KClass<out Any>): Int {
        return eventCount.getOrDefault(eventClass, 0)
    }

    fun contains(event: Any): Boolean {
        return eventsDispatched.contains(event)
    }
}