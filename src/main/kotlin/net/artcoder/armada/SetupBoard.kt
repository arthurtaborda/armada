package net.artcoder.armada

class SetupBoard(ships: IntArray) {

    private val boardSize = 10

    val allShipsPlaced: Boolean
        get() {
            return shipsInHold.isEmpty()
        }


    private val shipsInHold = ships.sorted().toMutableList()

    private val nextShipToPlace: Int?
        get() = shipsInHold.firstOrNull()
    private var isHorizontal = true

    private val placedShips = mutableListOf<PlacedShip>()

    fun nextPlacingPoints(point: Point): PlacingPoints {
        val points = mutableListOf<Point>()
        nextShipToPlace?.let {
            for (i in 0 until it) {
                if (isHorizontal) {
                    val x = point.x + i
                    if (x >= boardSize) {
                        return PlacingPoints(points, false)
                    }
                    val pointToPlace = Point(x, point.y)
                    points.add(pointToPlace)
                } else {
                    val y = point.y + i
                    if (y >= boardSize) {
                        return PlacingPoints(points, false)
                    }
                    points.add(Point(point.x, y))
                }
            }
            val somePointOverlap = points.any { overlapPlacedBoard(it) }
            return PlacingPoints(points, !somePointOverlap)
        }

        return PlacingPoints(emptyList(), false)
    }

    fun rotate() {
        isHorizontal = !isHorizontal
    }

    fun place(point: Point): List<Point> {
        val placingPoints = nextPlacingPoints(point)

        if(!placingPoints.validPlacement) {
            return emptyList()
        }

        if (shipsInHold.isNotEmpty()) {
            shipsInHold.removeAt(0)
        }

        placedShips.add(PlacedShip(placingPoints.points))

        return placingPoints.points
    }

    fun canPlace(point: Point): Boolean {
        nextShipToPlace?.let {
            for (i in 0 until it) {
                if (isHorizontal) {
                    if (point.x >= boardSize) {
                        return false
                    }
                } else {
                    if (point.y >= boardSize) {
                        return false
                    }
                }
            }
        }

        return !overlapPlacedBoard(point)
    }

    private fun overlapPlacedBoard(point: Point): Boolean {
        return placedShips.any { it.points.contains(point) }
    }

    fun finish(): Board {
        return Board(placedShips)
    }
}

