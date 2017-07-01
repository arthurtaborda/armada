package net.artcoder.armada.ui

import javafx.scene.paint.Color

enum class CellColor(private val fillR: Int,
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
    SUNK(0, 0, 0, 0, 0, 0),
    SHIP(178, 194, 207, 0, 0, 0);

    val fill: Color
        get() = Color.rgb(fillR, fillG, fillB)
    val stroke: Color
        get() = Color.rgb(strokeR, strokeG, strokeB)
}