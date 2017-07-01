package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import org.testng.Assert.fail
import org.testng.annotations.Test
import java.util.*

class SetupBoardTest {

    private val ships = listOf(1, 1, 2, 2, 3, 4, 5)
    /*
    when finish without placing all ships
    throw ShipsUnplacedException
     */
    @Test
    fun testFinishWithoutPlacingAllShips() {
        val availableShips = listOf(2, 1)
        val setupBoard = setupBoard(availableShips)

        assertShipsAreUnplaced(setupBoard)

        setupBoard.placeShip(Vertical(0, 0))

        assertShipsAreUnplaced(setupBoard)
    }

    /*
    when finish placing all ships
    create board
     */
    @Test
    fun testFinishPlacingAllShips() {
        val availableShips = listOf(2, 1)
        val setupBoard = setupBoard(availableShips)

        setupBoard.placeShip(Vertical(0, 0))
        setupBoard.placeShip(Vertical(1, 0))

        assertThat(setupBoard.finish()).isNotNull()
    }

    /*
    given setup board with destroyer and submarine and a placed destroyer
    when removes destroyer
    destroyer should be available to place again
     */
    @Test
    fun testRemoveShip() {
        val availableShips = listOf(2, 1)
        val setupBoard = setupBoard(availableShips)

        val destroyerId = setupBoard.placeShip(Horizontal(5, 5))
        setupBoard.remove(destroyerId)

        val destroyerId2 = setupBoard.placeShip(Horizontal(5, 5))

        assertThat(destroyerId).isNotEqualTo(destroyerId2)
    }

    /*
    when place ship vertically
    it should be on initial point plus ship size on right
     */
    @Test
    fun testPlaceVertically() {
        val board = setupBoard(listOf(3, 4, 5, 2, 1))
        val aircraftCarrier = board.placeShip(Vertical(0, 0))
        val battleship = board.placeShip(Vertical(1, 1))
        val cruiser = board.placeShip(Vertical(2, 2))
        val destroyer = board.placeShip(Vertical(3, 3))
        val submarine = board.placeShip(Vertical(4, 4))

        assertPoints(board, aircraftCarrier, listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3), Point(0, 4)))
        assertPoints(board, battleship, listOf(Point(1, 1), Point(1, 2), Point(1, 3), Point(1, 4)))
        assertPoints(board, cruiser, listOf(Point(2, 2), Point(2, 3), Point(2, 4)))
        assertPoints(board, destroyer, listOf(Point(3, 3), Point(3, 4)))
        assertPoints(board, submarine, listOf(Point(4, 4)))
    }

    /*
    when placeShip ship horizontally out of bounds
    it should throw PlacementOutOfBoundsException
     */
    @Test
    fun testHorizontallyAddShipOutOfBounds() {
        assertOutOfBounds(Horizontal(10, 10))
        assertOutOfBounds(Horizontal(9, 9))
        assertOutOfBounds(Horizontal(8, 8))
        assertOutOfBounds(Horizontal(7, 7))
        assertOutOfBounds(Horizontal(6, 6))
    }

    /*
    when placeShip ship vertically out of bounds
    it should throw PlacementOutOfBoundsException
     */
    @Test
    fun testVerticallyAddShipOutOfBounds() {
        assertOutOfBounds(Vertical(10, 10))
        assertOutOfBounds(Vertical(9, 9))
        assertOutOfBounds(Vertical(8, 8))
        assertOutOfBounds(Vertical(7, 7))
        assertOutOfBounds(Vertical(6, 6))
    }

    /*
    when placeShip one ship and then placeShip second ship with overlapping
    it should throw ShipOverlapException
     */
    @Test
    fun testOverlapping() {
        assertShipOverlap(Vertical(2, 2), Horizontal(1, 2))
        assertShipOverlap(Vertical(2, 2), Horizontal(0, 3))
        assertShipOverlap(Vertical(4, 4), Horizontal(1, 6))
        assertShipOverlap(Horizontal(0, 7), Vertical(4, 4))
    }


    /*
    given current ship to place a cruiser
    when placing horizontally on (8,8) canPlace = false
    when placing horizontally on (9,9) canPlace = false
    when placing horizontally on (7,7) canPlace = true
    when placing horizontally on (0,0) canPlace = true
     */
    @Test
    fun testCheckShipAvailabilityHorizontally() {
        val availableShips = listOf(3)
        val setupBoard = setupBoard(availableShips)

        val canPlaceOn88 = setupBoard.canPlace(Horizontal(8, 8))
        val canPlaceOn99 = setupBoard.canPlace(Horizontal(9, 9))
        val canPlaceOn77 = setupBoard.canPlace(Horizontal(7, 7))
        val canPlaceOn00 = setupBoard.canPlace(Horizontal(0, 0))

        assertThat(canPlaceOn88).isFalse()
        assertThat(canPlaceOn99).isFalse()
        assertThat(canPlaceOn77).isTrue()
        assertThat(canPlaceOn00).isTrue()
    }

    /*
    given current ship to place a cruiser
    when placing vertically on (8,8) canPlace = false
    when placing vertically on (9,9) canPlace = false
    when placing vertically on (7,7) canPlace = true
    when placing vertically on (0,0) canPlace = true
     */
    @Test
    fun testCheckShipAvailabilityVertically() {
        val availableShips = listOf(3)
        val setupBoard = setupBoard(availableShips)

        val canPlaceOn88 = setupBoard.canPlace(Vertical(8, 8))
        val canPlaceOn99 = setupBoard.canPlace(Vertical(9, 9))
        val canPlaceOn77 = setupBoard.canPlace(Vertical(7, 7))
        val canPlaceOn00 = setupBoard.canPlace(Vertical(0, 0))

        assertThat(canPlaceOn88).isFalse()
        assertThat(canPlaceOn99).isFalse()
        assertThat(canPlaceOn77).isTrue()
        assertThat(canPlaceOn00).isTrue()
    }

    /*
    given current ship a battleship
    when placing horizontally
    return possible points inside board
     */
    @Test
    fun testPointsAvailableHorizontally() {
        val availableShips = listOf(4)
        val setupBoard = setupBoard(availableShips)

        val pointsAvailableOn99 = setupBoard.pointsAvailable(Horizontal(9, 9))
        val pointsAvailableOn88 = setupBoard.pointsAvailable(Horizontal(8, 8))
        val pointsAvailableOn77 = setupBoard.pointsAvailable(Horizontal(7, 7))
        val pointsAvailableOn00 = setupBoard.pointsAvailable(Horizontal(0, 0))

        assertThat(pointsAvailableOn99).containsAllIn(listOf(Point(9, 9)))
        assertThat(pointsAvailableOn88).containsAllIn(listOf(Point(8, 8), Point(9, 8)))
        assertThat(pointsAvailableOn77).containsAllIn(listOf(Point(7, 7), Point(8, 7), Point(9, 7)))
        assertThat(pointsAvailableOn00).containsAllIn(listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)))
    }

    /*
    given current ship a battleship
    when placing vertically
    return possible points inside board
     */
    @Test
    fun testPointsAvailableVertically() {
        val availableShips = listOf(4)
        val setupBoard = setupBoard(availableShips)

        val pointsAvailableOn99 = setupBoard.pointsAvailable(Vertical(9, 9))
        val pointsAvailableOn88 = setupBoard.pointsAvailable(Vertical(8, 8))
        val pointsAvailableOn77 = setupBoard.pointsAvailable(Vertical(7, 7))
        val pointsAvailableOn00 = setupBoard.pointsAvailable(Vertical(0, 0))

        assertThat(pointsAvailableOn99).containsAllIn(listOf(Point(9, 9)))
        assertThat(pointsAvailableOn88).containsAllIn(listOf(Point(8, 8), Point(8, 9)))
        assertThat(pointsAvailableOn77).containsAllIn(listOf(Point(7, 7), Point(7, 8), Point(7, 9)))
        assertThat(pointsAvailableOn00).containsAllIn(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3)))
    }

    /*
    when board can finish
    cannot place anything
     */
    @Test
    fun testPlacementOnFinishBoard() {
        val availableShips = listOf<Int>()
        val setupBoard = setupBoard(availableShips)

        assertThat(setupBoard.canPlace(Horizontal(4, 4))).isFalse()
    }

    private fun assertOutOfBounds(direction: Direction) {
        val board = setupBoard(ships)
        assertThat(board.canPlace(direction)).isFalse()
    }


    private fun assertShipOverlap(direction1: Direction, direction2: Direction) {
        val board = setupBoard(ships)
        board.placeShip(direction1)
        assertThat(board.canPlace(direction2)).isFalse()
    }

    private fun assertPoints(board: SetupBoard, uuid: UUID, points: List<Point>) {
        assertThat(board.pointsOf(uuid)).containsAllIn(points)
    }

    private fun assertShipsAreUnplaced(setupBoard: SetupBoard) {
        try {
            setupBoard.finish()
            fail()
        } catch(e: ShipsUnplacedException) {
        }
    }

    private fun setupBoard(availableShips: List<Int>) = SetupBoard(10, availableShips)
}
