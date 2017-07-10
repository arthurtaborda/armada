package net.artcoder.armada

import com.google.common.truth.Truth

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.PlayerObjectMother.playerOne
import net.artcoder.armada.PlayerObjectMother.playerOnePoints
import net.artcoder.armada.PlayerObjectMother.playerTwo
import org.testng.annotations.Test

class GameFlowTest {

    /*
    player one can attack only points not attacked
     */
    @Test
    fun testCanAttack() {
        val playerOne = PlayerObjectMother.playerOne()
        val playerTwo = PlayerObjectMother.playerTwo()

        val game = BattleshipGame(playerOne, playerTwo)
        game.attack(Point(7, 0)) //player one attack
        game.attack(Point(8, 0)) //player two attack
        game.attack(Point(9, 0)) //player one attack
        game.attack(Point(9, 1)) //player two attack

        Truth.assertThat(game.canAttack(Point(7, 0))).isFalse()
        Truth.assertThat(game.canAttack(Point(8, 0))).isTrue()
        Truth.assertThat(game.canAttack(Point(9, 0))).isFalse()
        Truth.assertThat(game.canAttack(Point(9, 1))).isTrue()
    }

    /*
    when game starts
    playerBoard 1 is attacking and playerBoard 2 is waiting
     */
    @Test
    fun testGameStart() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()

        BattleshipGame(playerOne, playerTwo)

        assertThat(playerOne.state).isEqualTo(Player.State.ATTACKING)
        assertThat(playerTwo.state).isEqualTo(Player.State.WAITING)
    }

    /*
    when playerBoard 1 hits
    playerBoard 1 is attacking and playerBoard 2 is waiting
     */
    @Test
    fun testPlayerOneHits() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        val attackResult = game.attack(Point(2, 9)) // player one attack

        assertThat(attackResult).isEqualTo(AttackResult.HIT)
        assertThat(playerOne.state).isEqualTo(Player.State.ATTACKING)
        assertThat(playerTwo.state).isEqualTo(Player.State.WAITING)
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
    playerBoard 1 is attacking and playerBoard 2 is waiting
     */
    @Test
    fun testPlayerOneSinksOneButNotAll() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        game.attack(Point(2, 9)) //player one attack
        val attackResult = game.attack(Point(2, 8)) //player one attack

        assertThat(attackResult).isEqualTo(AttackResult.SUNK)
        assertThat(playerOne.state).isEqualTo(Player.State.ATTACKING)
        assertThat(playerTwo.state).isEqualTo(Player.State.WAITING)
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

        destroyPlayer(game, PlayerObjectMother.playerTwoPoints())

        assertThat(playerOne.state).isEqualTo(Player.State.WON)
        assertThat(playerTwo.state).isEqualTo(Player.State.LOST)
    }

    /*
    when playerBoard 2 hits
    playerBoard 2 is attacking and playerBoard 1 is waiting
     */
    @Test
    fun testPlayerTwoHits() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        game.attack(Point(2, 0)) // player one attack
        val attackResult = game.attack(Point(2, 0)) // player two attack

        assertThat(attackResult).isEqualTo(AttackResult.HIT)
        assertThat(playerOne.state).isEqualTo(Player.State.WAITING)
        assertThat(playerTwo.state).isEqualTo(Player.State.ATTACKING)
    }

    /*
    when playerBoard 2 misses
    playerBoard 2 is waiting and playerBoard 1 is attacking
     */
    @Test
    fun testPlayerTwoMisses() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        game.attack(Point(3, 3))
        val attackResult = game.attack(Point(3, 3))

        assertThat(attackResult).isEqualTo(AttackResult.MISS)
        assertThat(playerOne.state).isEqualTo(Player.State.ATTACKING)
        assertThat(playerTwo.state).isEqualTo(Player.State.WAITING)
    }

    /*
    when playerBoard 2 sinks and not all ships from playerBoard 1 sunk
    playerBoard 2 is waiting and playerBoard 1 is attacking
     */
    @Test
    fun testPlayerTwoSinksOneButNotAll() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        game.attack(Point(2, 0)) //player one attack
        game.attack(Point(2, 0)) //player two attack
        val attackResult = game.attack(Point(2, 1)) //player two attack

        assertThat(attackResult).isEqualTo(AttackResult.SUNK)
        assertThat(playerOne.state).isEqualTo(Player.State.WAITING)
        assertThat(playerTwo.state).isEqualTo(Player.State.ATTACKING)
    }

    /*
    when playerBoard 2 sinks and all ships from playerBoard 1 sunk
    playerBoard 2 wins
     */
    @Test
    fun testPlayerTwoSinksAll() {
        val playerOne = playerOne()
        val playerTwo = playerTwo()
        val game = BattleshipGame(playerOne, playerTwo)

        game.attack(Point(2, 0)) //player one attack
        destroyPlayer(game, playerOnePoints())

        assertThat(playerOne.state).isEqualTo(Player.State.LOST)
        assertThat(playerTwo.state).isEqualTo(Player.State.WON)
    }

    private fun destroyPlayer(game: BattleshipGame, playerPoints: MutableList<Point>) {
        val points = playerPoints
        for (i in 0..9) {
            for (j in 1..9) {
                val point = points.removeAt(0)

                game.attack(point)

                if (points.isEmpty()) {
                    return
                }
            }
        }
    }
}

