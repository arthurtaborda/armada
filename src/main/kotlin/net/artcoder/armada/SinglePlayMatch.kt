package net.artcoder.armada

import com.google.common.eventbus.EventBus

class SinglePlayMatch(private val eventBus: EventBus,
                      private val playerBoard: Board,
                      private val botBoard: Board,
                      private val bot: Bot) {

    private val game = BattleshipGame(eventBus, playerBoard, botBoard)


    fun attack(point: Point) {
        game.attack(point)
    }

    fun canAttack(point: Point): Boolean {
        return botBoard.canAttack(point)
    }
}