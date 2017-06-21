package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.ships.Destroyer
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class AttackBoardTest {

    var board: Board? = null

    @BeforeMethod
    fun setUp() {
        board = BoardBuilder().withShip(Destroyer(), Horizontal(4, 4))
                .build()
    }

    @Test
    fun whenAttackHits_returnHit() {
        val result = attacks(4, 4)

        assertThat(result).isEqualTo(AttackResult.HIT)
    }

    @Test
    fun whenAttackMisses_returnMiss() {
        val result = attacks(5, 5)

        assertThat(result).isEqualTo(AttackResult.MISS)
    }

    @Test
    fun whenAttacksSamePointTwice_returnAlreadyAttacked() {
        attacks(4, 4)
        val result = attacks(4, 4)

        assertThat(result).isEqualTo(AttackResult.ALREADY_ATTACKED)
    }

    @Test
    fun whenAttacksAllDestroyer_returnSunk() {
        attacks(4, 4)
        val result = attacks(5, 4)

        assertThat(result).isEqualTo(AttackResult.SUNK)
    }

    private fun attacks(x: Int, y: Int): AttackResult {
        return board!!.attack(Point(x, y))
    }
}