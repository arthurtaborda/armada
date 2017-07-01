package net.artcoder.armada

class BoardGenerator(private val setupBoard: SetupBoard,
                     private val directionGenerator: DirectionGenerator) {

    fun randomBoard(): Board {
        while (!setupBoard.canFinish()) {
            val direction = directionGenerator.randomDirection()
            if(setupBoard.canPlace(direction)) {
                setupBoard.placeShip(direction)
            }
        }

        return setupBoard.finish()
    }
}