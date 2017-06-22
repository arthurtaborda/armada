package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.AttackResult.*
import org.testng.annotations.Test

class BotTest {

    /*
    when attack misses
    next attack should be random
     */
    @Test
    fun testAttackMiss() {
        val bot = bot()

        val point = bot.nextPoint()
        bot.reportAttack(point, MISS)

        assertRandomPoint(bot.nextPoint())
    }

    /*
    when attack sinks
    next attack should be random
     */
    @Test
    fun testAttackSinks() {
        val bot = bot()

        val point = bot.nextPoint()
        bot.reportAttack(point, SUNK)

        assertRandomPoint(bot.nextPoint())
    }

    /*
    when attack hits
    next attack should be to the right
     */
    @Test
    fun testAttackHitsRight() {
        val bot = bot()

        val point = bot.nextPoint()
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(point.right())
    }

    /*
    when attack hits and right point is attacked already
    next attack should be down
     */
    @Test
    fun testAttackHitsDown() {
        val bot = bot()

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(point.down())
    }

    /*
    when attack hits and right and down points are attacked already
    next attack should be left
     */
    @Test
    fun testAttackHitsLeft() {
        val bot = bot()

        val point = bot.nextPoint()
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point.down(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(point.left())
    }

    /*
    when attack hits and right, down and left points are attacked already
    next attack should be up
     */
    @Test
    fun testAttackHitsUp() {
        val bot = bot()

        val point = bot.nextPoint()
        bot.reportAttack(point.left(), MISS)
        bot.reportAttack(point.right(), MISS)
        bot.reportAttack(point.down(), MISS)
        bot.reportAttack(point, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(point.up())
    }

    /*
    when attack hits and next attack on right hits
    next attack should be right to second attack until sinks
     */
    @Test
    fun testContinueRightAttackUntilSinks() {
        val bot = bot()

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)
        val attack3 = bot.nextPoint()
        bot.reportAttack(attack3, SUNK)

        val nextAttack = bot.nextPoint()
        assertRandomPoint(nextAttack)
        assertThat(nextAttack).isNotEqualTo(attack3.right())
    }

    /*
    when attack hits and next attack on right hits and next attack on right misses
    next attack should be left to the first attack until sinks
     */
    @Test
    fun testGoToLeftAfterMisses() {
        val bot = bot()

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)
        val attack3 = bot.nextPoint()
        bot.reportAttack(attack3, MISS)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack1.left())
    }

    /*
    when attack hits and next attack on right hits and next attack on right misses
    next attack should be left to the first attack until sinks
     */
    @Test
    fun testGoToLeftIfNextCellIsHit() {
        val bot = bot()

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2.right(), MISS) //make the right cell hit already
        bot.reportAttack(attack2, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack1.left())
    }

    /*
    when attack hits and next attack on down hits
    next attack should be down to second attack until sinks
     */

    /*
    when attack hits and next attack on down hits and next attack on down misses
    next attack should be up to the first attack until sinks
     */

    /*
    when attack hits and next attack on left hits
    next attack should be left to second attack until sinks
     */

    /*
    when attack hits and next attack on left hits and next attack on left misses
    next attack should be right to the first attack until sinks
     */

    /*
    when attack hits and next attack on up hits
    next attack should be up to second attack until sinks
     */

    /*
    when attack hits and next attack on up hits and next attack on up misses
    next attack should be down to the first attack until sinks
     */

    private fun bot() = Bot(10, PointGeneratorStub(
            mutableListOf(
                    RandomPoint(1, 2),
                    RandomPoint(3, 4),
                    RandomPoint(5, 6),
                    RandomPoint(7, 8)
            )))

    class PointGeneratorStub(val points: MutableList<RandomPoint>) : PointGenerator {

        override fun randomPoint(): Point {
            return points.removeAt(0)
        }
    }

    class RandomPoint(override val x: Int, override val y: Int) : Point {

        override fun up() = Cell(x, y - 1)
        override fun right() = Cell(x + 1, y)
        override fun left() = Cell(x - 1, y)
        override fun down() = Cell(x, y + 1)
    }

    private fun assertRandomPoint(point: Point) {
        assertThat(point).isInstanceOf(RandomPoint::class.java)
    }

    private fun assertNotRandomPoint(point: Point) {
        assertThat(point).isNotInstanceOf(RandomPoint::class.java)
    }
}

