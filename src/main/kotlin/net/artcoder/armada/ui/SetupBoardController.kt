package net.artcoder.armada.ui

import net.artcoder.armada.*
import net.artcoder.armada.ships.*
import tornadofx.*

class SetupBoardController : Controller() {

    private var isHorizontal = true

    private val setupBoard = defaultBoard()

    fun canPlace(point: Point): Boolean {
        val direction = direction(point)
        return setupBoard.canPlace(direction)
    }

    fun pointsToHint(point: Point): List<Point> {
        val direction = direction(point)
        return setupBoard.pointsAvailable(direction)
    }

    fun placeShip(point: Point) {
        setupBoard.placeShip(direction(point))
    }

    fun canFinish(): Boolean {
        return setupBoard.canFinish()
    }

    private fun direction(point: Point): Direction {
        if (isHorizontal) {
            return Horizontal(point)
        } else {
            return Vertical(point)
        }
    }

    fun rotate() {
        isHorizontal = !isHorizontal
    }

    fun createBoard(): Board {
        return setupBoard.finish()
    }

    private fun defaultBoard(): SetupBoard {
        return SetupBoard(10, mutableListOf(
                Submarine(), Submarine(),
                Destroyer(), Destroyer(),
                Cruiser(), Battleship(), AircraftCarrier()))
    }
}