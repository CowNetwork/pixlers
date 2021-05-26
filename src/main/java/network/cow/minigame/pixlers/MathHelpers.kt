package network.cow.minigame.pixlers

import java.awt.Point
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */

fun getPointsInCircle(center: Point, diameter: Int) = getPointsInEllipse(center, diameter, diameter)

fun getPointsInEllipse(center: Point, horizontalDiameter: Int, verticalDiameter: Int) : List<Point> {
    // https://stackoverflow.com/a/10322607/1690664

    val points = mutableListOf<Point>()

    val verticalRadius = verticalDiameter / 2.0
    val horizontalRadius = horizontalDiameter / 2.0

    val squaredVerticalRadius = verticalRadius.pow(2)
    val squaredHorizontalRadius = horizontalRadius.pow(2)

    for (y in 0 until verticalDiameter) {
        for (x in 0 until horizontalDiameter) {
            val squaredX = (x - horizontalRadius).pow(2)
            val squaredY = (y - verticalRadius).pow(2)
            if (squaredX * squaredVerticalRadius + squaredY * squaredHorizontalRadius <= squaredVerticalRadius * squaredHorizontalRadius) {
                points.add(Point(
                        (center.x - horizontalRadius + x).roundToInt(),
                        (center.y - verticalRadius + y).roundToInt()
                ))
            }
        }
    }

    return points
}
