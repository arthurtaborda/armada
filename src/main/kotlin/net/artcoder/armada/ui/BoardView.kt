package net.artcoder.armada.ui

import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import net.artcoder.armada.Point

class BoardView(mouseClickedHandler: PointClickHandler,
                mouseEnteredHandler: PointEnterHandler,
                mouseExitedHandler: PointExitHandler) : VBox() {


    val cells = mutableMapOf<Point, CellRect>()

    init {
        for (y in 0 until 10) {
            val line = HBox()
            for (x in 0 until 10) {
                val point = Point(x, y)
                val cell = CellRect(point, this)
                cell.setOnMouseClicked { mouseClickedHandler.click(point) }
                cell.setOnMouseEntered { mouseEnteredHandler.enter(point) }
                cell.setOnMouseExited { mouseExitedHandler.exit(point) }
                cells[point] = cell
                line.children.add(cell)
            }
            children.add(line)
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