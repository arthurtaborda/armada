package net.artcoder.armada

import com.google.common.truth.Truth
import org.testng.annotations.Test

class MatchTest {

    /*
    player one can attack only points not attacked
     */
    @Test
    fun testCanAttack() {
        val playerOne = PlayerObjectMother.playerOne()
        val playerTwo = PlayerObjectMother.playerTwo()

        val game = BattleshipGame(playerOne, playerTwo)
        game.attack(Point(4, 5))
        game.attack(Point(7, 4))
        game.attack(Point(7, 9))
        game.attack(Point(0, 0))

        Truth.assertThat(game.canAttack(Point(4, 5))).isFalse()
        Truth.assertThat(game.canAttack(Point(7, 4))).isTrue()
        Truth.assertThat(game.canAttack(Point(7, 9))).isFalse()
        Truth.assertThat(game.canAttack(Point(0, 0))).isTrue()
    }
}