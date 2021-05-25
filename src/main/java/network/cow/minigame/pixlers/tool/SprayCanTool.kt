package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.getCoordinatesInCircle

/**
 * @author Benedikt Wüller
 */
open class SprayCanTool : Tool() {

    var size: Int = 3

    override val action: Action.(Int, Int) -> Unit = { x, y ->
        val coordinates = getCoordinatesInCircle(x, y, size)
        coordinates.filter { Math.random() >= 0.99 }.forEach { (x, y) -> this.setColor(x, y, color) }
    }

}
