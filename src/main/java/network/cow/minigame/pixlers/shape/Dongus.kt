package network.cow.minigame.pixlers.shape

import java.awt.Point
import kotlin.math.abs

/**
 * @author Benedikt Wüller
 */
class Dongus : Shape {

    override fun calculatePixels(from: Point, to: Point): Set<Point> {
        val points = mutableSetOf<Point>()

        val width = abs(to.x - from.x)
        val height = abs(to.y - from.y)

        val min = Point(minOf(from.x, to.x), minOf(from.y, to.y))
        val max = Point(maxOf(from.x, to.x), maxOf(from.y, to.y))

        val circle = ShapeType.ELLIPSE.shape
        val rect = ShapeType.RECTANGLE.shape

        if (width > height) {
            val size = height / 2

            val centerOffset = height / 3
            points.addAll(rect.calculatePixels(Point(min.x + size / 2, min.y + centerOffset), Point(max.x - size / 2, max.y - centerOffset)))

            if (from.x < to.x) {
                points.addAll(circle.calculatePixels(min, Point(min.x + size, min.y + size)))
                points.addAll(circle.calculatePixels(Point(min.x, max.y - size), Point(min.x + size, max.y)))
                points.addAll(circle.calculatePixels(Point(max.x - size, min.y + centerOffset), Point(max.x, max.y - centerOffset)))
            } else {
                points.addAll(circle.calculatePixels(Point(max.x - size, min.y), Point(max.x, min.y + size)))
                points.addAll(circle.calculatePixels(Point(max.x - size, max.y -  size), max))
                points.addAll(circle.calculatePixels(Point(min.x, min.y + centerOffset), Point(min.x + size, max.y - centerOffset)))
            }
        } else {
            val size = width / 2

            val centerOffset = width / 3
            points.addAll(rect.calculatePixels(Point(min.x + centerOffset, min.y + size / 2), Point(max.x - centerOffset, max.y - size / 2)))

            if (from.y < to.y) {
                points.addAll(circle.calculatePixels(min, Point(min.x + size, min.y + size)))
                points.addAll(circle.calculatePixels(Point(max.x - size, min.y), Point(max.x, min.y + size)))
                points.addAll(circle.calculatePixels(Point(min.x + centerOffset, max.y - size), Point(max.x - centerOffset, max.y)))
            } else {
                points.addAll(circle.calculatePixels(Point(min.x, max.y - size), Point(min.x + size, max.y)))
                points.addAll(circle.calculatePixels(Point(max.x - size, max.y - size), max))
                points.addAll(circle.calculatePixels(Point(min.x + centerOffset, min.y), Point(max.x - centerOffset, min.y + size)))
            }
        }

        return points
    }

}
