package net.artcoder.armada.core

data class PlacedShip(val points: List<Point>) {

    private var hits = 0

    fun hit(point: Point): Boolean {
        if (points.contains(point)) {
            hits++
            return true
        }

        return false
    }

    fun isDead(): Boolean {
        return hits == points.size
    }
}