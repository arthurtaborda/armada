package net.artcoder.armada

import com.google.common.truth.Truth


class PointGeneratorMock(val points: List<Point>) : PointGenerator {

    private var count = 0

    override fun randomPoint(): Point {
        count++
        return points.first()
    }

    fun assertCount(timesUsed: Int) {
        Truth.assertThat(count).isEqualTo(timesUsed)
    }
}