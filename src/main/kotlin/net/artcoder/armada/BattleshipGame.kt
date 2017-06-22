package net.artcoder.armada

import net.artcoder.armada.Player.State.*

class BattleshipGame(private val playerOne: Player,
                     private val playerTwo: Player) {

    init {
        playerOne.state = ATTACKING
        playerTwo.state = WAITING
    }

    private fun attackingPlayer(): Player {
        if (playerOne.state == ATTACKING) {
            return playerOne
        }
        return playerTwo
    }

    private fun waitingPlayer(): Player {
        if (playerOne.state == WAITING) {
            return playerOne
        }
        return playerTwo
    }

    fun attack(x: Int, y: Int): AttackResult {
        val attackResult = attackingPlayer().attack(waitingPlayer(), Cell(x, y))

        when (attackResult) {
            AttackResult.HIT -> flipStatus()
            AttackResult.MISS -> flipStatus()
            AttackResult.SUNK -> {
                if (waitingPlayer().allShipsDestroyed()) {
                    declareWinner()
                } else {
                    flipStatus()
                }
            }
            AttackResult.ALREADY_ATTACKED -> TODO()
        }

        return attackResult
    }

    private fun flipStatus() {
        if (playerOne.state == ATTACKING) {
            playerOne.state = WAITING
            playerTwo.state = ATTACKING
        } else {
            playerOne.state = ATTACKING
            playerTwo.state = WAITING
        }
    }

    private fun declareWinner() {
        if (playerOne.state == ATTACKING) {
            playerOne.state = WON
            playerTwo.state = LOST
        } else {
            playerOne.state = LOST
            playerTwo.state = WON
        }
    }

}
