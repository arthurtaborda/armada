package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import net.artcoder.armada.core.Point
import net.artcoder.armada.match.*
import org.awaitility.Awaitility.await
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit.SECONDS

class SinglePlayMatchTest {

    private var bot = BotMock()
    private var eventBus = EventBusSpy()

    @Before
    fun setUp() {
        bot = BotMock()
        eventBus = EventBusSpy()
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

    @Test
    fun whenBotAttackMisses_dispatchOpponentMissEvent_thenEndsTurn() {
        bot.putRandomPoint(Point(5, 7))

        val match = match()
        match.attack(Point(0, 3)) //player one misses

        await().timeout(3, SECONDS).until {
            eventBus.contains(OpponentMissEvent(Point(5, 7)))
        }

        assertThat(eventBus.postCount(PlayerMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerMissEvent(Point(0, 3)))).isTrue()
        assertThat(eventBus.postCount(OpponentMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentMissEvent(Point(5, 7)))).isTrue()
        assertThat(bot.randomCount).isEqualTo(1)
    }

    @Test
    fun whenBotAttackHits_dispatchOpponentHitEvent_thenAttacksAgain() {
        bot.putRandomPoint(Point(0, 0))
        bot.putRandomPoint(Point(5, 7))

        val match = match()
        match.attack(Point(0, 3)) //player one misses

        await().timeout(3, SECONDS).until {
            eventBus.contains(OpponentMissEvent(Point(5, 7)))
        }

        assertThat(eventBus.postCount(PlayerMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerMissEvent(Point(0, 3)))).isTrue()
        assertThat(eventBus.postCount(OpponentHitEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentHitEvent(Point(0, 0)))).isTrue()
        assertThat(eventBus.postCount(OpponentMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentMissEvent(Point(5, 7)))).isTrue()
        assertThat(bot.randomCount).isEqualTo(2)
    }

    @Test
    fun whenBotAttackSunks_dispatchOpponentSunkEvent() {
        bot.putRandomPoint(Point(0, 0))
        bot.putRandomPoint(Point(1, 0))
        bot.putRandomPoint(Point(5, 7))

        val match = match()
        match.attack(Point(0, 3)) //player one misses

        await().timeout(3, SECONDS).until {
            eventBus.contains(OpponentMissEvent(Point(5, 7)))
        }

        assertThat(eventBus.postCount(PlayerMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(PlayerMissEvent(Point(0, 3)))).isTrue()
        assertThat(eventBus.postCount(OpponentHitEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentHitEvent(Point(0, 0)))).isTrue()
        assertThat(eventBus.postCount(OpponentSunkEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentSunkEvent(Point(1, 0), listOf(Point(0, 0),
                                                                           Point(1, 0))))).isTrue()
        assertThat(eventBus.postCount(OpponentMissEvent::class)).isEqualTo(1)
        assertThat(eventBus.contains(OpponentMissEvent(Point(5, 7)))).isTrue()
        assertThat(bot.randomCount).isEqualTo(3)
    }

    private fun match(): SinglePlayMatch {
        val playerBoard = BoardObjectMother.playerBoard()
        val botBoard = BoardObjectMother.opponentBoard()
        val match = SinglePlayMatch(eventBus, playerBoard, botBoard, bot)
        eventBus.register(match)
        return match
    }
}