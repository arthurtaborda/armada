package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.ships.*
import org.testng.Assert.fail
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class SetupBoardTest {

    private var board: SetupBoard? = null

    private val ships = mutableListOf(
            Submarine(), Submarine(),
            Destroyer(), Destroyer(),
            Cruiser(), Battleship(), AircraftCarrier())


    @BeforeMethod
    fun setUp() {
        board = SetupBoard(10, ships)
    }

    /*
    when finish without placing all ships
    throw ShipsUnplacedException
     */
    @Test
    fun testFinishWithoutPlacingAllShips() {
        val availableShips = mutableListOf(Destroyer(), Submarine())
        val setupBoard = SetupBoard(10, availableShips)

        assertShipsAreUnplaced(setupBoard, availableShips)

        setupBoard.add(availableShips.removeAt(0), Vertical(0, 0))

        assertShipsAreUnplaced(setupBoard, availableShips)
    }

    /*
    when finish placing all ships
    create board
     */
    @Test
    fun testFinishPlacingAllShips() {
        val availableShips = mutableListOf(Destroyer(), Submarine())
        val setupBoard = SetupBoard(10, availableShips)

        setupBoard.add(availableShips.removeAt(0), Vertical(0, 0))
        setupBoard.add(availableShips.removeAt(0), Vertical(1, 0))

        assertThat(setupBoard.finish()).isNotNull()
    }

    /*
    given setup board with destroyer and submarine
    when places a cruiser
    throw ShipNotAvailableException
     */
    @Test(expectedExceptions = arrayOf(ShipNotAvailableException::class))
    fun testPlacingShip() {
        val availableShips = mutableListOf(Destroyer(), Submarine())
        val setupBoard = SetupBoard(10, availableShips)

        setupBoard.add(Cruiser(), Vertical(0, 0))
    }

    /*
    given setup board with destroyer and submarine and a placed destroyer
    when removes destroyer
    destroyer should be available to place again
     */
    @Test
    fun testRemoveShip() {
        val availableShips = mutableListOf(Destroyer(), Submarine())
        val setupBoard = SetupBoard(10, availableShips)

        val destroyerId = setupBoard.add(Destroyer(), Horizontal(5,5))
        setupBoard.remove(destroyerId)

        val destroyerId2 = setupBoard.add(Destroyer(), Horizontal(5, 5))

        assertThat(destroyerId).isNotEqualTo(destroyerId2)
    }

    /*
        when add submarine on (3,3) horizontally
        it should be on point (3,3) only
         */
    @Test
    fun testAddOneSubmarine() {
        val ship = Submarine()
        add(ship, Horizontal(3, 3))

        assertPoints(Submarine().name, listOf(Point(3, 3)))
    }

    /*
    when add two submarines on (3,3) horizontally and (7,7) vertically
    it should be on point (3,3) and (7,7)
     */
    @Test
    fun testAddTwoSubmarines() {
        add(Submarine(), Horizontal(3, 3))
        add(Submarine(), Vertical(7, 7))

        assertPoints(Submarine().name, listOf(Point(3, 3),
                                                Point(7, 7)))
    }

    /*
    when add destroyer on (2,2) horizontally
    it should be on points (2,2) and (3,2)
     */
    @Test
    fun testAddOneDestroyer() {
        add(Destroyer(), Horizontal(2, 2))

        assertPoints(Destroyer().name, listOf(Point(2, 2),
                                                Point(3, 2)))
    }

    /*
    when add two destroyers on (2,2) horizontally and (6,6) vertically
    it should be on points (2,2), (3,2), (6,6) and (6,7)
     */
    @Test
    fun testAddTwoDestroyers() {
        add(Destroyer(), Horizontal(2, 2))
        add(Destroyer(), Vertical(6, 6))

        assertPoints(Destroyer().name, listOf(Point(2, 2),
                                                Point(3, 2),
                                                Point(6, 6),
                                                Point(6, 7)))
    }

    /*
    when add destroyer on (4,4) vertically
    it should be on points (4,4), (4,5) and (4,6)
     */
    @Test
    fun testAddOneCruiser() {
        add(Cruiser(), Vertical(4, 4))

        assertPoints(Cruiser().name, listOf(Point(4, 4),
                                              Point(4, 5),
                                              Point(4, 6)))
    }

    /*
    when add battleship on (5,5) vertically
    it should be on points (5,5), (5,6), (5,7) and (5,8)
     */
    @Test
    fun testAddOneBattleship() {
        add(Battleship(), Vertical(5, 5))

        assertPoints(Battleship().name, listOf(Point(5, 5),
                                                 Point(5, 6),
                                                 Point(5, 7),
                                                 Point(5, 8)))
    }

    /*
    when add aircraft carrier on (0,0) horizontally
    it should be on points (0,0), (1,0), (2,0), (3,0) and (4,0)
     */
    @Test
    fun testAddOneAircraftCarrier() {
        add(AircraftCarrier(), Horizontal(0, 0))

        assertPoints(AircraftCarrier().name, listOf(Point(0, 0),
                                                      Point(1, 0),
                                                      Point(2, 0),
                                                      Point(3, 0),
                                                      Point(4, 0)))
    }

    /*
    when add ship horizontally out of bounds
    it should throw PlacementOutOfBoundsException
     */
    @Test
    fun testHorizontallyAddShipOutOfBounds() {
        assertOutOfBounds(Submarine(), Horizontal(10, 10))
        assertOutOfBounds(Destroyer(), Horizontal(9, 9))
        assertOutOfBounds(Cruiser(), Horizontal(8, 8))
        assertOutOfBounds(Battleship(), Horizontal(7, 7))
        assertOutOfBounds(AircraftCarrier(), Horizontal(6, 6))
    }

    /*
    when add ship vertically out of bounds
    it should throw PlacementOutOfBoundsException
     */
    @Test
    fun testVerticallyAddShipOutOfBounds() {
        assertOutOfBounds(Submarine(), Vertical(10, 10))
        assertOutOfBounds(Destroyer(), Vertical(9, 9))
        assertOutOfBounds(Cruiser(), Vertical(8, 8))
        assertOutOfBounds(Battleship(), Vertical(7, 7))
        assertOutOfBounds(AircraftCarrier(), Vertical(6, 6))
    }


    /*
    when add one ship and then add second ship with overlapping
    it should throw ShipOverlapException
     */
    @Test
    fun testOverlapping() {
        assertShipOverlap(Submarine(), Vertical(2, 2),
                          Destroyer(), Horizontal(1, 2))
        assertShipOverlap(Destroyer(), Vertical(2, 2),
                          Cruiser(), Horizontal(0, 3))
        assertShipOverlap(Cruiser(), Vertical(4, 4),
                          Battleship(), Horizontal(1, 6))
        assertShipOverlap(Battleship(), Vertical(4, 4),
                          AircraftCarrier(), Horizontal(0, 7))
    }

    private fun assertOutOfBounds(ship: Ship, direction: Direction) {
        try {
            board?.add(ship, direction)
            fail()
        } catch(e: PlacementOutOfBoundsException) {
        }
    }

    private fun assertShipOverlap(ship1: Ship, direction1: Direction,
                                  ship2: Ship, direction2: Direction) {
        try {
            board?.add(ship1, direction1)
            board?.add(ship2, direction2)
            fail()
        } catch(e: ShipOverlapException) {
        }
    }

    private fun assertPoints(shipName: String, points: List<Point>) {
        assertThat(board?.pointsOf(shipName)).containsAllIn(points)
    }

    private fun assertShipsAreUnplaced(setupBoard: SetupBoard,
                                       availableShips: List<Ship>) {
        try {
            setupBoard.finish()
            fail()
        } catch(e: ShipsUnplacedException) {
            assertThat(e.ships).containsAllIn(availableShips)
        }
    }

    private fun add(ship: Ship, direction: Direction) {
        board?.add(ship, direction)
    }
}
