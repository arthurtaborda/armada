package net.artcoder.armada

import com.google.common.truth.Truth


class PointGeneratorMock(points: List<Point>) : PointGenerator {

    private val points = points.toMutableList()
    private var count = 0

    override fun randomPoint(): Point {
        count++
        return points.removeAt(0)
    }

    fun verifyCount(timesUsed: Int) {
        Truth.assertThat(count).isEqualTo(timesUsed)
    }
}