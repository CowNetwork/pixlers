package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.getPointsInCircle

/**
 * @author Benedikt Wüller
 */
open class PaintTool(canvas: Canvas) : LayerTool(canvas) {

    protected open val sizes = listOf(1, 2, 3, 4, 5)

    var size: Int = 1; private set

    override val primaryAction: Layer.() -> Unit = {
        val coordinates = getPointsInCircle(cursor, size)
        coordinates.forEach { this.setColor(it.x, it.y, getColor()) }
    }

    override val secondaryAction: Layer.() -> Unit = {
        val index = sizes.indexOf(size)
        val nextIndex = if (index == sizes.lastIndex) 0 else index + 1
        size = sizes[nextIndex]
    }

    protected open fun getColor() = this.canvas.currentColor

}
