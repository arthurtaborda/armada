package net.artcoder.armada

import java.util.concurrent.ThreadLocalRandom

class RandomPointGenerator(private val boardSize: Int) : PointGenerator {

    override fun randomPoint(): Point {
        val rand = ThreadLocalRandom.current()

        val x = rand.nextInt(0, boardSize)
        val y = rand.nextInt(0, boardSize)
        return Point(x, y)
    }
}