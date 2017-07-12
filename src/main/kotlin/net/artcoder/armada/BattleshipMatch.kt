package net.artcoder.armada

import com.google.common.eventbus.EventBus

class BattleshipMatch(private val eventBus: EventBus,
                      private val playerBoard: Board,
                      private val opponentBoard: Board) {

    private var isPlayersTurn = true

    fun attack(point: Point) {
        val attackResult = getBoardToAttack().attack(point)
        when (attackResult) {
            AttackResult.MISS  -> miss(point)
            AttackResult.HIT -> hit(point)
            AttackResult.SUNK -> sunk(point)
        }

    }

    private fun sunk(point: Point) {
        if (isPlayersTurn) {
            eventBus.post(PlayerSunkEvent(opponentBoard.pointsOfShipIn(point)))
        } else {
            eventBus.post(OpponentSunkEvent(playerBoard.pointsOfShipIn(point)))
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
            eventBus.post(PlayerMissEvent(point))
        } else {
            eventBus.post(OpponentMissEvent(point))
        }
        flipTurn()
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

data class PlayerMissEvent(val point: Point)
data class PlayerHitEvent(val point: Point)
data class PlayerSunkEvent(val points: List<Point>)

data class OpponentMissEvent(val point: Point)
data class OpponentHitEvent(val point: Point)
data class OpponentSunkEvent(val points: List<Point>)