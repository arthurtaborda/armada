package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import org.testng.annotations.Test

class GameStartTest {

    /*
    when game starts
    player 1 is attacking and player 2 is waiting
     */
    @Test fun testGameStart() {
        val playerOne = Player()
        val playerTwo = Player()

        BattleshipGame(playerOne, playerTwo)

        assertThat(playerOne.state).isEqualTo(Player.State.ATTACKING)
        assertThat(playerTwo.state).isEqualTo(Player.State.WAITING)
    }
}