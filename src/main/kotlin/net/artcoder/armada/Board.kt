package net.artcoder.armada

class Board(private val placedShips: List<PlacedShip>) {

    fun  attack(point: Point): AttackResult {
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

}

enum class AttackResult {
    HIT, MISS, SUNK
}