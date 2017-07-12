package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class SetupBoardView(eventBus: EventBus) : VBox() {

    private val boardView = BoardView(eventBus)
    private val rotateButton = Button("Rotate")
    private val startButton = Button("Start")
    private val controller = SetupBoardController(eventBus)

    init {
        eventBus.register(controller)
        eventBus.register(this)
    }

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