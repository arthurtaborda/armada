package net.artcoder.armada

import java.util.*

class SetupBoard(private val size: Int,
                 availableShips: List<Ship>) {

    private val table = Array(size) { Array(size) { false } } //matrix of (size x size) with all false elements
    private val placedShips = mutableMapOf<UUID, PlacedShip>()

    val availableShips: MutableList<Ship> = availableShips.toMutableList()

    fun add(ship: Ship, direction: Direction): UUID {
        if (!availableShips.contains(ship)) {
            throw ShipNotAvailableException(ship)
        }

        checkPlacementAvailability(ship, direction)

        val points = direction.points(ship.size)
        points.forEach({ table[it.x][it.y] = true })

        availableShips.remove(ship)
        val placedShip = PlacedShip(ship, points)
        placedShips[placedShip.id] = placedShip

        return placedShip.id
    }

    fun pointsOf(shipName: String): List<Point> {
        val result = mutableListOf<Point>()

        placedShips
                .filter({ it.value.ship.name == shipName })
                .forEach({ result.addAll(it.value.points) })

        return result
    }

    private fun checkPlacementAvailability(ship: Ship, direction: Direction) {
        for ((x, y) in direction.points(ship.size)) {
            try {
                if (table[x][y]) {
                    throw ShipOverlapException(Point(x, y))
                }
            } catch(e: ArrayIndexOutOfBoundsException) {
                throw PlacementOutOfBoundsException(Point(x, y))
            }
        }
    }

    fun finish(): Board {
        if (availableShips.isNotEmpty()) {
            throw ShipsUnplacedException(availableShips)
        }

        return Board(size, placedShips.values.toList())
    }

    fun remove(shipId: UUID) {
        placedShips.remove(shipId)?.let {
            it.points.forEach({ table[it.x][it.y] = false })
            availableShips.add(it.ship)
        }
    }
}

