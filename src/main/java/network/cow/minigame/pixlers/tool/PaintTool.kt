package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.getCoordinatesInCircle

/**
 * @author Benedikt Wüller
 */
open class PaintTool : Tool() {

    protected open val sizes = listOf(1, 2, 3)

    var size: Int = 1

    override val primaryAction: Action.(Int, Int) -> Unit = { x, y ->
        val coordinates = getCoordinatesInCircle(x, y, size)
        coordinates.forEach { (x, y) -> this.setColor(x, y, color) }
    }

    override val secondaryAction: Action.(Int, Int) -> Unit = { _, _ ->
        val index = sizes.indexOf(size)
        val nextIndex = if (index == sizes.lastIndex) 0 else index + 1
        size = sizes[nextIndex]
    }

}
