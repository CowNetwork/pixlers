package network.cow.minigame.pixlers.shape

import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class Line : Shape {

    override fun calculatePixels(from: Point, to: Point) : List<Point> {
        val delta = Point(to.x - from.x, to.y - from.y)

        val current = Point(from)
        var p = 2.0 * delta.y - delta.x

        val points = mutableListOf<Point>()

        while (current.x < from.x) {
            points.add(Point(current))
            if (p >= 0) {
                current.y += 1
                p = p + 2.0 * delta.y - 2.0 * delta.x
            } else {
                p += 2.0 * delta.y
                current.x += 1
            }
        }

        return points
    }

}
