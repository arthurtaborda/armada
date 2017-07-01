package net.artcoder.armada

class BoardBuilder {

    private val ships = mutableListOf<Pair<Int, Direction>>()

    fun withShip(shipSize: Int, direction: Direction): BoardBuilder {
        ships.add(Pair(shipSize, direction))
        return this
    }

    fun build(): Board {
        ships.map { it.first }
                .toCollection(arrayListOf())
        val setupBoard = SetupBoard(10, ships.map { it.first }.toCollection(arrayListOf()))
        ships.forEach({ setupBoard.placeShip(it.second) })
        return setupBoard.finish()
    }
}