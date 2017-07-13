package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.core.Point
import net.artcoder.armada.match.AttackResult.*
import org.junit.Test

class BotTargetTest {

    private fun bot() = BotMock()

    /*
    when attack hits
    next attack should be right
     */
    @Test
    fun testAttackHitsRight() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.right())
    }

    /*
    when attack hits on right edge
    next attack should be down
     */
    @Test
    fun testAttackHitsOnRightEdge() {
        val bot = bot()
        bot.putRandomPoint(Point(9, 3))

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.down())
    }

    /*
    when attack hits and right point is attacked already
    next attack should be down
     */
    @Test
    fun testAttackHitsDown() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.down())
    }

    /*
    when attack hits on left edge
    next attack should be left
     */
    @Test
    fun testAttackHitsOnBottomEdge() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 9))

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.left())
    }

    /*
    when attack hits and right and down points are attacked already
    next attack should be left
     */
    @Test
    fun testAttackHitsLeft() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point.down(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.left())
    }

    /*
    when attack hits on left edge
    next attack should be up
     */
    @Test
    fun testAttackHitsOnLeftEdge() {
        val bot = bot()
        bot.putRandomPoint(Point(0, 3))

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point.down(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.up())
    }

    /*
    when attack hits and right, down and left points are attacked already
    next attack should be up
     */
    @Test
    fun testAttackHitsUp() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point.left(), MISS)
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point.down(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.up())
    }
}

