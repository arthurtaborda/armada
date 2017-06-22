package net.artcoder.armada

import net.artcoder.armada.Player.State.CREATING_BOARD
import net.artcoder.armada.Player.State.READY_TO_START
import net.artcoder.armada.ships.*
import java.util.*

class Player {

    enum class State {
        CREATING_BOARD, READY_TO_START, ATTACKING, WAITING, WON, LOST
    }

    var state = CREATING_BOARD
    private var board: Board? = null

    private val setupBoard = SetupBoard(10, mutableListOf(
            Submarine(), Submarine(),
            Destroyer(), Destroyer(),
            Cruiser(), Battleship(), AircraftCarrier()))

    val availableShips = setupBoard.availableShips


    fun placeShip(ship: Ship, direction: Direction): UUID {
        val shipId = setupBoard.add(ship, direction)

        if(availableShips.isEmpty()) {
            board = setupBoard.finish()
            state = READY_TO_START
        }

        return shipId
    }

    fun removeShip(shipId: UUID) {
        setupBoard.remove(shipId)

        if (availableShips.isNotEmpty()) {
            board = null
            state = CREATING_BOARD
        }
    }

    fun attack(opponent: Player, point: Point): AttackResult {
        return opponent.receiveAttack(point)
    }

    private fun receiveAttack(point: Point): AttackResult {
        return board!!.attack(point)
    }

    fun allShipsDestroyed(): Boolean {
        return board!!.allShipsDestroyed()
    }
}

