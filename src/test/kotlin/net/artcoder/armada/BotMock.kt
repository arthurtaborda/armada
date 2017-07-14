package net.artcoder.armada

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import net.artcoder.armada.ai.Bot
import net.artcoder.armada.ai.PointGenerator
import net.artcoder.armada.ai.SmartBot
import net.artcoder.armada.core.Point
import net.artcoder.armada.match.OpponentHitEvent
import net.artcoder.armada.match.OpponentMissEvent
import net.artcoder.armada.match.OpponentSunkEvent
import java.util.*

class BotMock(private val eventBus: EventBus) : Bot {

    var randomCount = 0
        private set

    private val randomPoint = LinkedList<Point>()

    private val realBot = SmartBot(object : PointGenerator {

        override fun randomPoint(): Point {
            randomCount++
            return randomPoint.pop()
        }
    })

    @Subscribe fun handle(event: OpponentHitEvent) {
        realBot.handle(event)
    }

    @Subscribe fun handle(event: OpponentMissEvent) {
        realBot.handle(event)
    }

    @Subscribe fun handle(event: OpponentSunkEvent) {
        realBot.handle(event)
    }

    override fun nextPoint(): Point {
        return realBot.nextPoint()
    }

    fun putRandomPoint(point: Point) {
        randomPoint.add(point)
    }

    fun reportMiss(point: Point) {
        eventBus.post(OpponentMissEvent(point))
    }

    fun reportHit(point: Point) {
        eventBus.post(OpponentHitEvent(point))
    }

    fun reportSunk(point: Point, points: List<Point>) {
        eventBus.post(OpponentSunkEvent(point, points))
    }
}