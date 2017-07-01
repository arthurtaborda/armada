package net.artcoder.armada

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

        pointGenerator.assertCount(2)
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

        pointGenerator.assertCount(2)
    }
}

