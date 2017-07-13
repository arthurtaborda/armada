package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.core.Point
import org.junit.Before
import org.junit.Test

class BotTargetTest {

    private var eventBus = EventBusSpy()
    private var bot = BotMock(eventBus)

    @Before
    fun setUp() {
        eventBus = EventBusSpy()
        bot = BotMock(eventBus)
        eventBus.register(bot)
    }

    /*
    when attack hits
    next attack should be right
     */
    @Test
    fun testAttackHitsRight() {
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportHit(point)

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
        bot.putRandomPoint(Point(9, 3))

        val point = bot.nextPoint()
        bot.reportHit(point)

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
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportMiss(point.right())
        bot.reportHit(point)

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
        bot.putRandomPoint(Point(3, 9))

        val point = bot.nextPoint()
        bot.reportMiss(point.right())
        bot.reportHit(point)

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
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportMiss(point.right())
        bot.reportMiss(point.down())
        bot.reportHit(point)

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
        bot.putRandomPoint(Point(0, 3))

        val point = bot.nextPoint()
        bot.reportMiss(point.right())
        bot.reportMiss(point.down())
        bot.reportHit(point)

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
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportMiss(point.left())
        bot.reportMiss(point.right())
        bot.reportMiss(point.down())
        bot.reportHit(point)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.up())
    }
}

