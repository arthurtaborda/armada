package net.artcoder.armada

object BoardObjectMother {

    fun opponentBoard(): Board {
        // points: (0,8),(1,8),(0,9),(1,9),(2,9)
        val setupBoard = SetupBoard(intArrayOf(2, 3))
        setupBoard.place(Point(0, 8))
        setupBoard.place(Point(0, 9))

        return setupBoard.finish()
    }

    fun playerBoard(): Board {
        // points: (0,0),(1,0),(0,1),(1,1),(2,1)
        val setupBoard = SetupBoard(intArrayOf(2, 3))
        setupBoard.place(Point(0, 0))
        setupBoard.place(Point(0, 1))

        return setupBoard.finish()
    }
}