package net.artcoder.armada

class Player(val board: Board) {

    enum class State {
        ATTACKING, WAITING, WON, LOST
    }

    var state = State.ATTACKING

    fun attack(opponent: Player, point: Point): AttackResult {
        return opponent.receiveAttack(point)
    }

    fun allShipsDestroyed(): Boolean {
        return board.allShipsDestroyed()
    }

    fun canAttack(opponent: Player, point: Point): Boolean {
        return !opponent.isAttacked(point)
    }

    private fun receiveAttack(point: Point): AttackResult {
        return board.attack(point)
    }

    private fun isAttacked(point: Point): Boolean {
        return board.isAttacked(point)
    }
}

