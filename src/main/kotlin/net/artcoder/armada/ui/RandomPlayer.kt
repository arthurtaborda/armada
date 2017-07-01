package net.artcoder.armada.ui

import net.artcoder.armada.*

object RandomPlayer {

    fun create(): Player {
        val setupBoard = SetupBoard(10, listOf(1, 1, 2, 2, 3, 4, 5))
        val directionGenerator = RandomDirectionGenerator(RandomPointGenerator(10))
        val boardGenerator = BoardGenerator(setupBoard, directionGenerator)

        return Player(boardGenerator.randomBoard())
    }
}