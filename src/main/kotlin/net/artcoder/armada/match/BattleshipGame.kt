package net.artcoder.armada.match

import com.google.common.eventbus.EventBus
import net.artcoder.armada.core.Point

class BattleshipGame(private val eventBus: EventBus,
                     private val playerBoard: Board,
                     private val opponentBoard: Board) {

    private var isPlayersTurn = true

    fun attack(point: Point) {
        val attackResult = getBoardToAttack().attack(point)
        when (attackResult) {
            AttackResult.MISS -> miss(point)
            AttackResult.HIT  -> hit(point)
            AttackResult.SUNK -> sunk(point)
        }
    }

    private fun sunk(point: Point) {
        if (isPlayersTurn) {
            eventBus.post(PlayerSunkEvent(point, opponentBoard.pointsOfShipIn(point)))
            if(opponentBoard.allShipsDestroyed()) {
                eventBus.post(PlayerWonEvent(point))
            }
        } else {
            eventBus.post(OpponentSunkEvent(point, playerBoard.pointsOfShipIn(point)))
            if (playerBoard.allShipsDestroyed()) {
                eventBus.post(OpponentWonEvent(point))
            }
        }
    }

    private fun hit(point: Point) {
        if (isPlayersTurn) {
            eventBus.post(PlayerHitEvent(point))
        } else {
            eventBus.post(OpponentHitEvent(point))
        }
    }

    private fun miss(point: Point) {
        if (isPlayersTurn) {
            flipTurn()
            eventBus.post(PlayerMissEvent(point))
        } else {
            flipTurn()
            eventBus.post(OpponentMissEvent(point))
        }
    }

    private fun getBoardToAttack(): Board {
        if (isPlayersTurn)
            return opponentBoard
        else
            return playerBoard
    }

    private fun flipTurn() {
        isPlayersTurn = !isPlayersTurn
    }


}

data class PlayerWonEvent(val pointAttacked: Point)
data class PlayerMissEvent(val pointAttacked: Point)
data class PlayerHitEvent(val pointAttacked: Point)
data class PlayerSunkEvent(val pointAttacked: Point, val points: List<Point>)

data class OpponentWonEvent(val pointAttacked: Point)
data class OpponentMissEvent(val pointAttacked: Point)
data class OpponentHitEvent(val pointAttacked: Point)
data class OpponentSunkEvent(val pointAttacked: Point, val points: List<Point>)