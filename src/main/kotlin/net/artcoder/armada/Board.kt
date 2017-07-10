package net.artcoder.armada

class Board(private val size: Int,
            private val placedShips: List<PlacedShip>) {

    private val table = Array(size) { Array(size) { false } } //matrix of (size x size) with all false elements
    private val attacks = Array(size) { Array(size) { false } } //matrix of (size x size) with all false elements

    init {
        placedShips
                .flatMap { it.points }
                .forEach { table[it.x][it.y] = true }
    }

    fun attack(point: Point): AttackResult {
        if (attacks[point.x][point.y]) {
            throw PointAlreadyAttackedException(point)
        }

        attacks[point.x][point.y] = true

        if (table[point.x][point.y]) {
            return if (placedShips.any { ship -> ship.hit(point) && ship.isDead() })
                AttackResult.SUNK
            else
                AttackResult.HIT
        } else {
            return AttackResult.MISS
        }
    }

    fun allShipsDestroyed(): Boolean {
        return placedShips.stream().allMatch { it.isDead() }
    }

    fun isAttacked(point: Point): Boolean {
        return attacks[point.x][point.y]
    }

    fun pointsOfPlacedShips(): List<Point> {
        return placedShips.flatMap { it.points }.toList()
    }

    fun pointsOfShipIn(point: Point): List<Point> {
        return placedShips
                .filter { it.points.contains(point) }
                .flatMap { it.points }
                .toList()
    }
}

