package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.getCoordinatesInCircle

/**
 * @author Benedikt Wüller
 */
open class PaintTool : Tool() {

    var size: Int = 1

    override val action: Action.(Int, Int) -> Unit = { x, y ->
        val coordinates = getCoordinatesInCircle(x, y, size)
        coordinates.forEach { (x, y) -> this.setColor(x, y, color) }
    }

}
