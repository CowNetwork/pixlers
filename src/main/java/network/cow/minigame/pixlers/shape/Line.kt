package network.cow.minigame.pixlers.shape

import java.awt.Point
import kotlin.math.abs

/**
 * @author Benedikt Wüller
 */
class Line : Shape {

    override fun calculatePixels(from: Point, to: Point) : Set<Point> {
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
                println(Point(x, y))
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
                println(Point(x, y))
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

}
