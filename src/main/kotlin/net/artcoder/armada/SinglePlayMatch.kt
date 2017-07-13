package net.artcoder.armada

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class SinglePlayMatch(private val eventBus: EventBus,
                      private val playerBoard: Board,
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

    @Subscribe fun handle(event: OpponentMissEvent) {
        bot.reportAttack(event.pointAttacked, AttackResult.MISS)
    }

    @Subscribe fun handle(event: OpponentHitEvent) {
        bot.reportAttack(event.pointAttacked, AttackResult.HIT)
        botAttack()
    }

    @Subscribe fun handle(event: OpponentSunkEvent) {
        bot.reportAttack(event.pointAttacked, AttackResult.SUNK)
        botAttack()
    }

    private fun botAttack() {
        CompletableFuture.supplyAsync(
                {
                    TimeUnit.MILLISECONDS.sleep(botDelay)

                    game.attack(bot.nextPoint())
                })
    }
}