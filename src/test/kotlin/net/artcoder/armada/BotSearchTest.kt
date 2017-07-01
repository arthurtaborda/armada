package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.AttackResult.*
import org.testng.annotations.Test

class BotSearchTest : BotTestCase() {

    /*
    when attack misses
    next attack should be random
     */
    @Test
    fun testAttackMiss() {
        val pointGenerator = pointGenerator()
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point, MISS)
        bot.nextPoint()

        pointGenerator.verifyCount(2)
    }

    /*
    when attack hits
    next attack should not be random
     */
    @Test
    fun testAttackHits() {
        val pointGenerator = pointGenerator()
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)
        bot.nextPoint()

        pointGenerator.verifyCount(1)
    }

    /*
    when attack sinks
    next attack should be random
     */
    @Test
    fun testAttackSinks() {
        val pointGenerator = pointGenerator()
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point, SUNK)
        bot.nextPoint()

        pointGenerator.verifyCount(2)
    }

    /*
    when attack sinks
    next attack should be random
     */
    @Test
    fun testRandomAttackCannotDuplicate() {
        val pointGenerator = pointGenerator(listOf(Point(3, 4), Point(3, 4), Point(6, 4)))
        val bot = bot(pointGenerator)

        val point1 = bot.nextPoint()
        bot.reportAttack(point1, MISS)
        val point2 = bot.nextPoint()
        bot.reportAttack(point2, MISS)

        pointGenerator.verifyCount(3)
        assertThat(point1).isEqualTo(Point(3, 4))
        assertThat(point2).isEqualTo(Point(6, 4))
    }
}

