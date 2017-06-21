package net.artcoder.armada

interface Direction {

    fun points(size: Int): List<Point>

    fun contains(point: Point, size: Int): Boolean
}