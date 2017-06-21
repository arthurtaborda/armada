package net.artcoder.armada

class Horizontal(val point: Point) : Direction {

    constructor(x: Int, y: Int) : this(Point(x, y))

    override fun contains(point: Point, size: Int): Boolean {
        return points(size).contains(point)
    }

    override fun points(size: Int): List<Point> {
        return (0 until size).map { Point(point.x + it, point.y) }
    }
}