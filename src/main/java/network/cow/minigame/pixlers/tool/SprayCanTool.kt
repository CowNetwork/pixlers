package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.getCoordinatesInCircle

/**
 * @author Benedikt Wüller
 */
open class SprayCanTool(canvas: Canvas) : LayerTool(canvas) {

    protected open val sizes = listOf(1, 2, 3, 4, 5)

    var size: Int = 3

    override val primaryAction: Layer.() -> Unit = {
        val coordinates = getCoordinatesInCircle(cursorX, cursorY, size)
        coordinates.filter { Math.random() >= 0.99 }.forEach { (x, y) -> this.setColor(x, y, canvas.currentColor) }
    }

    override val secondaryAction: Layer.() -> Unit = {
        val index = sizes.indexOf(size)
        val nextIndex = if (index == sizes.lastIndex) 0 else index + 1
        size = sizes[nextIndex]
    }

}
