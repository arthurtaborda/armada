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
    when attack hits and next attack right hits
    next attack should be right to second attack
     */
    @Test
    fun testContinueRightAttack() {
        val bot = bot()

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack2.right())
    }

    /*
    when attack hits and next attack right hits and next attack on right misses
    next attack should be left to the first attack
     */
    @Test
    fun testGoLeftAfterMiss() {
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
    when attack hits and next attack right hits and next cell is already attacked
    next attack should be left to the first attack
     */
    @Test
    fun testGoLeftIfNextCellIsAttacked() {
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
    when attack hits and next attack right hits and next cell is out of board
    next attack should be left to the first attack
     */
    @Test
    fun testGoLeftIfNextCellIsOutOfBoard() {
        val bot = bot(listOf(RandomPoint(8, 9)))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack1.left())
    }

    /*
    when attack hits and next attack down hits
    next attack should be down to second attack
     */
    @Test
    fun testContinueDownAttack() {
        val bot = bot()

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack2.down())
    }

    /*
    when attack hits and next attack down hits and next attack down misses
    next attack should be up to the first attack
     */
    @Test
    fun testGoUpAfterMiss() {
        val bot = bot()

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)
        val attack3 = bot.nextPoint()
        bot.reportAttack(attack3, MISS)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack1.up())
    }

    /*
    when attack hits and next attack down hits and next cell is already attacked
    next attack should be up to the first attack
     */
    @Test
    fun testGoUpIfNextCellIsAttacked() {
        val bot = bot()

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2.down(), MISS) //make the right cell hit already
        bot.reportAttack(attack2, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack1.up())
    }

    /*
    when attack hits and next attack down hits and next cell is out of board
    next attack should be left to the first attack
     */
    @Test
    fun testGoUpIfNextCellIsOutOfBoard() {
        val bot = bot(listOf(RandomPoint(8, 8)))

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack1.up())
    }

    /*
    when attack hits and next attack left hits
    next attack should be left to second attack
     */
    @Test
    fun testContinueLeftAttack() {
        val bot = bot()

        val attack1 = bot.nextPoint()
        bot.reportAttack(attack1.right(), MISS)
        bot.reportAttack(attack1.down(), MISS)
        bot.reportAttack(attack1, HIT)
        val attack2 = bot.nextPoint()
        bot.reportAttack(attack2, HIT)

        val nextAttack = bot.nextPoint()
        assertNotRandomPoint(nextAttack)
        assertThat(nextAttack).isEqualTo(attack2.left())
    }

    private fun bot() = Bot(10, PointGeneratorStub(
            listOf(
                    RandomPoint(3, 4),
                    RandomPoint(1, 2),
                    RandomPoint(5, 6),
                    RandomPoint(7, 8)
            )))

    private fun bot(points: List<RandomPoint>) = Bot(10, PointGeneratorStub(points))

    class PointGeneratorStub(points: List<RandomPoint>) : PointGenerator {

        val points = points.toMutableList()

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

