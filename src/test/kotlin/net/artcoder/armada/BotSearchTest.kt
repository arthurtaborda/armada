package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.core.Point
import org.junit.Before
import org.junit.Test

class BotSearchTest {

    private var eventBus = EventBusSpy()
    private var bot = BotMock(eventBus)

    @Before
    fun setUp() {
        eventBus = EventBusSpy()
        bot = BotMock(eventBus)
        eventBus.register(bot)
    }

    /*
    when attack misses
    next attack should be random
     */
    @Test
    fun testAttackMiss() {
        bot.putRandomPoint(Point(3, 2))
        bot.putRandomPoint(Point(5, 2))

        val point = bot.nextPoint()
        bot.reportMiss(point)
        bot.nextPoint()

        assertThat(bot.randomCount).isEqualTo(2)
    }

    /*
    when attack hits
    next attack should not be random
     */
    @Test
    fun testAttackHits() {
        bot.putRandomPoint(Point(5, 2))

        val point = bot.nextPoint()
        bot.reportHit(point)
        bot.nextPoint()

        assertThat(bot.randomCount).isEqualTo(1)
    }

    /*
    when attack sinks
    next attack should be random
     */
    @Test
    fun testAttackSinks() {
        bot.putRandomPoint(Point(5, 2))
        bot.putRandomPoint(Point(3, 2))
        bot.putRandomPoint(Point(5, 7))

        val point = bot.nextPoint()
        bot.reportSunk(point, listOf(point, point.left()))
        bot.nextPoint()

        assertThat(bot.randomCount).isEqualTo(2)
    }

    /*
    given random point that was already attacked
    next point should be generated again
     */
    @Test
    fun testRandomAttackCannotDuplicate() {
        bot.putRandomPoint(Point(3, 4))
        bot.putRandomPoint(Point(3, 4))
        bot.putRandomPoint(Point(6, 4))

        val point1 = bot.nextPoint()
        bot.reportMiss(point1)
        val point2 = bot.nextPoint()
        bot.reportMiss(point2)

        assertThat(bot.randomCount).isEqualTo(3)
        assertThat(point1).isEqualTo(Point(3, 4))
        assertThat(point2).isEqualTo(Point(6, 4))
    }
}

