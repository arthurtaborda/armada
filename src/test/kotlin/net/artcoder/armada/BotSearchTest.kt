package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.core.Point
import net.artcoder.armada.match.AttackResult.*
import org.junit.Test

class BotSearchTest {

    private fun bot() = BotMock()

    /*
    when attack misses
    next attack should be random
     */
    @Test
    fun testAttackMiss() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))
        bot.putRandomPoint(Point(5, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point, MISS)
        bot.nextPoint()

        assertThat(bot.randomCount).isEqualTo(2)
    }

    /*
    when attack hits
    next attack should not be random
     */
    @Test
    fun testAttackHits() {
        val bot = bot()
        bot.putRandomPoint(Point(5, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)
        bot.nextPoint()

        assertThat(bot.randomCount).isEqualTo(1)
    }

    /*
    when attack sinks
    next attack should be random
     */
    @Test
    fun testAttackSinks() {
        val bot = bot()
        bot.putRandomPoint(Point(5, 2))
        bot.putRandomPoint(Point(3, 2))
        bot.putRandomPoint(Point(5, 7))

        val point = bot.nextPoint()
        bot.reportAttack(point, SUNK)
        bot.nextPoint()

        assertThat(bot.randomCount).isEqualTo(2)
    }

    /*
    given random point that was already attacked
    next point should be generated again
     */
    @Test
    fun testRandomAttackCannotDuplicate() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 4))
        bot.putRandomPoint(Point(3, 4))
        bot.putRandomPoint(Point(6, 4))

        val point1 = bot.nextPoint()
        bot.reportAttack(point1, MISS)
        val point2 = bot.nextPoint()
        bot.reportAttack(point2, MISS)

        assertThat(bot.randomCount).isEqualTo(3)
        assertThat(point1).isEqualTo(Point(3, 4))
        assertThat(point2).isEqualTo(Point(6, 4))
    }
}

