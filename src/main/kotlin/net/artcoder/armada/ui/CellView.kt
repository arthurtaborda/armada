package net.artcoder.armada.ui

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class CellView : Rectangle() {

    private var savedColor: CellColor = CellColor.WATER

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
        changeColor(CellColor.SHIP)
    }

    fun toHit() {
        changeColor(CellColor.HIT)
    }

    fun toMiss() {
        changeColor(CellColor.MISS)
    }

    fun toSunk() {
        changeColor(CellColor.SUNK)
    }

    private fun changeColor(cellColor: CellColor) {
        savedColor = cellColor
        fill = cellColor.fill
        stroke = cellColor.stroke
    }
}

private enum class CellColor(private val fillR: Int,
                             private val fillG: Int,
                             private val fillB: Int,
                             private val strokeR: Int,
                             private val strokeG: Int,
                             private val strokeB: Int) {

    WATER(36, 166, 254, 5, 12, 221),
    HINT_VALID(47, 196, 54, 0, 45, 3),
    HINT_INVALID(207, 60, 60, 98, 18, 32),
    MISS(40, 40, 40, 0, 0, 0),
    HIT(204, 110, 50, 0, 0, 0),
    SUNK(200, 0, 0, 0, 0, 0),
    SHIP(178, 194, 207, 0, 0, 0);

    val fill: Color
        get() = Color.rgb(fillR, fillG, fillB)
    val stroke: Color
        get() = Color.rgb(strokeR, strokeG, strokeB)
}