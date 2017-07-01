package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import org.testng.annotations.Test

class AttackBoardTest {

    /*
    when attack hits
    return hit
     */
    @Test
    fun testHit() {
        val result = attack(board(), 4, 4)

        assertThat(result).isEqualTo(AttackResult.HIT)
    }

    /*
    when attack misses
    return mis
     */
    @Test
    fun testMiss() {
        val result = attack(board(), 5, 5)

        assertThat(result).isEqualTo(AttackResult.MISS)
    }

    /*
    when attack sinks
    return sunk
     */
    @Test
    fun testSink() {
        val board = board()
        attack(board, 4, 4)
        val result = attack(board, 5, 4)

        assertThat(result).isEqualTo(AttackResult.SUNK)
    }

    /*
    when already attacked
    throw PointAlreadyAttackedException
     */
    @Test(expectedExceptions = arrayOf(PointAlreadyAttackedException::class))
    fun testAlreadyAttacked() {
        val board = board()
        attack(board, 4, 4)
        attack(board, 4, 4)
    }

    private fun attack(board: Board, x: Int, y: Int): AttackResult {
        return board.attack(Point(x, y))
    }

    private fun board() = BoardBuilder().withShip(2, Horizontal(4, 4))
            .build()
}