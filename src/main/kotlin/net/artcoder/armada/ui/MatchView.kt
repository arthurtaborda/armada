package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class MatchView(eventBus: EventBus) : VBox() {

    private val controller = MatchController(eventBus)

    private val playerBoardView = BoardView("player", eventBus)
    private val opponentBoardView = BoardView("opponent", eventBus)

    init {
        eventBus.register(controller)

        children.add(Label("You"))
        children.add(playerBoardView)
        children.add(Label("Opponent"))
        children.add(opponentBoardView)
    }
}

