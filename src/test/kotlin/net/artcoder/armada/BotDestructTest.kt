package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.core.Point
import net.artcoder.armada.match.AttackResult.*
import org.junit.Test

class BotDestructTest {

    private fun bot() = BotMock()


    /*
    when attack hits and next attack misses
    next attack should be down
     */
    @Test
    fun testAttackHitsThenMissesOnce() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)
        bot.reportAttack(bot.nextPoint(), MISS)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)
        bot.reportAttack(bot.nextPoint(), MISS)
        bot.reportAttack(bot.nextPoint(), MISS)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)
        bot.reportAttack(bot.nextPoint(), MISS)
        bot.reportAttack(bot.nextPoint(), MISS)
        bot.reportAttack(bot.nextPoint(), MISS)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)

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

    /*
    when attack hits and next attack right hits
    next attack should be right to second attack
     */
    @Test
    fun testContinueRightAttack() {
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)
        val attack3 = bot.nextPoint()
        bot.reportAttack(attack3, HIT)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)
        val attack3 = bot.nextPoint()
        bot.reportAttack(attack3, MISS)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2.right(), MISS) //make the right cell hit already
        bot.reportAttack(attack2, HIT)

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
        val bot = bot()
        bot.putRandomPoint(Point(8, 9))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)
        val attack3 = bot.nextPoint()
        bot.reportAttack(attack3, MISS)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2.down(), MISS) //make the right cell hit already
        bot.reportAttack(attack2, HIT)

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
        val bot = bot()
        bot.putRandomPoint(Point(8, 8))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

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
        val bot = bot()
        bot.putRandomPoint(Point(3, 2))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1.down(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

        val nextAttack = bot.nextPoint()
        assertThat(bot.randomCount).isEqualTo(1)
        assertThat(nextAttack).isEqualTo(attack2.left())
    }
}

