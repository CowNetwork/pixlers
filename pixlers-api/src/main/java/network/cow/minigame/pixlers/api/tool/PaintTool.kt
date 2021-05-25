package network.cow.minigame.pixlers.api.tool

import network.cow.minigame.pixlers.api.canvas.Canvas
import network.cow.minigame.pixlers.api.getCoordinatesInCircle

/**
 * @author Benedikt Wüller
 */
open class PaintTool(canvas: Canvas) : LayerTool(canvas) {

    protected open val sizes = listOf(1, 2, 3)

    var size: Int = 1

    override val primaryAction: Layer.() -> Unit = {
        val coordinates = getCoordinatesInCircle(cursorX, cursorY, size)
        coordinates.forEach { (x, y) -> this.setColor(x, y, getColor()) }
    }

    override val secondaryAction: Layer.() -> Unit = {
        val index = sizes.indexOf(size)
        val nextIndex = if (index == sizes.lastIndex) 0 else index + 1
        size = sizes[nextIndex]
    }

    protected open fun getColor() = this.canvas.currentColor

}
