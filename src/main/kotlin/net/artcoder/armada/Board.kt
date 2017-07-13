package net.artcoder.armada

class Board(private val placedShips: List<PlacedShip>) {

    private val attackedPoints = mutableListOf<Point>()

    fun attack(point: Point): AttackResult {
        attackedPoints.add(point)
        val isHit = placedShips.any { it.points.contains(point) }
        if(isHit) {
            val isSunk = placedShips.any { ship -> ship.hit(point) &&
                                                   ship.isDead() }
            if (isSunk) {
                return AttackResult.SUNK
            } else {
                return AttackResult.HIT
            }
        }
        return AttackResult.MISS
    }

    fun pointsOfShipIn(point: Point): List<Point> {
        return placedShips
                .filter { it.points.contains(point) }
                .flatMap { it.points }
                .toList()
    }

    fun canAttack(point: Point): Boolean {
        return !attackedPoints.contains(point)
    }

}

enum class AttackResult {
    HIT, MISS, SUNK
}