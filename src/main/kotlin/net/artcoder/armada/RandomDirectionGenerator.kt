package net.artcoder.armada

import java.util.concurrent.ThreadLocalRandom

class RandomDirectionGenerator(private val pointGenerator: PointGenerator) : DirectionGenerator {

    override fun randomDirection(): Direction {
        val rand = ThreadLocalRandom.current()
        val isHorizontal = rand.nextBoolean()

        if(isHorizontal) {
            return Horizontal(pointGenerator.randomPoint())
        } else {
            return Vertical(pointGenerator.randomPoint())
        }
    }
}