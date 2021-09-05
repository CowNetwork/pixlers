package network.cow.minigame.pixlers.shape

import network.cow.minigame.pixlers.getPointsInLine
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class Line : Shape {

    override fun calculatePixels(from: Point, to: Point) = getPointsInLine(from, to)

}
