package network.cow.minigame.pixlers.shape

import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class Rectangle : Shape {

    override fun calculatePixels(from: Point, to: Point) : Set<Point> {
        val points = mutableSetOf<Point>()

        val minX = minOf(from.x, to.x)
        val maxX = maxOf(from.x, to.x)

        val minY = minOf(from.y, to.y)
        val maxY = maxOf(from.y, to.y)

        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                points.add(Point(x, y))
            }
        }

        return points
    }

}
