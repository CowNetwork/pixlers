package network.cow.minigame.pixlers

import java.awt.Point

/**
 * @author Benedikt Wüller
 */

fun getPointsInCircle(center: Point, radius: Int) = getPointsInEllipse(center, radius, radius)

fun getPointsInEllipse(center: Point, horizontalRadius: Int, verticalRadius: Int) : List<Point> {
    // https://stackoverflow.com/a/10322607/1690664

    val points = mutableListOf<Point>()

    val squaredVerticalRadius = verticalRadius * verticalRadius
    val squaredHorizontalRadius = horizontalRadius * horizontalRadius

    for (y in -verticalRadius until verticalRadius) {
        for (x in -horizontalRadius until horizontalRadius) {
            val squaredX = x * x
            val squaredY = y * y
            if (squaredX * squaredVerticalRadius + squaredY * squaredHorizontalRadius <= squaredVerticalRadius * squaredHorizontalRadius) {
                points.add(Point(center.x + x, center.y + y))
            }
        }
    }

    return points
}
