package net.artcoder.armada.ui

import net.artcoder.armada.Player
import tornadofx.*

class SetupBoardView : View() {

    val controller: SetupBoardController by inject()
    val playerBoard = BoardView()

    val startGameButton = button("Start Game") {
        isDisable = true
        action {
            replaceWith(MatchView(Player(controller.createBoard()), RandomPlayer.create()))
        }
    }
    val rotateButton = button("Rotate Ship") {
        action {
            controller.rotate()
        }
    }

    init {
        subscribe<MouseEnteredCellEvent> { event ->
            if (!controller.canFinish()) {
                if (controller.canPlace(event.point)) {
                    playerBoard.validHint(controller.pointsToHint(event.point))
                } else {
                    playerBoard.invalidHint(controller.pointsToHint(event.point))
                }
            }
        }
        subscribe<MouseExitedCellEvent> { event ->
            if (!controller.canFinish()) {
                playerBoard.removeHint(controller.pointsToHint(event.point))
            }
        }
        subscribe<MouseClickedCellEvent> { event ->
            if (controller.canPlace(event.point)) {
                playerBoard.changeColor(controller.pointsToHint(event.point), CellColor.SHIP)
                controller.placeShip(event.point)
                startGameButton.isDisable = !controller.canFinish()
            }
        }
    }

    override val root = vbox {
        add(playerBoard)
        hbox {
            add(rotateButton)
            add(startGameButton)
        }
    }
}