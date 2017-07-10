package net.artcoder.armada

class Player(private val board: Board) {

    enum class State {
        ATTACKING, WAITING, WON, LOST
    }

    var state = State.ATTACKING

    fun pointsOfPlacedShips(): List<Point> {
        return board.pointsOfPlacedShips()
    }

    fun pointsOfShipIn(point: Point): List<Point> {
        return board.pointsOfShipIn(point)
    }

    fun allShipsDestroyed(): Boolean {
        return board.allShipsDestroyed()
    }

    fun canAttack(opponent: Player, point: Point): Boolean {
        return !opponent.isAttacked(point)
    }

    fun attack(opponent: Player, point: Point): AttackResult {
        return opponent.receiveAttack(point)
    }

    private fun receiveAttack(point: Point): AttackResult {
        return board.attack(point)
    }

    private fun isAttacked(point: Point): Boolean {
        return board.isAttacked(point)
    }
}

