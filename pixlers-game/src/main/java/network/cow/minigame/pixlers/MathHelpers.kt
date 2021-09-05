package network.cow.minigame.pixlers

import java.awt.Point
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */

val circlePatterns = mapOf(
        1 to arrayOf(
                Point(0, 0)
        ),
        2 to arrayOf(
                Point(0, 0), Point(1, 0),
                Point(0, 1), Point(1, 1)
        ),
        3 to arrayOf(
                Point(-1, -1), Point(0, -1), Point(1, -1),
                Point(-1, 0), Point(0, 0), Point(1, 0),
                Point(-1, 1), Point(0, 1), Point(1, 1)
        ),
        4 to arrayOf(
                Point(0, -1), Point(1, -1),
                Point(-1, 0), Point(0, 0), Point(1, 0), Point(2, 0),
                Point(-1, 1), Point(0, 1), Point(1, 1), Point(2, 1),
                Point(0, 2), Point(1, 2),
        ),
        5 to arrayOf(
                Point(-1, -2), Point(0, -2), Point(1, -2),
                Point(-2, -1), Point(-1, -1), Point(0, -1), Point(1, -1), Point(2, -1),
                Point(-2, 0), Point(-1, 0), Point(0, 0), Point(1, 0), Point(2, 0),
                Point(-2, 1), Point(-1, 1), Point(0, 1), Point(1, 1), Point(2, 1),
                Point(-1, 2), Point(0, 2), Point(1, 2),
        )
)

fun getPointsInCircle(center: Point, diameter: Int) : Set<Point> {
    // TODO: Use Ellipsis2D for larger circles.
    val pattern = circlePatterns[diameter] ?: return emptySet()
    return pattern.map { Point(center.x + it.x, center.y + it.y) }.toSet()
}

fun getPointsInLine(from: Point, to: Point) : Set<Point> {
    val points = mutableSetOf<Point>()

    var x = from.x
    var y = from.y

    val incrementX = if (from.x < to.x) 1 else -1
    val incrementY = if (from.y < to.y) 1 else -1

    val deltaX = abs(to.x - from.x)
    val deltaY = abs(to.y - from.y)

    val deltaXScale = 2 * deltaX
    val deltaYScale = 2 * deltaY

    var delta = 0

    if (deltaX >= deltaY) {
        while (true) {
            points.add(Point(x, y))
            if (x == to.x) break
            x += incrementX
            delta += deltaYScale
            if (delta > deltaX) {
                y += incrementY
                delta -= deltaXScale
            }
        }
    } else {
        while (true) {
            points.add(Point(x, y))
            if (y == to.y) break
            y += incrementY
            delta += deltaXScale
            if (delta > deltaY) {
                x += incrementX
                delta -= deltaYScale
            }
        }
    }

    return points
}
