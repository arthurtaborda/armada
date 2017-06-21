package net.artcoder.armada

class BoardBuilder {

    private val ships = mutableListOf<Pair<Ship, Direction>>()

    fun withShip(ship: Ship, direction: Direction): BoardBuilder {
        ships.add(Pair(ship, direction))
        return this
    }

    fun build(): Board {
        ships.map { it.first }
                .toCollection(arrayListOf())
        val setupBoard = SetupBoard(10, ships.map { it.first }.toCollection(arrayListOf()))
        ships.forEach({ setupBoard.add(it.first, it.second) })
        return setupBoard.finish()
    }
}