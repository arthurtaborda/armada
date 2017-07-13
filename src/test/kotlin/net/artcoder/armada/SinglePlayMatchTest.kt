package net.artcoder.armada

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

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

    private fun match(): SinglePlayMatch {
        val playerBoard = BoardObjectMother.playerBoard()
        val botBoard = BoardObjectMother.opponentBoard()
        return SinglePlayMatch(eventBus, playerBoard, botBoard, bot)
    }
}