package net.artcoder.armada

import net.artcoder.armada.Player.State.CREATING_BOARD

class Player(val board: Board) {

    enum class State {
        CREATING_BOARD, READY_TO_START, ATTACKING, WAITING, WON, LOST
    }

    var state = CREATING_BOARD

    fun attack(opponent: Player, point: Point): AttackResult {
        return opponent.receiveAttack(point)
    }

    private fun receiveAttack(point: Point): AttackResult {
        return board.attack(point)
    }

    fun allShipsDestroyed(): Boolean {
        return board.allShipsDestroyed()
    }

    fun canAttack(opponent: Player, point: Point): Boolean {
        return !opponent.isAttacked(point)
    }

    private fun isAttacked(point: Point): Boolean {
        return board.isAttacked(point)
    }
}

