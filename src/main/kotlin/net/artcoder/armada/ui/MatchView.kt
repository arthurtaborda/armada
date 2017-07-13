package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import net.artcoder.armada.*

class MatchView(eventBus: EventBus, match: SinglePlayMatch) : VBox() {

    private val controller = MatchController(eventBus, match)

    private val playerBoardView = BoardView("player", eventBus)
    private val opponentBoardView = BoardView("opponent", eventBus)

    init {
        eventBus.register(controller)
        eventBus.register(this)

        children.add(Label("You"))
        children.add(playerBoardView)
        children.add(Label("Opponent"))
        children.add(opponentBoardView)
    }

    @Subscribe fun handle(event: PlayerMissEvent) {
        println("player-miss")
        opponentBoardView.miss(event.pointAttacked)
    }

    @Subscribe fun handle(event: PlayerHitEvent) {
        println("player-hit")
        opponentBoardView.hit(event.pointAttacked)
    }

    @Subscribe fun handle(event: PlayerSunkEvent) {
        println("player-sunk")
        opponentBoardView.sunk(event.points)
    }

    @Subscribe fun handle(event: OpponentMissEvent) {
        println("opponent-miss")
        playerBoardView.miss(event.pointAttacked)
    }

    @Subscribe fun handle(event: OpponentHitEvent) {
        println("opponent-hit")
        playerBoardView.hit(event.pointAttacked)
    }

    @Subscribe fun handle(event: OpponentSunkEvent) {
        println("opponent-sunk")
        playerBoardView.sunk(event.points)
    }
}

