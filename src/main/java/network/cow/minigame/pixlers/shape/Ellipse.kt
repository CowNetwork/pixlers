package network.cow.minigame.pixlers.shape

import java.awt.Point
import java.awt.geom.Ellipse2D
import kotlin.math.abs

/**
 * @author Benedikt Wüller
 */
class Ellipse : Shape {

    override fun calculatePixels(from: Point, to: Point): Set<Point> {
        val points = mutableSetOf<Point>()

        val minX = minOf(from.x, to.x) - 1
        val minY = minOf(from.y, to.y) - 1
        val width = abs(to.x - from.x) + 2
        val height = abs(to.y - from.y) + 2

        val ellipse = Ellipse2D.Double(minX.toDouble(), minY.toDouble(), width.toDouble(), height.toDouble())

        for (dx in 0 until width) {
            for (dy in 0 until height) {
                val x = minX + dx
                val y = minY + dy
                if (!ellipse.contains(x.toDouble(), y.toDouble())) continue
                points.add(Point(x, y))
            }
        }

        return points
    }

}
