package net.artcoder.armada.ui

import net.artcoder.armada.*
import net.artcoder.armada.ships.*

object RandomPlayer {

    fun create(): Player {
        val setupBoard = SetupBoard(10, mutableListOf(
                Submarine(), Submarine(),
                Destroyer(), Destroyer(),
                Cruiser(), Battleship(), AircraftCarrier()))
        val directionGenerator = RandomDirectionGenerator(RandomPointGenerator(Point(10, 10)))
        val boardGenerator = BoardGenerator(setupBoard, directionGenerator)

        return Player(boardGenerator.randomBoard())
    }
}