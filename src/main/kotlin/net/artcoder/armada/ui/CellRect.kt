package net.artcoder.armada.ui

import javafx.scene.shape.Rectangle

class CellRect : Rectangle() {

    var savedColor: CellColor = CellColor.WATER

    init {
        width = 30.0
        height = 30.0
        fill = CellColor.WATER.fill
        stroke = CellColor.WATER.stroke
    }

    fun changeColor(cellColor: CellColor) {
        savedColor = cellColor
        rollbackColor()
    }

    fun addHintValid() {
        fill = CellColor.HINT_VALID.fill
        stroke = CellColor.HINT_VALID.stroke
    }

    fun addHintInvalid() {
        fill = CellColor.HINT_INVALID.fill
        stroke = CellColor.HINT_INVALID.stroke
    }

    fun rollbackColor() {
        fill = savedColor.fill
        stroke = savedColor.stroke
    }
}