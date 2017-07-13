package net.artcoder.armada.match

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import net.artcoder.armada.bot.Bot
import net.artcoder.armada.core.Point
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.TimeUnit.MILLISECONDS

class SinglePlayMatch(eventBus: EventBus,
                      playerBoard: Board,
                      private val botBoard: Board,
                      private val bot: Bot) {

    private val botDelay = 500L

    private val game = BattleshipGame(eventBus, playerBoard, botBoard)


    fun attack(point: Point) {
        game.attack(point)
    }

    fun canAttack(point: Point): Boolean {
        return botBoard.canAttack(point)
    }

    @Subscribe fun handle(event: PlayerMissEvent) {
        botAttack()
    }

    @Subscribe fun handle(event: OpponentHitEvent) {
        botAttack()
    }

    @Subscribe fun handle(event: OpponentSunkEvent) {
        botAttack()
    }

    private fun botAttack() {
        supplyAsync({
                        MILLISECONDS.sleep(botDelay)

                        game.attack(bot.nextPoint())
                    })
    }
}