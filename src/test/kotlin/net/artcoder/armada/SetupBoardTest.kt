package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.core.Point
import net.artcoder.armada.setup.PlacingPoints
import net.artcoder.armada.setup.SetupBoard
import org.junit.Before
import org.junit.Test

class SetupBoardTest {

    private var board: SetupBoard = SetupBoard(intArrayOf(2, 2, 3, 3, 4, 5))

    @Before
    fun setUp() {
        board = SetupBoard(intArrayOf(2, 2, 3, 3, 4, 5))
    }

    @Test
    fun givenNewBoard_returnPointsFromSmallerShip() {
        assertThat(board.placingPointsFrom(Point(1, 1))).isEqualTo(PlacingPoints(listOf(Point(1, 1),
                                                                                        Point(2,
                                                                                                                                                1)), true))
        assertThat(board.placingPointsFrom(Point(3, 4))).isEqualTo(PlacingPoints(listOf(Point(3, 4),
                                                                                        Point(4,
                                                                                                                                                4)), true))
    }

    @Test
    fun givenPointNextToEndOfBoard_returnPartialPointsWithInvalidPlacement() {
        assertThat(board.placingPointsFrom(Point(9, 9))).isEqualTo(PlacingPoints(listOf(
                Point(9, 9)), false))
        assertThat(board.placingPointsFrom(Point(9, 6))).isEqualTo(PlacingPoints(listOf(
                Point(9, 6)), false))
    }

    @Test
    fun givenPointNextToAnotherShip_returnAllPossiblePointsAndInvalidPlacement() {
        board.place(Point(1, 1))

        assertThat(board.placingPointsFrom(Point(1, 1)))
                .isEqualTo(PlacingPoints(listOf(Point(1, 1), Point(2, 1)), false))
        assertThat(board.placingPointsFrom(Point(0, 1)))
                .isEqualTo(PlacingPoints(listOf(Point(0, 1), Point(1, 1)), false))
    }

    @Test
    fun whenRotateShip_nextPointsShouldBeInVertical() {
        board.rotate()

        assertThat(board.placingPointsFrom(Point(1, 1))).isEqualTo(PlacingPoints(listOf(Point(1, 1),
                                                                                        Point(1,
                                                                                                                                                2)), true))
        assertThat(board.placingPointsFrom(Point(3, 4))).isEqualTo(PlacingPoints(listOf(Point(3, 4),
                                                                                        Point(3,
                                                                                                                                                5)), true))
    }

    @Test
    fun whenPlacingShip_nextBoardShouldTheSmallerInLine() {
        val next1 = board.place(Point(1, 1))
        val next2 = board.place(Point(2, 2))
        val next3 = board.place(Point(3, 3))
        val next4 = board.place(Point(4, 4))
        val next5 = board.place(Point(5, 5))
        val next6 = board.place(Point(2, 9))

        assertThat(next1.size).isEqualTo(2)
        assertThat(next2.size).isEqualTo(2)
        assertThat(next3.size).isEqualTo(3)
        assertThat(next4.size).isEqualTo(3)
        assertThat(next5.size).isEqualTo(4)
        assertThat(next6.size).isEqualTo(5)
    }

    @Test
    fun whenPlacingShip_returnPoints() {
        val ship1 = board.place(Point(1, 1))
        val ship2 = board.place(Point(2, 2))
        val ship3 = board.place(Point(3, 3))

        assertThat(ship1).containsAllIn(listOf(Point(1, 1), Point(2, 1)))
        assertThat(ship2).containsAllIn(listOf(Point(2, 2), Point(3, 2)))
        assertThat(ship3).containsAllIn(listOf(Point(3, 3), Point(4, 3),
                                               Point(5, 3)))
    }

    @Test
    fun whenPlacingShipInInvalidPoint_doNotPlace() {
        val ship1 = board.place(Point(9, 1))
        val ship2 = board.place(Point(9, 5))
        val ship3 = board.place(Point(9, 9))

        assertThat(ship1).isEmpty()
        assertThat(ship2).isEmpty()
        assertThat(ship3).isEmpty()
        assertThat(board.placingPointsFrom(Point(1, 1)).points.size).isEqualTo(2) // assert that didn't place in the end
    }

    @Test
    fun whenPlacedAllShips_pointsShouldBeEmptyAndInvalid() {
        board.place(Point(1, 1))
        board.place(Point(2, 2))
        board.place(Point(3, 3))
        board.place(Point(4, 4))
        board.place(Point(5, 5))
        board.place(Point(2, 9))

        assertThat(board.placingPointsFrom(Point(1, 1)).points).isEmpty()
        assertThat(board.placingPointsFrom(Point(1, 1)).validPlacement).isFalse()
    }

    @Test
    fun whenAllShipsPlaced_canStartGame() {
        assertThat(board.allShipsPlaced).isFalse()
        board.place(Point(1, 1))
        assertThat(board.allShipsPlaced).isFalse()
        board.place(Point(2, 2))
        assertThat(board.allShipsPlaced).isFalse()
        board.place(Point(3, 3))
        assertThat(board.allShipsPlaced).isFalse()
        board.place(Point(4, 4))
        assertThat(board.allShipsPlaced).isFalse()
        board.place(Point(5, 5))
        assertThat(board.allShipsPlaced).isFalse()
        board.place(Point(2, 9))
        assertThat(board.allShipsPlaced).isTrue()
    }
}