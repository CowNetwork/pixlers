package network.cow.minigame.pixlers.shape

import network.cow.minigame.pixlers.getPointsInEllipse
import java.awt.Point
import kotlin.math.abs
import kotlin.math.ceil

/**
 * @author Benedikt Wüller
 */
class Ellipse : Shape {

    override fun calculatePixels(from: Point, to: Point): List<Point> {
        val horizontalRadius = abs(to.x - from.x) / 2
        val verticalRadius = abs(to.y - from.y) / 2
        val center = Point(ceil((to.x - from.x) / 2.0).toInt(), ceil((to.y - from.y) / 2.0).toInt())
        return getPointsInEllipse(center, horizontalRadius, verticalRadius)
    }

}
