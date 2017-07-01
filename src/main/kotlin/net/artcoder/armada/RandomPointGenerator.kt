package net.artcoder.armada

import java.util.concurrent.ThreadLocalRandom

class RandomPointGenerator(private val maxPoint: Point) : PointGenerator {

    override fun randomPoint(): Point {
        val rand = ThreadLocalRandom.current()

        val x = rand.nextInt(0, maxPoint.x)
        val y = rand.nextInt(0, maxPoint.y)
        return Point(x, y)
    }
}