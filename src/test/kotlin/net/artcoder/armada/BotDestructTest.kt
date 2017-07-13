package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.core.Point
import org.junit.Before
import org.junit.Test

class BotDestructTest {

    private var eventBus = EventBusSpy()
    private var bot = BotMock(eventBus)

    @Before
    fun setUp() {
        eventBus = EventBusSpy()
        bot = BotMock(eventBus)
        eventBus.register(bot)
    }

    /*
    when attack hits and next attack misses
    next attack should be down
     */
    @Test
    fun testAttackHitsThenMissesOnce() {
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportHit(point)
        bot.reportMiss(bot.nextPoint())

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.down())
    }

    /*
    when attack hits and next attack misses and next attack misses
    next attack should be left
     */
    @Test
    fun testAttackHitsThenMissesTwice() {
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportHit(point)
        bot.reportMiss(bot.nextPoint())
        bot.reportMiss(bot.nextPoint())

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.left())
    }

    /*
    when attack hits and next attack misses and next attack misses
    next attack should be up
     */
    @Test
    fun testAttackHitsThenMissesThrice() {
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportHit(point)
        bot.reportMiss(bot.nextPoint())
        bot.reportMiss(bot.nextPoint())
        bot.reportMiss(bot.nextPoint())

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.up())
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
    when attack hits and right point is attacked already
    next attack should be down
     */
    @Test
    fun testAttackHitsDown() {
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportMiss(point.right())
        bot.reportMiss(point.right())
        bot.reportHit(point)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(point.down())
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

    /*
    when attack hits and next attack right hits
    next attack should be right to second attack
     */
    @Test
    fun testContinueRightAttack() {
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportHit(attack2)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack2.right())
    }

    /*
    when attack hits and next attack right hits and next attack right hits
    next attack should be right to third attack
     */
    @Test
    fun testContinueRightAttack2() {
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportHit(attack2)
        val attack3 = bot.nextPoint()
        bot.reportHit(attack3)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(attack3).isEqualTo(attack2.right())
        assertThat(nextAttack).isEqualTo(attack3.right())
    }

    /*
    when attack hits and next attack right hits and next attack on right misses
    next attack should be left to the first attack
     */
    @Test
    fun testGoLeftAfterMiss() {
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportHit(attack2)
        val attack3 = bot.nextPoint()
        bot.reportMiss(attack3)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(attack2).isEqualTo(attack1.right())
        assertThat(attack3).isEqualTo(attack2.right())
        assertThat(nextAttack).isEqualTo(attack1.left())
    }

    /*
    when attack hits and next attack right hits and next cell is already attacked
    next attack should be left to the first attack
     */
    @Test
    fun testGoLeftIfNextCellIsAttacked() {
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportMiss(attack2.right()) //make the right cell hit already
        bot.reportHit(attack2)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack1.left())
    }

    /*
    when attack hits and next attack right hits and next cell is out of board
    next attack should be left to the first attack
     */
    @Test
    fun testGoLeftIfNextCellIsOutOfBoard() {
        bot.putRandomPoint(Point(8, 9))

        val attack1 = bot.nextPoint()
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportHit(attack2)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack1.left())
    }

    /*
    when attack hits and next attack down hits
    next attack should be down to second attack
     */
    @Test
    fun testContinueDownAttack() {
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportMiss(attack1.right())
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportHit(attack2)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack2.down())
    }

    /*
    when attack hits and next attack down hits and next attack down misses
    next attack should be up to the first attack
     */
    @Test
    fun testGoUpAfterMiss() {
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportMiss(attack1.right())
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportHit(attack2)
        val attack3 = bot.nextPoint()
        bot.reportMiss(attack3)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack1.up())
    }

    /*
    when attack hits and next attack down hits and next cell is already attacked
    next attack should be up to the first attack
     */
    @Test
    fun testGoUpIfNextCellIsAttacked() {
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportMiss(attack1.right())
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportMiss(attack2.down()) //make the right cell hit already
        bot.reportHit(attack2)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack1.up())
    }

    /*
    when attack hits and next attack down hits and next cell is out of board
    next attack should be left to the first attack
     */
    @Test
    fun testGoUpIfNextCellIsOutOfBoard() {
        bot.putRandomPoint(Point(8, 8))

        val attack1 = bot.nextPoint()
        bot.reportMiss(attack1.right())
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportHit(attack2)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack1.up())
    }

    /*
    when attack hits and next attack left hits
    next attack should be left to second attack
     */
    @Test
    fun testContinueLeftAttack() {
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportMiss(attack1.right())
        bot.reportMiss(attack1.down())
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint()
        bot.reportHit(attack2)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack2.left())
    }

    /*
    when there are three ships together and destruction goes in wrong direction
    bot detects right direction

    ship1: [(0,0),(0,1)], ship2: [(1,0),(1,1)], ship3: [(2,0),(2,1),(2,2)]
     */
    @Test
    fun testQueueOfAttacks() {
        bot.putRandomPoint(Point(1, 0))

        val attack1 = bot.nextPoint()
        bot.reportMiss(Point(3, 0))
        bot.reportHit(attack1)
        val attack2 = bot.nextPoint() //(2,0)
        bot.reportHit(attack2)
        val attack3 = bot.nextPoint() //(0,0) cause (3,0) is attacked
        bot.reportHit(attack3)

        val attack4 = bot.nextPoint() //should attack first not destroyed (1,1)
        bot.reportSunk(attack4, listOf(Point(1, 0), Point(1, 1)))
        val attack5 = bot.nextPoint() //should attack next not destroyed (2,1)
        bot.reportHit(attack5)
        val attack6 = bot.nextPoint() //should attack next down (2,2)
        bot.reportSunk(attack6, listOf(Point(2, 0), Point(2, 1), Point(2, 2)))
        val attack7 = bot.nextPoint() //should attack not destroyed (0,1)
        bot.reportSunk(attack7, listOf(Point(0, 0), Point(0, 1)))

        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(attack4).isEqualTo(Point(1, 1))
        assertThat(attack5).isEqualTo(Point(2, 1))
        assertThat(attack6).isEqualTo(Point(2, 2))
        assertThat(attack7).isEqualTo(Point(0, 1))
    }
}

