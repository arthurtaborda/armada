package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.ships.*
import org.testng.annotations.Test

class PlayerSetupTest {

    /*
    given new player
    player state is CREATING_BOARD
     */
    @Test
    fun testPlayerStartsCreatingBoard() {
        val player = newPlayer()

        assertThat(player.state).isEqualTo(Player.State.CREATING_BOARD)
    }

    /*
    given new player
    player available ships are 2 submarines, 2 destroyers, 1 cruiser, 1 battleship and 1 aircraft carrier
     */
    @Test
    fun testPlayersStartingShips() {
        val startingShip = mutableListOf(
                Submarine(), Submarine(),
                Destroyer(), Destroyer(),
                Cruiser(), Battleship(), AircraftCarrier())

        val player = newPlayer()

        assertThat(player.availableShips).containsAllIn(startingShip)
    }

    /*
    when player places given ships
    player have no ships of that kind
     */
    @Test
    fun testPlayerShipPlacing() {
        val expectedAvailableShips = mutableListOf(
                Destroyer(), Destroyer(),
                Cruiser(), Battleship(),
                AircraftCarrier())

        val player = newPlayer()
        player.placeShip(Submarine(), Vertical(0,0))
        player.placeShip(Submarine(), Vertical(0,1))

        assertThat(player.availableShips).containsAllIn(expectedAvailableShips)
    }


    /*
    when player places given ships and removes ship
    player has one ship of that kind available
     */
    @Test
    fun testPlayerShipRemoving() {
        val expectedAvailableShips = mutableListOf(
                Submarine(), Destroyer(), Destroyer(),
                Cruiser(), Battleship(),
                AircraftCarrier())

        val player = newPlayer()
        player.placeShip(Submarine(), Vertical(0, 0))
        val submarineTwoId = player.placeShip(Submarine(), Vertical(0, 1))
        player.removeShip(submarineTwoId)

        assertThat(player.availableShips).containsAllIn(expectedAvailableShips)
    }

    /*
    when player places given ships and places one more
    throw ShipNotAvailableException
     */
    @Test(expectedExceptions = arrayOf(ShipNotAvailableException::class))
    fun testPlayerShipOverPlacing() {
        val player = newPlayer()

        player.placeShip(Submarine(), Vertical(0, 0))
        player.placeShip(Submarine(), Vertical(0, 1))
        player.placeShip(Submarine(), Vertical(0, 2))
    }

    /*
    when player places all ships
    player state is READY_TO_START
     */
    @Test
    fun testPlayerReadyToStart() {
        val player = newPlayer()

        player.placeShip(Submarine(), Vertical(0, 0))
        player.placeShip(Submarine(), Vertical(1, 0))
        player.placeShip(Destroyer(), Vertical(2, 0))
        player.placeShip(Destroyer(), Vertical(3, 0))
        player.placeShip(Cruiser(), Vertical(4, 0))
        player.placeShip(Battleship(), Vertical(5, 0))
        player.placeShip(AircraftCarrier(), Vertical(6, 0))

        assertThat(player.state).isEqualTo(Player.State.READY_TO_START)
    }

    /*
    given player READY_TO_START
    when player removes ship
    player state is CREATING_BOARD
     */
    @Test
    fun testPlayerNotReadyToStartAnymore() {
        val player = newPlayer()

        player.placeShip(Submarine(), Vertical(0, 0))
        player.placeShip(Submarine(), Vertical(1, 0))
        player.placeShip(Destroyer(), Vertical(2, 0))
        player.placeShip(Destroyer(), Vertical(3, 0))
        player.placeShip(Cruiser(), Vertical(4, 0))
        player.placeShip(Battleship(), Vertical(5, 0))
        val shipId = player.placeShip(AircraftCarrier(), Vertical(6, 0))
        player.removeShip(shipId)

        assertThat(player.state).isEqualTo(Player.State.CREATING_BOARD)
    }

    private fun newPlayer() = Player()
}
