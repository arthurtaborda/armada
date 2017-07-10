package net.artcoder.armada.ui

import javafx.scene.layout.VBox

class SetupBoardView(gameStarter: GameStarter) : VBox() {

    val controller: SetupBoardController = SetupBoardController(gameStarter)

    init {
        children.add(controller.playerBoard)
        children.add(controller.startGameButton)
        children.add(controller.rotateButton)
    }
}