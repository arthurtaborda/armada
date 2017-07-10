package net.artcoder.armada

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.PlayerObjectMother.playerOne
import net.artcoder.armada.PlayerObjectMother.playerTwo
import org.testng.annotations.Test

class PlayerAttackTest {

    /*
    when playerBoard 1 hits
    playerBoard 1 is waiting and playerBoard 2 is attacking
     */
    @Test
    fun testPlayerOneHits() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        val attackResult = game.attack(Point(2, 9)) // player one attack

        assertThat(attackResult).isEqualTo(AttackResult.HIT)
        assertThat(playerOne.state).isEqualTo(Player.State.WAITING)
        assertThat(playerTwo.state).isEqualTo(Player.State.ATTACKING)
    }

    /*
    when playerBoard 1 misses
    playerBoard 1 is waiting and playerBoard 2 is attacking
     */
    @Test
    fun testPlayerOneMisses() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        val attackResult = game.attack(Point(3, 3))

        assertThat(attackResult).isEqualTo(AttackResult.MISS)
        assertThat(playerOne.state).isEqualTo(Player.State.WAITING)
        assertThat(playerTwo.state).isEqualTo(Player.State.ATTACKING)
    }

    /*
    when playerBoard 1 sinks and not all ships from playerBoard 2 sunk
    playerBoard 1 is waiting and playerBoard 2 is attacking
     */
    @Test
    fun testPlayerOneSinksOneButNotAll() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        game.attack(Point(2, 9)) //player one attack
        game.attack(Point(4, 5)) //player two attack
        val attackResult = game.attack(Point(2, 8)) //player one attack

        assertThat(attackResult).isEqualTo(AttackResult.SUNK)
        assertThat(playerOne.state).isEqualTo(Player.State.WAITING)
        assertThat(playerTwo.state).isEqualTo(Player.State.ATTACKING)
    }

    /*
    when playerBoard 1 sinks and all ships from playerBoard 2 sunk
    playerBoard 1 wins
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
    playerBoard 1 is attacking and playerBoard 2 is waiting
     */

    /*
    when playerBoard 2 hits
    playerBoard 2 is waiting and playerBoard 1 is attacking
     */

    /*
    when playerBoard 2 misses
    playerBoard 2 is waiting and playerBoard 1 is attacking
     */

    /*
    when playerBoard 2 sinks and not all ships from playerBoard 1 sunk
    playerBoard 2 is waiting and playerBoard 1 is attacking
     */

    /*
    when playerBoard 2 sinks and all ships from playerBoard 1 sunk
    playerBoard 2 wins
     */

    private fun destroyPlayerTwo(game: BattleshipGame): AttackResult {
        val playerTwoPoints = PlayerObjectMother.playerTwoPoints()
        for (i in 0..9) {
            for (j in 1..9) {
                val point = playerTwoPoints.removeAt(0)

                val playerOneAttackResult = game.attack(Point(point.x, point.y))

                if (playerTwoPoints.isEmpty()) {
                    return playerOneAttackResult
                }

                game.attack(Point(i, j))
            }
        }

        return AttackResult.MISS
    }

    /*
    player one can attack only points not attacked
     */
    @Test
    fun testCanAttack() {
        val playerOne = PlayerObjectMother.playerOne()
        val playerTwo = PlayerObjectMother.playerTwo()

        playerOne.attack(playerTwo, Point(4, 5))
        playerTwo.attack(playerOne, Point(7, 4))
        playerOne.attack(playerTwo, Point(7, 9))

        Truth.assertThat(playerOne.canAttack(playerTwo, Point(4, 5))).isFalse()
        Truth.assertThat(playerOne.canAttack(playerTwo, Point(7, 4))).isTrue()
        Truth.assertThat(playerOne.canAttack(playerTwo, Point(7, 9))).isFalse()
    }
}

