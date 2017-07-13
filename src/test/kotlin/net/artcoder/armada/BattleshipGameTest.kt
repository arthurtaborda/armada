package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BattleshipGameTest {

    private var eventBus = EventBusSpy()

    @Before
    fun setUp() {
        eventBus = EventBusSpy()
    }

    @Test
    fun whenPlayerAttackPointWithNoShip_dispatchPlayerMissEvent() {
        val point = Point(0, 3)
        val game = game()
        game.attack(point)

        assertThat(eventBus.postCount(PlayerMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerMissEvent(point))).isTrue()
    }

    @Test
    fun whenPlayerAttackPointWithShip_dispatchPlayerHitEvent() {
        val point = Point(0, 8)
        val game = game()
        game.attack(point)

        assertThat(eventBus.postCount(PlayerHitEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerHitEvent(point))).isTrue()
    }

    @Test
    fun whenPlayerOneMissesAndOpponentMisses_dispatchOpponentMissEvent() {
        val game = game()
        game.attack(Point(0, 3)) //player one misses
        val point = Point(4, 4)
        game.attack(point) //player two misses

        assertThat(eventBus.postCount(OpponentMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentMissEvent(point))).isTrue()
    }

    @Test
    fun whenPlayerOneMissesAndOpponentHits_dispatchOpponentHitEvent() {
        val game = game()
        game.attack(Point(0, 3)) //player one misses
        val point = Point(1, 1)
        game.attack(point) //player two hits

        assertThat(eventBus.postCount(OpponentHitEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentHitEvent(point))).isTrue()
    }

    @Test
    fun whenPlayerSinks_dispatchPlayerSunkEvent() {
        val game = game()
        game.attack(Point(0, 8)) //player one hits
        game.attack(Point(1, 8)) //player one hits

        assertThat(eventBus.postCount(PlayerSunkEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerSunkEvent(Point(1, 8), listOf(Point (0, 8), Point(1, 8))))).isTrue()
    }

    @Test
    fun whenOpponentSinks_dispatchOpponentSunkEvent() {
        val game = game()
        game.attack(Point(0, 3)) //player one misses
        game.attack(Point(0, 0)) //player two hits
        game.attack(Point(1, 0)) //player two hits

        assertThat(eventBus.postCount(OpponentSunkEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentSunkEvent(Point(1, 0), listOf(Point(0, 0), Point(1, 0))))).isTrue()
    }

    @Test
    fun whenOpponentSinksAll_dispatchOpponentWonEvent() {
        val game = game()
        game.attack(Point(0, 3)) //player one misses
        game.attack(Point(0, 0)) //player two hits
        game.attack(Point(1, 0)) //player two hits
        game.attack(Point(0, 1)) //player two hits
        game.attack(Point(1, 1)) //player two hits
        game.attack(Point(2, 1)) //player two hits

        assertThat(eventBus.postCount(OpponentWonEvent::class)).isEqualTo(1)
        assertThat(eventBus.postCount(OpponentSunkEvent::class)).isEqualTo(2)
        assertThat(eventBus.contains(OpponentSunkEvent(Point(1, 0), listOf(Point(0, 0), Point(1, 0))))).isTrue()
        assertThat(eventBus.contains(OpponentSunkEvent(Point(2, 1), listOf(Point(0, 1),
                                                                           Point(1, 1),
                                                                           Point(2, 1))))).isTrue()
    }

    @Test
    fun whenPlayerSinksAll_dispatchPlayerWonEvent() {
        val game = game()
        game.attack(Point(0, 8)) //player one hits
        game.attack(Point(1, 8)) //player one hits
        game.attack(Point(0, 9)) //player one hits
        game.attack(Point(1, 9)) //player one hits
        game.attack(Point(2, 9)) //player one hits

        assertThat(eventBus.postCount(PlayerWonEvent::class)).isEqualTo(1)
        assertThat(eventBus.postCount(PlayerSunkEvent::class)).isEqualTo(2)
        assertThat(eventBus.contains(PlayerSunkEvent(Point(1, 8), listOf(Point(0, 8), Point(1, 8))))).isTrue()
        assertThat(eventBus.contains(PlayerSunkEvent(Point(2, 9), listOf(Point(0, 9),
                                                                           Point(1, 9),
                                                                           Point(2, 9))))).isTrue()
    }

    private fun game(): BattleshipGame {
        val playerBoard = BoardObjectMother.playerBoard()
        val opponentBoard = BoardObjectMother.opponentBoard()

        return BattleshipGame(eventBus, playerBoard, opponentBoard)
    }
}

