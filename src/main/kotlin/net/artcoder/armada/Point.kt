package net.artcoder.armada

open class Point(val x: Int, val y: Int) {

    fun up() = Point(x, y - 1)
    fun right() = Point(x + 1, y)
    fun left() = Point(x - 1, y)
    fun down() = Point(x, y + 1)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "($x,$y)"
    }
}