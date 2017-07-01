package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.AttackResult.*
import org.testng.annotations.Test

class BotTargetTest : BotTestCase() {

    /*
    when attack hits
    next attack should be right
     */
    @Test
    fun testAttackHitsRight() {
        val pointGenerator = pointGenerator()
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        pointGenerator.verifyCount(1)
        assertThat(nextAttack).isEqualTo(point.right())
    }

    /*
    when attack hits on right edge
    next attack should be down
     */
    @Test
    fun testAttackHitsOnRightEdge() {
        val pointGenerator = pointGenerator(listOf(Point (9, 3)))
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        pointGenerator.verifyCount(1)
        assertThat(nextAttack).isEqualTo(point.down())
    }

    /*
    when attack hits and right point is attacked already
    next attack should be down
     */
    @Test
    fun testAttackHitsDown() {
        val pointGenerator = pointGenerator()
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        pointGenerator.verifyCount(1)
        assertThat(nextAttack).isEqualTo(point.down())
    }

    /*
    when attack hits on left edge
    next attack should be left
     */
    @Test
    fun testAttackHitsOnBottomEdge() {
        val pointGenerator = pointGenerator(listOf(Point(3, 9)))
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        pointGenerator.verifyCount(1)
        assertThat(nextAttack).isEqualTo(point.left())
    }

    /*
    when attack hits and right and down points are attacked already
    next attack should be left
     */
    @Test
    fun testAttackHitsLeft() {
        val pointGenerator = pointGenerator()
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point.down(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        pointGenerator.verifyCount(1)
        assertThat(nextAttack).isEqualTo(point.left())
    }

    /*
    when attack hits on left edge
    next attack should be up
     */
    @Test
    fun testAttackHitsOnLeftEdge() {
        val pointGenerator = pointGenerator(listOf(Point(0, 3)))
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point.down(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        pointGenerator.verifyCount(1)
        assertThat(nextAttack).isEqualTo(point.up())
    }

    /*
    when attack hits and right, down and left points are attacked already
    next attack should be up
     */
    @Test
    fun testAttackHitsUp() {
        val pointGenerator = pointGenerator()
        val bot = bot(pointGenerator)

        val point = bot.nextPoint()
        bot.reportAttack(point.left(), MISS)
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point.down(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        pointGenerator.verifyCount(1)
        assertThat(nextAttack).isEqualTo(point.up())
    }
}

