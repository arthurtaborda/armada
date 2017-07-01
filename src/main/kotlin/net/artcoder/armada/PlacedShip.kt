package net.artcoder.armada

import java.util.*

data class PlacedShip(val points: List<Point>) {

    val id: UUID = UUID.randomUUID()
    private var hits = 0

    fun hit(point: Point): Boolean {
        if(points.contains(point)) {
            hits++
            return true
        }

        return false
    }

    fun isDead(): Boolean {
        return hits == points.size
    }
}