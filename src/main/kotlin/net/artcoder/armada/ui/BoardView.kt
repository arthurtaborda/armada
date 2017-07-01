package net.artcoder.armada.ui

import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import net.artcoder.armada.Board
import net.artcoder.armada.Point
import tornadofx.*

class BoardView() : View() {

    constructor(board: Board) : this() {
        board.placedShips
                .forEach { changeColor(it.points, CellColor.SHIP) }
    }

    val cells = mutableMapOf<Point, CellRect>()

    override val root = VBox()

    init {
        for (y in 0 until 10) {
            val line = HBox()
            root += line
            for (x in 0 until 10) {
                val cell = CellRect()
                cell.setOnMouseEntered {
                    fire(MouseEnteredCellEvent(Point(x, y), this))
                }
                cell.setOnMouseExited {
                    fire(MouseExitedCellEvent(Point(x, y), this))
                }
                cell.setOnMouseClicked {
                    fire(MouseClickedCellEvent(Point(x, y), this))
                }
                cells[Point(x, y)] = cell
                line += cell
            }
        }
    }

    fun changeColor(points: List<Point>, color: CellColor) {
        for (point in points) {
            changeColor(point, color)
        }
    }

    fun changeColor(point: Point, color: CellColor) {
        cells[point]!!.changeColor(color)
    }

    fun validHint(points: List<Point>) {
        for (point in points) {
            validHint(point)
        }
    }

    fun validHint(point: Point) {
        cells[point]!!.addHintValid()
    }

    fun invalidHint(points: List<Point>) {
        for (point in points) {
            invalidHint(point)
        }
    }

    fun invalidHint(point: Point) {
        cells[point]!!.addHintInvalid()
    }

    fun removeHint(points: List<Point>) {
        for (point in points) {
            removeHint(point)
        }
    }

    fun removeHint(point: Point) {
        cells[point]!!.rollbackColor()
    }
}

class MouseEnteredCellEvent(val point: Point, val board: BoardView) : FXEvent()
class MouseExitedCellEvent(val point: Point, val board: BoardView) : FXEvent()
class MouseClickedCellEvent(val point: Point, val board: BoardView) : FXEvent()