package net.artcoder.armada.match.gui

import com.google.common.eventbus.Subscribe
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import net.artcoder.armada.core.gui.BoardView
import net.artcoder.armada.match.*

class MatchView(private val playerBoardView: BoardView,
                private val opponentBoardView: BoardView) : VBox() {

    init {
        children.add(Label("You"))
        children.add(playerBoardView)
        children.add(Label("Opponent"))
        children.add(opponentBoardView)
    }

    @Subscribe fun handle(event: PlayerMissEvent) {
        opponentBoardView.miss(event.pointAttacked)
    }

    @Subscribe fun handle(event: PlayerHitEvent) {
        opponentBoardView.hit(event.pointAttacked)
    }

    @Subscribe fun handle(event: PlayerSunkEvent) {
        opponentBoardView.sunk(event.points)
    }

    @Subscribe fun handle(event: OpponentMissEvent) {
        playerBoardView.miss(event.pointAttacked)
    }

    @Subscribe fun handle(event: OpponentHitEvent) {
        playerBoardView.hit(event.pointAttacked)
    }

    @Subscribe fun handle(event: OpponentSunkEvent) {
        playerBoardView.sunk(event.points)
    }
}

