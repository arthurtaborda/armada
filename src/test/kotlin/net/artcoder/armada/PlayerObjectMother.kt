package net.artcoder.armada

import net.artcoder.armada.ships.*

object PlayerObjectMother {


    fun playerOne(): Player {
        val player = Player()
        player.placeShip(Submarine(), Vertical(0, 0))
        player.placeShip(Submarine(), Vertical(1, 0))
        player.placeShip(Destroyer(), Vertical(2, 0))
        player.placeShip(Destroyer(), Vertical(3, 0))
        player.placeShip(Cruiser(), Vertical(4, 0))
        player.placeShip(Battleship(), Vertical(5, 0))
        player.placeShip(AircraftCarrier(), Vertical(6, 0))

        return player
    }

    fun playerOnePoints(): MutableList<Cell> {
        return mutableListOf(Cell(0, 0),
                             Cell(1, 0),
                             Cell(2, 0),
                             Cell(3, 0),
                             Cell(4, 0),
                             Cell(5, 0),
                             Cell(6, 0),
                             Cell(2, 1),
                             Cell(3, 1),
                             Cell(4, 1),
                             Cell(5, 1),
                             Cell(6, 1),
                             Cell(4, 2),
                             Cell(5, 2),
                             Cell(6, 2),
                             Cell(5, 3),
                             Cell(6, 3),
                             Cell(6, 4))
    }

    fun playerTwo(): Player {
        val player = Player()
        player.placeShip(Submarine(), Vertical(0, 9))
        player.placeShip(Submarine(), Vertical(1, 9))
        player.placeShip(Destroyer(), Vertical(2, 8))
        player.placeShip(Destroyer(), Vertical(3, 8))
        player.placeShip(Cruiser(), Vertical(4, 7))
        player.placeShip(Battleship(), Vertical(5, 6))
        player.placeShip(AircraftCarrier(), Vertical(6, 5))

        return player
    }

    fun playerTwoPoints(): MutableList<Cell> {
        return mutableListOf(Cell(0, 9),
                             Cell(1, 9),
                             Cell(2, 9),
                             Cell(3, 9),
                             Cell(4, 9),
                             Cell(5, 9),
                             Cell(6, 9),
                             Cell(2, 8),
                             Cell(3, 8),
                             Cell(4, 8),
                             Cell(5, 8),
                             Cell(6, 8),
                             Cell(4, 7),
                             Cell(5, 7),
                             Cell(6, 7),
                             Cell(5, 6),
                             Cell(6, 6),
                             Cell(6, 5))
    }
}