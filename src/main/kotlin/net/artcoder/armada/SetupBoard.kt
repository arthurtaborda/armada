package net.artcoder.armada

import java.util.*


class SetupBoard(private val boardSize: Int, availableShips: List<Int>) {

    private val table = Array(boardSize) { Array(boardSize) { false } } //matrix of (boardSize x boardSize) with all false elements
    private val placedShips = mutableMapOf<UUID, PlacedShip>()

    val availableShips: MutableList<Int> = availableShips.toMutableList()

    private val currentShipToPlace: Int?
        get() = availableShips
                .sortedWith(compareByDescending({ it }))
                .firstOrNull()

    fun placeShip(direction: Direction): UUID {
        val points = direction.points(currentShipToPlace!!)
        points.forEach({ table[it.x][it.y] = true })

        val placedShip = PlacedShip(points)
        placedShips[placedShip.id] = placedShip

        availableShips.remove(currentShipToPlace!!)

        return placedShip.id
    }

    fun pointsOf(uuid: UUID): List<Point> {
        val result = mutableListOf<Point>()

        placedShips
                .filter({ it.value.id == uuid })
                .forEach({ result.addAll(it.value.points) })

        return result
    }

    fun finish(): Board {
        if (!canFinish()) {
            throw ShipsUnplacedException()
        }

        return Board(boardSize, placedShips.values.toList())
    }

    fun canFinish() = availableShips.isEmpty()

    fun remove(shipId: UUID) {
        placedShips.remove(shipId)?.let {
            it.points.forEach({ table[it.x][it.y] = false })
            availableShips.add(it.points.size)
        }
    }

    fun canPlace(direction: Direction): Boolean {
        if(canFinish()) {
            return false
        }

        val points = direction.points(currentShipToPlace!!)
        val lastPoint = points.last()
        val outOfBounds = lastPoint.x >= boardSize || lastPoint.y >= boardSize

        if(!outOfBounds) {
            val overlap = points.stream().anyMatch({ table[it.x][it.y] })
            return !overlap
        }

        return false
    }

    fun pointsAvailable(direction: Direction): List<Point> {
        return direction.points(currentShipToPlace!!).filter { it.x < boardSize && it.y < boardSize }
    }
}

