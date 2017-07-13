package net.artcoder.armada

import net.artcoder.armada.bot.Bot
import net.artcoder.armada.bot.PointGenerator
import net.artcoder.armada.bot.SmartBot
import net.artcoder.armada.core.Point
import net.artcoder.armada.match.AttackResult
import java.util.*

class BotMock : Bot {

    var randomCount = 0
        private set

    private val randomPoint = LinkedList<Point>()

    private val realBot = SmartBot(object : PointGenerator {

        override fun randomPoint(): Point {
            randomCount++
            return randomPoint.pop()
        }
    })

    override fun nextPoint(): Point {
        return realBot.nextPoint()
    }

    override fun reportAttack(attackPoint: Point, attackResult: AttackResult) {
        realBot.reportAttack(attackPoint, attackResult)
    }

    fun putRandomPoint(point: Point) {
        randomPoint.add(point)
    }
}