package net.artcoder.armada.setup.gui

import com.google.common.eventbus.Subscribe
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import net.artcoder.armada.core.gui.BoardView
import net.artcoder.armada.setup.CanStartGameEvent
import net.artcoder.armada.setup.SetupBoardController

class SetupBoardView(boardView: BoardView,
                     controller : SetupBoardController) : VBox() {

    private val rotateButton = Button("Rotate")
    private val startButton = Button("Start")

    init {
        children.add(boardView)
        children.add(rotateButton)
        children.add(startButton)

        rotateButton.setOnMouseClicked {
            controller.rotate()
        }
        startButton.setOnMouseClicked {
            controller.start()
        }
        startButton.isDisable = true
    }

    @Subscribe
    fun handle(event : CanStartGameEvent) {
        startButton.isDisable = false
    }
}