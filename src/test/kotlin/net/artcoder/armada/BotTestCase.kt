package net.artcoder.armada

open class BotTestCase {

    protected fun pointGenerator() = PointGeneratorMock(listOf(Point(3, 4),
                                                               Point(1, 2),
                                                               Point(5, 6),
                                                               Point(7, 8)))

    protected fun pointGenerator(points: List<Point>) = PointGeneratorMock(points)

    protected fun bot(pointGenerator: PointGeneratorMock) = Bot(10, pointGenerator)
}