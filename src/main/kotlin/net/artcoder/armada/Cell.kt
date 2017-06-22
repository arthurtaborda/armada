package net.artcoder.armada

data class Cell(override val x: Int, override val y: Int) : Point {

    override fun up() = Cell(x, y - 1)
    override fun right() = Cell(x + 1, y)
    override fun left() = Cell(x - 1, y)
    override fun down() = Cell(x, y + 1)
}