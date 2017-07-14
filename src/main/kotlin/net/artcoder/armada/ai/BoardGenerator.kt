package net.artcoder.armada.ai

import net.artcoder.armada.match.Board
import net.artcoder.armada.setup.SetupBoard
import java.util.concurrent.ThreadLocalRandom

class BoardGenerator(private val ships: IntArray,
                     private val pointGenerator: PointGenerator) {

    private val rand = ThreadLocalRandom.current()

    fun randomBoard(): Board {
        val setupBoard = SetupBoard(ships)

        while (!setupBoard.allShipsPlaced) {
            if (rand.nextBoolean()) {
                setupBoard.rotate()
            }

            val point = pointGenerator.randomPoint()
            if (setupBoard.canPlace(point)) {
                setupBoard.place(point)
            }
        }

        return setupBoard.finish()
    }
}