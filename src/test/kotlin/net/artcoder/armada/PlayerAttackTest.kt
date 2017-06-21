package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.PlayerObjectMother.playerOne
import net.artcoder.armada.PlayerObjectMother.playerTwo
import org.testng.annotations.Test

class PlayerAttackTest {

    /*
    when player 1 hits
    player 1 is waiting and player 2 is attacking
     */
    @Test
    fun testPlayerOneHits() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        val attackResult = game.attack(2, 9) // player one attack

        assertThat(attackResult).isEqualTo(AttackResult.HIT)
        assertThat(playerOne.state).isEqualTo(Player.State.WAITING)
        assertThat(playerTwo.state).isEqualTo(Player.State.ATTACKING)
    }

    /*
    when player 1 misses
    player 1 is waiting and player 2 is attacking
     */
    @Test
    fun testPlayerOneMisses() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        val attackResult = game.attack(3, 3)

        assertThat(attackResult).isEqualTo(AttackResult.MISS)
        assertThat(playerOne.state).isEqualTo(Player.State.WAITING)
        assertThat(playerTwo.state).isEqualTo(Player.State.ATTACKING)
    }

    /*
    when player 1 sinks and not all ships from player 2 sunk
    player 1 is waiting and player 2 is attacking
     */
    @Test
    fun testPlayerOneSinksOneButNotAll() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        game.attack(2, 9) //player one attack
        game.attack(4, 5) //player two attack
        val attackResult = game.attack(2, 8) //player one attack

        assertThat(attackResult).isEqualTo(AttackResult.SUNK)
        assertThat(playerOne.state).isEqualTo(Player.State.WAITING)
        assertThat(playerTwo.state).isEqualTo(Player.State.ATTACKING)
    }

    /*
    when player 1 sinks and all ships from player 2 sunk
    player 1 wins
     */
    @Test
    fun testPlayerOneSinksAll() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        val attackResult = destroyPlayerTwo(game)

        assertThat(attackResult).isEqualTo(AttackResult.SUNK)
        assertThat(playerOne.state).isEqualTo(Player.State.WON)
        assertThat(playerTwo.state).isEqualTo(Player.State.LOST)
    }

    /*
    when game starts
    player 1 is attacking and player 2 is waiting
     */

    /*
    when player 2 hits
    player 2 is waiting and player 1 is attacking
     */

    /*
    when player 2 misses
    player 2 is waiting and player 1 is attacking
     */

    /*
    when player 2 sinks and not all ships from player 1 sunk
    player 2 is waiting and player 1 is attacking
     */

    /*
    when player 2 sinks and all ships from player 1 sunk
    player 2 wins
     */

    private fun destroyPlayerTwo(game: BattleshipGame): AttackResult {
        val playerTwoPoints = PlayerObjectMother.playerTwoPoints()
        for (i in 0..9) {
            for (j in 1..9) {
                val point = playerTwoPoints.removeAt(0)

                val playerOneAttackResult = game.attack(point.x, point.y)

                if (playerTwoPoints.isEmpty()) {
                    return playerOneAttackResult
                }

                game.attack(i, j)
            }
        }

        return AttackResult.ALREADY_ATTACKED
    }
}

