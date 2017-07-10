package net.artcoder.armada.ui

import javafx.scene.control.Button
import net.artcoder.armada.*

class SetupBoardController(val gameStarter: GameStarter) : PointClickHandler,
                                                           PointEnterHandler,
                                                           PointExitHandler {

    private var isHorizontal = true
    val playerBoard = BoardView(this, this, this)
    val startGameButton = Button("Start Game")
    val rotateButton = Button("Rotate Ship")

    init {
        startGameButton.isDisable = true
        startGameButton.setOnMouseClicked {
            if (true) {
                gameStarter.startGame(Player(createBoard()))
            }
        }
        rotateButton.setOnMouseClicked {
            rotate()
        }
    }

    private val setupBoard = defaultBoard()

    override fun enter(point: Point) {
        if (!canFinish()) {
            if (canPlace(point)) {
                playerBoard.validHint(pointsToHint(point))
            } else {
                playerBoard.invalidHint(pointsToHint(point))
            }
        }
    }

    override fun exit(point: Point) {
        if (!canFinish()) {
            playerBoard.removeHint(pointsToHint(point))
        }
    }

    override fun click(point: Point) {
        if (canPlace(point)) {
            playerBoard.changeColor(pointsToHint(point), CellColor.SHIP)
            placeShip(point)
            startGameButton.isDisable = !canFinish()
        }
    }

    fun canPlace(point: Point): Boolean {
        val direction = direction(point)
        return setupBoard.canPlace(direction)
    }

    fun pointsToHint(point: Point): List<Point> {
        val direction = direction(point)
        return setupBoard.pointsAvailable(direction)
    }

    fun placeShip(point: Point) {
        setupBoard.placeShip(direction(point))
    }

    fun canFinish(): Boolean {
        return setupBoard.canFinish()
    }

    private fun direction(point: Point): Direction {
        if (isHorizontal) {
            return Horizontal(point)
        } else {
            return Vertical(point)
        }
    }

    fun rotate() {
        isHorizontal = !isHorizontal
    }

    fun createBoard(): Board {
        return setupBoard.finish()
    }

    private fun defaultBoard(): SetupBoard {
        return SetupBoard(10, listOf(1, 1, 2, 2, 3, 4, 5))
    }
}