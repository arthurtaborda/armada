package net.artcoder.armada

object PlayerObjectMother {


    fun playerOne(): Player {
        val setupBoard = setupBoard()
        setupBoard.placeShip(Vertical(6, 0))
        setupBoard.placeShip(Vertical(5, 0))
        setupBoard.placeShip(Vertical(4, 0))
        setupBoard.placeShip(Vertical(2, 0))
        setupBoard.placeShip(Vertical(3, 0))
        setupBoard.placeShip(Vertical(1, 0))
        setupBoard.placeShip(Vertical(0, 0))

        return Player(setupBoard.finish())
    }

    private fun setupBoard(): SetupBoard {
        return SetupBoard(10, listOf(1, 1, 2, 2, 3, 4, 5))
    }

    fun playerOnePoints(): MutableList<Point> {
        return mutableListOf(Point(0, 0),
                             Point(1, 0),
                             Point(2, 0),
                             Point(3, 0),
                             Point(4, 0),
                             Point(5, 0),
                             Point(6, 0),
                             Point(2, 1),
                             Point(3, 1),
                             Point(4, 1),
                             Point(5, 1),
                             Point(6, 1),
                             Point(4, 2),
                             Point(5, 2),
                             Point(6, 2),
                             Point(5, 3),
                             Point(6, 3),
                             Point(6, 4))
    }

    fun playerTwo(): Player {
        val setupBoard = setupBoard()
        setupBoard.placeShip(Vertical(6, 5))
        setupBoard.placeShip(Vertical(5, 6))
        setupBoard.placeShip(Vertical(4, 7))
        setupBoard.placeShip(Vertical(3, 8))
        setupBoard.placeShip(Vertical(2, 8))
        setupBoard.placeShip(Vertical(1, 9))
        setupBoard.placeShip(Vertical(0, 9))

        return Player(setupBoard.finish())
    }

    fun playerTwoPoints(): MutableList<Point> {
        return mutableListOf(Point(0, 9),
                             Point(1, 9),
                             Point(2, 9),
                             Point(3, 9),
                             Point(4, 9),
                             Point(5, 9),
                             Point(6, 9),
                             Point(2, 8),
                             Point(3, 8),
                             Point(4, 8),
                             Point(5, 8),
                             Point(6, 8),
                             Point(4, 7),
                             Point(5, 7),
                             Point(6, 7),
                             Point(5, 6),
                             Point(6, 6),
                             Point(6, 5))
    }
}