package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import net.artcoder.armada.Point

class BoardView(val name: String, eventBus: EventBus) : VBox() {

    constructor(eventBus: EventBus): this("", eventBus)

    private val cells = mutableMapOf<Point, CellView>()

    init {
        for (y in 0 until 10) {
            val line = HBox()
            for (x in 0 until 10) {
                val point = Point(x, y)
                val cell = CellView()
                cell.setOnMouseClicked { eventBus.post(MouseClickedCellEvent(point, this)) }
                cell.setOnMouseEntered { eventBus.post(MouseEnteredCellEvent(point, this)) }
                cell.setOnMouseExited { eventBus.post(MouseExitedCellEvent(point, this)) }
                cells[point] = cell
                line.children.add(cell)
            }
            children.add(line)
        }
    }


    fun validHint(points: List<Point>) {
        for (point in points) {
            validHint(point)
        }
    }

    fun validHint(point: Point) {
        cells[point]!!.toHintValid()
    }

    fun invalidHint(points: List<Point>) {
        for (point in points) {
            invalidHint(point)
        }
    }

    fun ship(points: List<Point>) {
        for (point in points) {
            cells[point]!!.toShip()
        }
    }

    fun invalidHint(point: Point) {
        cells[point]!!.toHintInvalid()
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

class MouseClickedCellEvent(val point: Point, val board: BoardView)
class MouseEnteredCellEvent(val point: Point, val board: BoardView)
class MouseExitedCellEvent(val point: Point, val board: BoardView)
