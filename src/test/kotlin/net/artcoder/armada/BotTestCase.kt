package net.artcoder.armada

open class BotTestCase {

    protected val boardSize = 10

    protected fun pointGenerator() = PointGeneratorMock(boardSize, listOf(Point(3, 4),
                                                                          Point(1, 2),
                                                                          Point(5, 6),
                                                                          Point(7, 8)))

    protected fun pointGenerator(points: List<Point>) = PointGeneratorMock(boardSize, points)

    protected fun bot(pointGenerator: PointGeneratorMock) = Bot(boardSize, pointGenerator)
}