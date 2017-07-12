package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class MatchTest {

    private var eventBus = EventBusSpy()

    @Before
    fun setUp() {
        eventBus = EventBusSpy()
    }

    @Test
    fun whenPlayerAttackPointWithNoShip_dispatchPlayerMissEvent() {
        val point = Point(0, 3)
        val match = match()
        match.attack(point)

        assertThat(eventBus.postCount(PlayerMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerMissEvent(point))).isTrue()
    }

    @Test
    fun whenPlayerAttackPointWithShip_dispatchPlayerHitEvent() {
        val point = Point(0, 8)
        val match = match()
        match.attack(point)

        assertThat(eventBus.postCount(PlayerHitEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerHitEvent(point))).isTrue()
    }

    @Test
    fun whenPlayerOneMissesAndOpponentMisses_dispatchOpponentMissEvent() {
        val match = match()
        match.attack(Point(0, 3)) //player one misses
        val point = Point(4, 4)
        match.attack(point) //player two misses

        assertThat(eventBus.postCount(OpponentMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentMissEvent(point))).isTrue()
    }

    @Test
    fun whenPlayerOneMissesAndOpponentHits_dispatchOpponentHitEvent() {
        val match = match()
        match.attack(Point(0, 3)) //player one misses
        val point = Point(1, 1)
        match.attack(point) //player two hits

        assertThat(eventBus.postCount(OpponentHitEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentHitEvent(point))).isTrue()
    }

    @Test
    fun whenPlayerSinks_dispatchPlayerSunkEvent() {
        val match = match()
        match.attack(Point(0, 8)) //player one hits
        match.attack(Point(1, 8)) //player one hits

        assertThat(eventBus.postCount(PlayerSunkEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerSunkEvent(listOf(Point (0, 8), Point(1, 8))))).isTrue()
    }

    @Test
    fun whenOpponentSinks_dispatchOpponentSunkEvent() {
        val match = match()
        match.attack(Point(0, 3)) //player one misses
        match.attack(Point(0, 0)) //player two hits
        match.attack(Point(1, 0)) //player two hits

        assertThat(eventBus.postCount(OpponentSunkEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentSunkEvent(listOf(Point(0, 0), Point(1, 0))))).isTrue()
    }

    @Test
    fun cannotAttackAlreadyAttackedPoint() {
        val match = match()
        match.attack(Point(0, 8)) //player one hits

        assertThat(match.canAttack(Point(1, 2))).isTrue()
        assertThat(match.canAttack(Point(9, 9))).isTrue()
        assertThat(match.canAttack(Point(4, 7))).isTrue()
        assertThat(match.canAttack(Point(0, 8))).isFalse()
    }

    private fun match(): BattleshipMatch {
        val playerBoard = playerBoard()
        val opponentBoard = opponentBoard()

        return BattleshipMatch(eventBus, playerBoard, opponentBoard)
    }

    private fun opponentBoard(): Board {
        // points: (0,8),(1,8),(0,9),(1,9),(2,9)
        val setupBoard = SetupBoard(intArrayOf(2, 3))
        setupBoard.place(Point(0, 8))
        setupBoard.place(Point(0, 9))

        return setupBoard.finish()
    }

    private fun playerBoard(): Board {
        // points: (0,0),(1,0),(0,1),(1,1),(2,1)
        val setupBoard = SetupBoard(intArrayOf(2, 3))
        setupBoard.place(Point(0, 0))
        setupBoard.place(Point(0, 1))

        return setupBoard.finish()
    }
}

