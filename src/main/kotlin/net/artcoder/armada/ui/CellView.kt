package net.artcoder.armada.ui

import javafx.scene.shape.Rectangle

class CellView : Rectangle() {

    var savedColor: CellColor = CellColor.WATER

    init {
        width = 30.0
        height = 30.0
        fill = CellColor.WATER.fill
        stroke = CellColor.WATER.stroke
    }


    fun toHintValid() {
        fill = CellColor.HINT_VALID.fill
        stroke = CellColor.HINT_VALID.stroke
    }

    fun toHintInvalid() {
        fill = CellColor.HINT_INVALID.fill
        stroke = CellColor.HINT_INVALID.stroke
    }

    fun rollbackColor() {
        fill = savedColor.fill
        stroke = savedColor.stroke
    }

    fun toShip() {
        savedColor = CellColor.SHIP
        fill = CellColor.SHIP.fill
        stroke = CellColor.SHIP.stroke
    }
}