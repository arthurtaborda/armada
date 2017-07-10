package net.artcoder.armada.ui

import javafx.scene.control.Label
import javafx.scene.layout.VBox
import net.artcoder.armada.Player

class MatchView(playerOne: Player, playerTwo: Player) : VBox() {

    private val controller = MatchController(playerOne, playerTwo)

    init {

        children.add(Label("You"))
        children.add(controller.playerBoard)
        children.add(Label("Opponent"))
        children.add(controller.opponentBoard)
    }
}