package net.artcoder.armada

import com.google.common.truth.Truth


class PointGeneratorMock(boardSize: Int) : PointGenerator {

    constructor(boardSize: Int, points: List<Point>) : this(boardSize) {
        this.points = points.toMutableList()
    }

    private var count = 0
    private var points = mutableListOf<Point>()
    private val generator = RandomPointGenerator(boardSize)

    override fun randomPoint(): Point {
        count++
        return points.firstOrNull() ?: generator.randomPoint()
    }

    fun assertCount(timesUsed: Int) {
        Truth.assertThat(count).isEqualTo(timesUsed)
    }
}