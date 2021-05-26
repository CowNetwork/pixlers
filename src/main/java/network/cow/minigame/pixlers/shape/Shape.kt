package network.cow.minigame.pixlers.shape

import java.awt.Point

/**
 * @author Benedikt Wüller
 */
interface Shape {

    fun calculatePixels(from: Point, to: Point) : List<Point>

}
