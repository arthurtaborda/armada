package net.artcoder.armada

class Vertical(val point: Point) : Direction {

    constructor(x: Int, y: Int) : this(Cell(x, y))

    override fun contains(point: Point, size: Int): Boolean {
        return points(size).contains(point)
    }

    override fun points(size: Int): List<Point> {
        return (0 until size).map { Cell(point.x, point.y + it) }
    }
}