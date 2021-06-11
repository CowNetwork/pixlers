package network.cow.minigame.pixlers

import java.awt.Point
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
